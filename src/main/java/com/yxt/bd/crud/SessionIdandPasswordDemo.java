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
public class SessionIdandPasswordDemo implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {

        String host = "localhost:2181";
        ZooKeeper zooKeeper1 = new ZooKeeper(host, 2000, new SessionIdandPasswordDemo());
        latch.await();
        long sessionId = zooKeeper1.getSessionId();
        byte[] sessionPasswd = zooKeeper1.getSessionPasswd();
//        ZooKeeper zooKeeper2 = new ZooKeeper(host, 2000, new SessionIdandPasswordDemo(), 1L, "1".getBytes());
        ZooKeeper zooKeeper3 = new ZooKeeper(host, 2000, new SessionIdandPasswordDemo(), sessionId, sessionPasswd);
        Thread.sleep(Integer.MAX_VALUE);
    }

    public void process(WatchedEvent watchedEvent) {
        System.out.println("received watch event: " + watchedEvent);
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }

    }
}
