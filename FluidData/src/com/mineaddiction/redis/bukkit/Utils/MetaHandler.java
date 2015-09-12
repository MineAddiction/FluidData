package com.mineaddiction.redis.bukkit.Utils;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import com.mineaddiction.redis.bukkit.BukkitMain;

public class MetaHandler {

	
	public static void setMeta(Player p, Object data, String key){
		BukkitMain plugin = BukkitMain.instance;
		p.setMetadata(key, new FixedMetadataValue(plugin, data));
	}

	
	public static Object getMetadata(Player p, String key) {
		List<MetadataValue> values;

		try{
			values = p.getMetadata(key);  
			for (MetadataValue value : values) {
				return value.value();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
