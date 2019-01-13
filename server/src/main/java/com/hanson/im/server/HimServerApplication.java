package com.hanson.im.server;

import com.hanson.im.server.server.HimServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hanson
 * @Date 2019/1/11
 * @Description:
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.hanson.im.server.dao")
public class HimServerApplication implements CommandLineRunner {
    @Autowired
    HimServer himServer;

    public static void main(String args[]) {
        log.info("him-server is starting");
        SpringApplication.run(HimServerApplication.class, args);
        log.info("him-server started complete");
    }


    @Override
    public void run(String... strings) throws Exception {
        himServer.start()
                .whenComplete((result, error) -> {
            if (!result) {
                log.error("him server network listener start failed case:{}",error);
            }else{
                log.info("him-server network listener started success");
            }
        });
    }
}
