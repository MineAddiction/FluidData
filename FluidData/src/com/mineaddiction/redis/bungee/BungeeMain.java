package com.mineaddiction.redis.bungee;

import husky.mysql.MySQL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;

import com.google.common.io.ByteStreams;
import com.mineaddiction.redis.bungee.Listeners.ChannelListener;
import com.mineaddiction.redis.bungee.Listeners.Events;
import com.mineaddiction.redis.bungee.Utils.EESubscriber;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeMain extends Plugin{
	
	private JedisPool pool;
	private final String BUNGEE_CHANNEL = "BUC";
	public static BungeeMain instance;
	public PluginManager pm;
	public MySQL mysql;
	public Connection c;

	Configuration config;
	EESubscriber eeSubscriber;

	@Override
	public void onEnable() {
		instance = this;
		
		//tester db data
		mysql = new MySQL("localhost", "3306", "Core", "core", "YellowZebra6");
		c = mysql.openConnection();
		
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(
					loadResource(this, "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final String ip = config.getString("ip");
		final int port = config.getInt("port");
		final String password = config.getString("password");
		getProxy().getScheduler().runAsync(this, new Runnable() {
			@Override
			public void run() {
				
				if (password == null || password.equals("")){
					pool = new JedisPool(new JedisPoolConfig(), ip, port, 0);
					getLogger().severe("connected to Redis server.");
				}else{
					pool = new JedisPool(new JedisPoolConfig(), ip, port, 0, password);
					getLogger().severe("connected to Redis server.");
				}
				
				Jedis jedis = pool.getResource();
				try {
					eeSubscriber = new EESubscriber();
					jedis.subscribe(eeSubscriber, BUNGEE_CHANNEL);
				} catch (Exception e) {
					e.printStackTrace();
					pool.returnBrokenResource(jedis);
					getLogger().severe("Unable to connect to Redis server.");
					return;
				}
				pool.returnResource(jedis);            }
		});
		
		//listeners
		pm = getProxy().getPluginManager();
		pm.registerListener(this, new Events());
		
		pm.registerListener(this, new ChannelListener());

        getProxy().getInstance().registerChannel("Return");
	}

	@Override
	public void onDisable() {
		eeSubscriber.unsubscribe();
		pool.destroy();
	}

	public static File loadResource(Plugin plugin, String resource) {
		File folder = plugin.getDataFolder();
		if (!folder.exists())
			folder.mkdir();
		File resourceFile = new File(folder, resource);
		try {
			if (!resourceFile.exists()) {
				resourceFile.createNewFile();
				try (InputStream in = plugin.getResourceAsStream(resource);
						OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resourceFile;
	}
	
	public JedisPool getPool(){
		return pool;
	}
}
