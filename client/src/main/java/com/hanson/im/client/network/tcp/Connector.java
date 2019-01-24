package com.hanson.im.client.network.tcp;

import com.hanson.im.client.handler.HimClientHandler;
import com.hanson.im.client.handler.IMReceiver;
import com.hanson.im.client.handler.IMSender;
import com.hanson.im.common.cryption.Cryptor;
import com.hanson.im.common.decode.HimDecoder;
import com.hanson.im.common.encode.HimEncoder;
import com.hanson.im.common.protocol.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
@Slf4j
public class Connector implements IMSender {

    /**
     * nio event loop grouop
     */
    private EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    private IMReceiver imReceiver;

    private Map<String, Set<String>> chatCache;
    private Map<String, Cryptor> cryptorCache;

    public Connector(IMReceiver imReceiver, Map<String, Set<String>> chatCache, Map<String, Cryptor> cryptorCache) {
        this.imReceiver = imReceiver;
        this.chatCache = chatCache;
        this.cryptorCache = cryptorCache;
    }

    public CompletableFuture<Boolean> start(InetSocketAddress address) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        Bootstrap bootstrap = new Bootstrap();
        HimClientHandler handler = new HimClientHandler();
        handler.setImReceiver(imReceiver);

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new HimDecoder());
                        channel.pipeline().addLast(new HimEncoder());
                        channel.pipeline().addLast(handler);
                    }
                });

        ChannelFuture f = null;
        try {
            f = bootstrap.connect(address).sync();
            if (f.isSuccess()) {
                this.channel = f.channel();

            }
            future.complete(f.isSuccess());
        } catch (InterruptedException e) {
            e.printStackTrace();
            future.complete(false);
        }
        return future;
    }

    public CompletableFuture<Boolean> restart(InetSocketAddress address){
        return start(address);
    }


    @Override
    public CompletableFuture<Boolean> send(Message message) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        channel.writeAndFlush(message).addListener(channel -> {
            if (channel.isSuccess()) future.complete(true);
            else future.complete(false);
        });
        return future;
    }

}
