package com.yxt.bd.crud;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: hebj
 * @Date: 2020/2/14 21:05
 */
public class ZNodeInsertDemo implements Watcher {

    private static CountDownLatch latch =  new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = "localhost:2181";
        ZooKeeper zooKeeper1 = new ZooKeeper(host, 2000, new SessionIdandPasswordDemo());
        latch.await();


    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println("event: " + watchedEvent.getState());
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }

    }
}
