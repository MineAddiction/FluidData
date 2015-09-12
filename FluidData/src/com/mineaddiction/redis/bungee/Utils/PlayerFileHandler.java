package com.mineaddiction.redis.bungee.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

public class PlayerFileHandler {

	public static String getJSONFromFile(String uuid) throws IOException{
		String path ="/usr/games/minecraft/servers/playerData/" + uuid + ".json";
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, Charset.defaultCharset());
	}

	public static String data2JSON(String uuid){
		PlayerData data = new PlayerData(uuid);
		Gson gson = new Gson();
		String json = gson.toJson(data);

		write2File(uuid, json);
		
		return json;
	}
	
	
	// write to file when player joins and leaves
	// json data comes from the redis
	public static void write2File(String uuid, String json){
		
		try {
			PrintWriter writer;
			writer = new PrintWriter("/usr/games/minecraft/servers/playerData/" + uuid + ".json", "UTF-8");
			writer.write(json);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
}
