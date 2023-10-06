package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//生产者消费者模型
public class BotPool extends Thread{
    private final ReentrantLock lock = new ReentrantLock();
    private  Condition condition = lock.newCondition();
    private Queue<Bot> bots = new LinkedList<>();

    public  void addBot(Integer userId,String botCode,String input){
        lock.lock();
        try{
            bots.add(new Bot(userId,botCode,input));
            //消息队列里面有任务了 唤醒线程
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }


    //每次执行都搞一个新的线程 使用join操作来限制线程执行时间
    private void consume(Bot bot){
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000,bot);
    }

    @Override
    public void run() {
        while(true){
            lock.lock();
            if(bots.isEmpty()){
                //发现列表是空的 阻塞当前进程 释放锁
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            } else {
                Bot bot = bots.remove();
                lock.unlock();
                //放在解锁后面 因为这个函数执行时间很长 会影响加入队列和删除队列操作
                consume(bot);
            }
        }
    }
}
