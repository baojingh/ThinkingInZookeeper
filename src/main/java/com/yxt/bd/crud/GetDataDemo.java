package com.yxt.bd.crud;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: hebj
 * @Date: 2020/2/16 20:55
 */
public class GetDataDemo implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper client = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String host = "localhost:2181";
        String path = "/zk";
        String path1 = path + "/c1";
        client = new ZooKeeper(host, 5000, new GetDataDemo());
        latch.await();
        client.create(path1, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        byte[] data = client.getData(path1, true, stat);
        System.out.println(new String(data));
        System.out.println(stat.getVersion() + " " + stat.getCzxid() + " " + stat.getMzxid());

        client.setData(path1, "456".getBytes(), -1);
        Thread.sleep(5000);
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                System.out.println("zookeeper client is created successfully!");
                latch.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                try {
                    byte[] data = client.getData(watchedEvent.getPath(), true, stat);
                    System.out.println("cb: " + new String(data));
                    System.out.println("cb:" + stat.getVersion() + " " + stat.getCzxid() + " " + stat.getMzxid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
