package com.mineaddiction.redis.bukkit.Utils;


import org.bukkit.scheduler.BukkitRunnable;
import com.mineaddiction.redis.bukkit.BukkitMain;
import redis.clients.jedis.JedisPubSub;

public class EESubscriber extends JedisPubSub {
    @Override
    public void onMessage(String channel, final String msg) {
        // Needs to be done in the server thread
         new BukkitRunnable() {
            @Override
            public void run() {
                BukkitMain.instance.getLogger().info("Dispatching /" + msg);
                BukkitMain.instance.getServer().dispatchCommand(
                		BukkitMain.instance.getServer().getConsoleSender(), msg);
            }
        }.runTaskAsynchronously(BukkitMain.instance);
    }

    @Override
    public void onPMessage(String s, String s2, String s3) {
    }

    @Override
    public void onSubscribe(String s, int i) {
    }

    @Override
    public void onUnsubscribe(String s, int i) {
    }

    @Override
    public void onPUnsubscribe(String s, int i) {
    }

    @Override
    public void onPSubscribe(String s, int i) {
    }
}
