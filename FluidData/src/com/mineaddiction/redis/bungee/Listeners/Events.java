package com.mineaddiction.redis.bungee.Listeners;

import java.io.IOException;

import com.mineaddiction.redis.bungee.BungeeMain;
import com.mineaddiction.redis.bungee.Utils.DataHandler;
import com.mineaddiction.redis.bungee.Utils.PlayerFileHandler;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {

	@EventHandler
	public void onPostLogin(PostLoginEvent event) throws IOException {
		ProxiedPlayer player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		//String server = player.getServer().getInfo().getName();
		//String data = DBHandler.getInstance().getData(uuid);
		//DataHandler.setData(uuid, "hello me");
		//BungeeMain.instance.getLogger().severe(uuid + "hello me");
		String data = PlayerFileHandler.data2JSON(uuid);
		//BungeeMain.instance.getLogger().info("0" + ": " + data);
		DataHandler.setData(uuid, data);
	}
	
	@EventHandler
	public void onDisconnect(ServerDisconnectEvent event) {
		BungeeMain.instance.getLogger().severe("ServerDisconnectEvent " + System.nanoTime());
		ChannelListener.sendToBukkit("BungeeCord", "left a server", getServer(event.getPlayer()), event.getPlayer().getUniqueId().toString());
		System.out.print("left server " + getServer(event.getPlayer()).getMotd());
	}
	
	@EventHandler
	public void onConnect(ServerConnectEvent event) {
		BungeeMain.instance.getLogger().severe("ServerConnectEvent " + System.nanoTime());
	}

	@EventHandler
	public void onConnect(PlayerHandshakeEvent event) {
		BungeeMain.instance.getLogger().severe("PlayerHandshakeEvent " + System.nanoTime());
	}
	
	@EventHandler
	public void onConnect(PreLoginEvent event) {
		BungeeMain.instance.getLogger().severe("PreLoginEvent " + System.nanoTime());
	}
	
	@EventHandler
	public void onQuit(PlayerDisconnectEvent event){
		ProxiedPlayer player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		// pulls player data from redis
		// saves to flat file
		DataHandler.getData(uuid, DataHandler.callback);
		
		// remove data from redis
		//DataHandler.clearData(uuid);
	}
	
	public ServerInfo getServer(ProxiedPlayer p){
		return p.getServer().getInfo();
	}
}
