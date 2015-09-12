package com.mineaddiction.redis.bukkit.Listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;
import com.mineaddiction.redis.Utils.PlayerData;
import com.mineaddiction.redis.bukkit.BukkitMain;
import com.mineaddiction.redis.bukkit.Utils.DataHandler;

public class PlayerListener implements Listener{

	Plugin plugin;

	public PlayerListener(){
		this.plugin = BukkitMain.instance;
	}

	@EventHandler
	void onPlayerJoin(PlayerJoinEvent event)
	{ 
		final Player p = event.getPlayer();
		p.sendMessage("joined server");
		DataHandler.getData(p, DataHandler.callback);

		plugin.getLogger().severe("connect " + System.nanoTime());
	}

	@EventHandler
	void onPlayerLeave(PlayerQuitEvent event)
	{ 
		Player p = event.getPlayer();
		PlayerData data = new PlayerData(p);

		Gson gson = new Gson();
		String json = gson.toJson(data);

		DataHandler.setData(p.getUniqueId().toString(), json);
		plugin.getLogger().severe("leave " + System.nanoTime());
	}
}
