package com.mineaddiction.redis.bungee.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;
import com.mineaddiction.redis.bungee.BungeeMain;

import lombok.Data;

public @Data class PlayerData {


	private String uuid;
	private int coins;
	PlayerData instance;


	public PlayerData(String uuid) {

		instance = this;
		
		// check for file first
		String path = "/usr/games/minecraft/servers/playerData/" + uuid + ".json";
		File f = new File(path);

		if(f.exists()){
			Gson gson = new Gson();
			
			byte[] encoded;
			try {
				encoded = Files.readAllBytes(Paths.get(path));
				String json = new String(encoded, Charset.defaultCharset());
				instance = gson.fromJson(json, PlayerData.class);
				this.uuid = uuid;
				this.coins = instance.getCoins();
				instance = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else{

			//get player data from db
			try {

				Statement statement = BungeeMain.instance.c.createStatement();
				ResultSet rs = statement.executeQuery("SELECT coins FROM BetaTest WHERE uuid = '" + uuid + "' limit 1");

				if(rs.next()){
					System.out.print(uuid + ": " + rs.getInt("coins"));
					this.coins = rs.getInt("coins");
				}else{
					// inject player
					statement.execute("INSERT into BetaTest (uuid) VALUES ('" + uuid + "')");
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

}