package com.hanson.im.server.server.handler;


import com.alibaba.fastjson.JSONObject;
import com.hanson.im.common.protocol.Message;
import com.hanson.im.common.protocol.MessageBody;
import com.hanson.im.common.protocol.MessageHeader;
import com.hanson.im.common.protocol.MessageType;
import com.hanson.im.common.protocol.body.LoginRequest;
import com.hanson.im.server.model.User;
import com.hanson.im.server.service.MessageHandler;
import com.hanson.im.server.service.UserCache;
import com.hanson.im.server.service.UserService;
import com.hanson.im.server.service.UserVailidator;
import com.hanson.im.server.util.MessageBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hanson
 * @Date 2019/1/12
 * @Description:
 */
@Slf4j
@Service
public class MessageHandlerImpl implements MessageHandler {


    @Autowired
    private UserCache userCache;

    @Autowired
    private UserVailidator userVailidator;

    @Autowired
    private UserService userService;


    private Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    private Map<ChannelId,String> channelIdMap = new ConcurrentHashMap<>();

    @Override
    public void handleMessage(Channel channel, Message message) {
        MessageHeader header = message.getHeader();
        MessageBody body = message.getBody();
        MessageType messageType = header.getMessageType();

        log.info("receive message :{}", JSONObject.toJSONString(message));
        switch (messageType) {
            case LOGIN_TO:
                if (!(body.getData() instanceof LoginRequest)) {
                    Message backMessage = MessageBuilder.buildSystemMessage(404, "the login information is error");
                    channel.writeAndFlush(backMessage).addListener(ch -> channel.close());
                    return;
                }
                LoginRequest loginRequest = (LoginRequest) body.getData();
                if (userCache.userIsOnline(loginRequest.getUserId())) {
                    Message backMessage = MessageBuilder.buildSystemMessage(404, "user is already online,login repeat");
                    channel.writeAndFlush(backMessage).addListener(ch -> channel.close());
                    return;
                } else
                if (!userVailidator.validateLogin(loginRequest)) {
                    Message backMessage = MessageBuilder.buildSystemMessage(404, "the login information is error");
                    channel.writeAndFlush(backMessage).addListener(ch -> channel.close());
                    log.info("validate user failed,userId:{},userName:{}", loginRequest.getUserId(), loginRequest.getUserName());
                    return;
                }
                User user = userService.getUserById(loginRequest.getUserId());
                channel.writeAndFlush(MessageBuilder.buildSystemMessage(200, "login success"))
                        .addListener(ch -> {
                            if (ch.isSuccess()) {
                                channelMap.put(loginRequest.getUserId(), channel);
                                channelIdMap.put(channel.id(),loginRequest.getUserId());
                                userCache.setOnline(user);
                                log.info("userId:{},userName:{} login success", user.getUserId(), user.getUserName());
                            } else {
                                log.info("userId:{},userName:{} login failed,data send error:{}", user.getUserId(), user.getUserName(), ch.cause());
                            }

                        });
                break;
            case KEY_EXCHANGE_TO:
            case KEY_EXCHANGE_BACK:
            case ENCRYPT_TEXT_MSG:
                List<String> toList = header.getToList();
                toList.forEach(userId -> {
                    Channel toChannel = channelMap.get(userId);
                    if (toChannel != null) {
                        toChannel.writeAndFlush(message).addListener(resultCh -> {
                            if (!resultCh.isSuccess()) {
                                log.error("failed to send message to:{}", userId);
                            }
                        });
                    } else {
                        log.info("can't send message to userId:{},there is no channel", userId);
                    }

                });
                break;
        }
    }

    @Override
    public void userOffline(ChannelId channelId) {
        String userId = channelIdMap.remove(channelId);

        if(userId != null){
            channelMap.remove(userId);
            userCache.setOffLineUser(userId);
        }
    }
}
