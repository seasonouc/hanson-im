package com.hanson.im.server.server;

import com.hanson.im.server.server.initializer.HimServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
@Slf4j
@Component
public class HimServer {

    @Value("${config.server.port}")
    private int nettyPort;

    @Value("${config.netty.workers}")
    private int nettyWorks;

    EventLoopGroup boss;
    EventLoopGroup work;

    @Autowired
    HimServerInitializer himServerInitializer;


    public CompletableFuture<Boolean> start(){
        log.info("start network service");
         boss = new NioEventLoopGroup();
         work = new NioEventLoopGroup(nettyWorks);

         CompletableFuture<Boolean> future = new CompletableFuture();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss,work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(nettyPort))
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(himServerInitializer);
        try {
            ChannelFuture f = bootstrap.bind().sync();
            future.complete(f.isSuccess());
        } catch (InterruptedException e) {
            future.complete(false);
            future.completeExceptionally(e);
            log.error(e.getMessage());
        }
        return future;
    }


    public void shutdown(){
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }
}
