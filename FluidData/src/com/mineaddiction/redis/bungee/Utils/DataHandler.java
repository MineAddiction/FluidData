package com.mineaddiction.redis.bungee.Utils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.Gson;
import com.mineaddiction.redis.bukkit.BukkitMain;
import com.mineaddiction.redis.bungee.BungeeMain;

import redis.clients.jedis.Jedis;

public class DataHandler {

	//post data
	public static void setData(final String uuid, final String data){
		BungeeMain.instance.getProxy().getScheduler().runAsync(BungeeMain.instance, new Runnable() {
			@Override
			public void run() {
				Jedis jedis = BungeeMain.instance.getPool().getResource();
				try {
					jedis.set(uuid, data);
				} catch (Exception e) {
					BungeeMain.instance.getPool().returnBrokenResource(jedis);
				}
				BungeeMain.instance.getPool().returnResource(jedis);
			}
		});
	}


	//post data
	public static void getData(final String uuid, final Callback<String[]> callback){

		BungeeMain.instance.getProxy().getScheduler().runAsync(BungeeMain.instance, new Runnable() {
			@Override
			public void run() {
				Jedis jedis = BungeeMain.instance.getPool().getResource();
				try {
					
					callback.execute(new String[]{uuid, jedis.get(uuid)});
				} catch (Exception e) {
					BungeeMain.instance.getPool().returnBrokenResource(jedis);
				}
				BungeeMain.instance.getPool().returnResource(jedis);
			}
		});
	}

	public interface Callback<T>
	{
		public void execute(T response);
	}

	public static Callback<String[]> callback = new Callback<String[]>(){
		public void execute(String[] data)
		{
			Gson gson = new Gson();
			
			String uuid = (String)data[0];
			String json = (String)data[1];
			
			PlayerFileHandler.write2File(uuid, json);
		}
	};
	/*
	
	public static void clearData(final String uuid){
		
		BungeeMain.instance.getProxy().getScheduler().runAsync(BungeeMain.instance, new Runnable() {
			@Override
			public void run() {
				Jedis jedis = BungeeMain.instance.getPool().getResource();
				try {
					jedis.del(uuid);
				} catch (Exception e) {
					BungeeMain.instance.getPool().returnBrokenResource(jedis);
				}
				BungeeMain.instance.getPool().returnResource(jedis);
			}
		});
	}
	*/
}
