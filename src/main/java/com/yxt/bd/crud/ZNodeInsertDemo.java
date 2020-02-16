package com.yxt.bd.crud;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: hebj
 * @Date: 2020/2/14 21:05
 */
public class ZNodeInsertDemo implements Watcher {

    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String host = "localhost:2181";
        String path1 = "/zk_test_1";
        String path2 = "/zk_test_1_";

        ZooKeeper client = new ZooKeeper(host, 2000, new ZNodeInsertDemo());
        latch.await();

//        client.create(path1, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//        client.create(path2, "456".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        client.create(path1, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new IStringCallback(),
                "I am context1");


        client.create(path2, "456".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL, new IStringCallback(),
                "I am context2");

        Thread.sleep(5000);

    }


    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }

    }
}

class IStringCallback implements AsyncCallback.StringCallback {
    public void processResult(int i, String s, Object o, String s1) {
        System.out.println(i + "*" + s + "*" + o + "*" + s1);

    }
}
