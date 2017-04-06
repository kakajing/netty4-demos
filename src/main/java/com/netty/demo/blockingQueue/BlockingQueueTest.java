package com.netty.demo.blockingQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author 卡卡
 * Created by jing on 2017/4/6.
 */
public class BlockingQueueTest {

    public static void main(String[] args) throws InterruptedException {

        // 声明一个容量为10的缓存队列
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

        Producer p1 = new Producer(queue);
        Producer p2 = new Producer(queue);
        Producer p3 = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        //借助Excutors
        ExecutorService service = Executors.newCachedThreadPool();

        //启动线程
        service.execute(p1);
        service.execute(p2);
        service.execute(p3);
        service.execute(consumer);

        //执行10s
//        Thread.sleep(10 * 1000);
//        p1.stop();
//        p2.stop();
//        p3.stop();

        Thread.sleep(2000);
        service.shutdown();
    }
}
