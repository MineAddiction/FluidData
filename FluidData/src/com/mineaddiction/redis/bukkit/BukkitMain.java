package com.mineaddiction.redis.bukkit;


import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.mineaddiction.redis.bukkit.Listeners.MessageListener;
import com.mineaddiction.redis.bukkit.Listeners.PlayerListener;
import com.mineaddiction.redis.bukkit.Utils.EESubscriber;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class BukkitMain extends JavaPlugin{

	public static BukkitMain instance;
	private EESubscriber eeSubscriber;;
	public JedisPool pool;
	private final String BUKKIT_CHANNEL = "BKC";
	private final String BUNGEE_CHANNEL = "BUC";
	public PluginManager pluginManager;
	String TAG = "FluidData";

	public void onEnable() {

		getLogger().info(TAG + " starting");

		instance = this;
		saveDefaultConfig();
		String ip = getConfig().getString("ip");
		int port = getConfig().getInt("port");
		String password = getConfig().getString("password");
		if (password == null || password.equals(""))
			pool = new JedisPool(new JedisPoolConfig(), ip, port, 0);
		if(pool != null){
			getLogger().info(TAG + " connected to Jedis");
		}
		else
			pool = new JedisPool(new JedisPoolConfig(), ip, port, 0, password);
		new BukkitRunnable() {
			@Override
			public void run() {
				eeSubscriber = new EESubscriber();
				Jedis jedis = pool.getResource();
				try {
					jedis.subscribe(eeSubscriber, BUKKIT_CHANNEL);
					getLogger().info(TAG + " jedis call");
				} catch (Exception e) {
					e.printStackTrace();
					pool.returnBrokenResource(jedis);
					getLogger().severe("Unable to connect to Redis server.");
					return;
				}
				pool.returnResource(jedis);
			}
		}.runTaskAsynchronously(this);

		//register listeners
		pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new PlayerListener(), this);

		//messages
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
	}

	public void onDisable() {
		eeSubscriber.unsubscribe();
		pool.destroy();
	}

	public JedisPool getPool(){
		return pool;
	}
}
