package com.yxt.bd.crud;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: hebj
 * @Date: 2020/2/15 17:08
 */
public class ZNodeGetDemo implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);
    private static ZooKeeper client = null;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String host = "localhost:2181";
        String path = "/zk";
        String path1 = "/c1";
        String path2 = "/c2";
        client = new ZooKeeper(host, 5000, new ZNodeGetDemo());
        latch.await();
//        client.create(path, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        client.create(path + path1, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        client.getChildren(path, true, new IChildren2Callback(), "async msg");
        client.create(path + path2, "456".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Thread.sleep(500000);
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                System.out.println("zookeeper client is created successfully!");
                latch.countDown();
            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    List<String> children = client.getChildren(watchedEvent.getPath(), true);
                    System.out.println("path:" + watchedEvent.getPath() + ", get child:" + children);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class IChildren2Callback implements AsyncCallback.Children2Callback {
        public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
            System.out.println("get children znode,res code:" + i + ", path:" + s + ", ctx:" + o
                    + ", children list:" + list + ",stat:" + stat);
        }
    }

}