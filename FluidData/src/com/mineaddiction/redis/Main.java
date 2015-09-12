package com.mineaddiction.redis;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	public Main instance;
	
	public void onEnable(){
		instance = this;
	}
	
	public void onDisable(){
		
	}
	
	public Main getInstance(){
		return instance;
	}
}
