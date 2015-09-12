package com.mineaddiction.redis.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Data;

import org.bukkit.entity.Player;

import com.mineaddiction.redis.bukkit.Utils.MetaHandler;
import com.mineaddiction.redis.bungee.BungeeMain;

public @Data class PlayerData {

	private String uuid;
	private int coins;

	//used in bukkit
	public PlayerData(Player p) {
		this.uuid = p.getUniqueId().toString();
		this.coins = (int) MetaHandler.getMetadata(p, "Coins");
	}
	
	
	//used in bungee
	public PlayerData(String uuid) {

		//get player data from db
		try {
			
			Statement statement = BungeeMain.instance.c.createStatement();
			ResultSet rs = statement.executeQuery("SELECT Coins FROM sgr_player WHERE uuid = '" + uuid + "' limit 1");
			
			if(rs.next()){
				System.out.print(uuid + ": " + rs.getInt("Coins"));
				this.coins = rs.getInt("Coins");
			}else{
				this.coins = 0;
			}
			
			statement.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.uuid = uuid;
	}
}
