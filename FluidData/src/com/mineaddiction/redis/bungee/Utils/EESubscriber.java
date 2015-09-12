package com.mineaddiction.redis.bungee.Utils;

import com.mineaddiction.redis.bungee.BungeeMain;

import net.md_5.bungee.api.ProxyServer;
import redis.clients.jedis.JedisPubSub;

public class EESubscriber extends JedisPubSub {
    @Override
    public void onMessage(String channel, final String msg) {
        BungeeMain.instance.getLogger().info("Dispatching /" + msg);
        ProxyServer ps = ProxyServer.getInstance();
        ps.getPluginManager().dispatchCommand(ps.getConsole(), msg);
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
