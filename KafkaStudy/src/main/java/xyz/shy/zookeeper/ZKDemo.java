package xyz.shy.zookeeper;
// import java classes

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

// import zookeeper classes

/**
 * Created by Shy on 2017/11/30
 */

public class ZKDemo {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zkConnect = connect("tagtic-master:2181");
        System.out.println(zkConnect);
        Stat exists = zkConnect.exists("/brokers", true);
        System.out.println(exists.getNumChildren());
    }

    // Method to connect zookeeper ensemble.
    public static ZooKeeper connect(String host) throws IOException, InterruptedException {
        final CountDownLatch connectedSignal = new CountDownLatch(1);
        ZooKeeper zoo = new ZooKeeper(host, 5000, new Watcher() {
            public void process(WatchedEvent we) {
                if (we.getState() == KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }
        });
        connectedSignal.await();
        return zoo;
    }
}
