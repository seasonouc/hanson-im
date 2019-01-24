package com.hanson.im.client.logic;

import com.hanson.im.client.ui.base.ShowMessage;
import com.hanson.im.client.util.FiniteDeque;
import com.hanson.im.common.protocol.Message;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hanson
 * @Date 2019/1/23
 * @Description:
 */
public class MessageCache {

    public static MessageCache messageCache = new MessageCache();

    Map<String,Deque<ShowMessage>> map = new HashMap<>();

    public static MessageCache getMessageCache(){
        return messageCache;
    }

    public Deque<ShowMessage> getMessageQueue(String sessionId){
        Deque<ShowMessage> queue = map.get(sessionId);
        if(queue == null){
            queue = new FiniteDeque<>(50);
            map.put(sessionId,queue);
        }
        return queue;
    }

    public void addMeesage(String sessionId,ShowMessage message){
        Deque<ShowMessage> queue = map.get(sessionId);
        if(queue == null){
            queue = new FiniteDeque<>(50);
            map.put(sessionId,queue);
        }
        queue.addLast(message);
    }
}
