package com.yxt.bd.crud;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: hebj
 * @Date: 2020/2/14 15:40
 */
public class SessionCreateDemo implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {

        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 2000, new SessionCreateDemo());
        System.out.println("1 zookeeper state: " + zooKeeper.getState());
        latch.await();
        System.out.println("2 zookeeper state: " + zooKeeper.getState());
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("received watch event: " + watchedEvent);
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }

    }
}
