package com.hanson.im.client.util;

import java.util.ArrayDeque;

/**
 * @author hanson
 * @Date 2019/1/23
 * @Description:
 */
public class FiniteDeque<E> extends ArrayDeque<E>{
    int capacity;
    int count;

    public FiniteDeque(int capacity){
        super(capacity);
        count = 0;
        this.capacity = capacity;
    }

    @Override
    public boolean add(E e){
        if(count >= capacity){
            super.removeFirst();
            return super.add(e);
        }else{
            count ++;
            return super.add(e);
        }
    }



}
