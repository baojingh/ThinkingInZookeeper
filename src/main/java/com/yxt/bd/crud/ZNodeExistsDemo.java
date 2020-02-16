package com.yxt.bd.crud;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: hebj
 * @Date: 2020/2/16 23:03
 */
public class ZNodeExistsDemo implements Watcher {


    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper client = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        String host = "localhost:2181";
        String path = "/zk";
        String path1 = path + "/c1";
        client = new ZooKeeper(host, 5000, new ZNodeExistsDemo());
        latch.await();

        client.exists(path1, true);
        client.create(path1, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        client.setData(path1, "456".getBytes(), -1);
        client.delete(path1,-1);
        Thread.sleep(5000);
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
            try {
                if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                    latch.countDown();
                } else if (watchedEvent.getType() == Event.EventType.NodeCreated) {
                    System.out.println("node created " + watchedEvent.getPath());
                    client.exists(watchedEvent.getPath(), true);
                } else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                    client.exists(watchedEvent.getPath(), true);
                    System.out.println("node deleted " + watchedEvent.getPath());
                } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                    System.out.println("node changed " + watchedEvent.getPath());
                    client.exists(watchedEvent.getPath(), true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
