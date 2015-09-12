package com.mineaddiction.redis.bukkit.Utils;

import org.bukkit.entity.Player;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;
import com.mineaddiction.redis.Utils.PlayerData;
import com.mineaddiction.redis.bukkit.BukkitMain;

public class DataHandler {

	//post data
	public static void setData(String uuid, final String data){
		BukkitMain.instance.getLogger().info("Data sent to Jedis 0");
		
		BukkitMain.instance.getLogger().info("Data sent to Jedis 1");
		Jedis jedis = BukkitMain.instance.getPool().getResource();
		try {
			jedis.set(uuid, data);
			BukkitMain.instance.getLogger().info("Data sent to Jedis 2");
		} catch (Exception e) {
			BukkitMain.instance.getPool().returnBrokenResource(jedis);
		}
		BukkitMain.instance.getPool().returnResource(jedis);
	}


	//post data
	public static void getData(Player p, final Callback<Object[]> callback){

		Jedis jedis = BukkitMain.instance.getPool().getResource();
		try {
			callback.execute(new Object[]{p, jedis.get(p.getUniqueId().toString())});
		} catch (Exception e) {
			BukkitMain.instance.getPool().returnBrokenResource(jedis);
		}
		BukkitMain.instance.getPool().returnResource(jedis);
	}

	public interface Callback<T>
	{
		public void execute(T response);
	}

	public static Callback<Object[]> callback = new Callback<Object[]>(){
		public void execute(Object[] data)
		{
			Gson gson = new Gson();
			Player p = (Player)data[0];
			String json = (String)data[1];
			
			PlayerData pd = gson.fromJson(json, PlayerData.class); 
			
			//set metadata
			int coins = pd.getCoins() + 1;
			MetaHandler.setMeta(p, coins, "Coins");
			
			//show coins
			p.sendMessage("Redis data: " + MetaHandler.getMetadata(p, "Coins"));
		}
	};
}
