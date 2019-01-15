package com.hanson.im.client.network.tcp;

import com.hanson.im.client.ClientConfig;
import com.hanson.im.client.handler.*;
import com.hanson.im.common.decode.HimDecoder;
import com.hanson.im.common.encode.HimEncoder;
import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageBody;
import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.MessageType;
import com.hanson.im.common.protocol.body.LoginRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;


/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
@Slf4j
public class Connector implements IMSender,IMReplier{

    /**
     * the server port to connect
     */
    private int port;

    /**
     * the server address
     */
    private String ip;

    /**
     * nio event loop grouop
     */
    private EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    public Connector setAddress(String ip,int port){
        this.ip = ip;
        this.port = port;
        return this;
    }

    public CompletableFuture<Boolean> start(){
        CompletableFuture<Boolean> future = new CompletableFuture<>();


        Bootstrap bootstrap = new Bootstrap();

        HimClientHandler handler = new HimClientHandler();

        IMReceiver imReceiver = new MessageAcceptor();

        handler.setImReceiver(imReceiver);

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000)
                .handler(new ChannelInitializer<Channel>(){
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new HimDecoder());
                        channel.pipeline().addLast(new HimEncoder());
                        channel.pipeline().addLast(handler);
                    }
                });

        ChannelFuture f = null;
        try {
            f = bootstrap.connect(ip,port).sync();
            if(f.isSuccess()){
                this.channel = f.channel();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        future.complete(f.isSuccess());
        return future;
    }
    public ChannelFuture login(LoginRequest loginRequest){
        Message message = new Message();

        MessageHeader header = new MessageHeader();
        header.setVersion(ClientConfig.version);
        header.setFrom(loginRequest.getUserId());
        header.setToList(null);
        header.setMessageType(MessageType.LOGIN_TO);

        message.setHeader(header);

        MessageBody body = new MessageBody();
        body.setData(loginRequest,LoginRequest.class);

        message.setBody(body);

        ChannelFuture future = channel.writeAndFlush(message);
        return future;
    }

    @Override
    public CompletableFuture<Void> send(Message message) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        channel.writeAndFlush(message).addListener(channel->future.complete(null));
        return future;
    }

    @Override
    public CompletableFuture reply(Message message) {
        return send(message);
    }
}
