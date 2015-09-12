package com.mineaddiction.redis.bukkit.Listeners;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.mineaddiction.redis.bukkit.BukkitMain;
import com.mineaddiction.redis.bukkit.Utils.DataHandler;

public class MessageListener implements PluginMessageListener {

	Plugin plugin;

	public MessageListener(){
		this.plugin = BukkitMain.instance;
	}

	@Override
	public synchronized void onPluginMessageReceived(String channel, final Player player, byte[] message) {
		final DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		System.out.print("bungee message");
		new BukkitRunnable() {
			@Override
			public void run() {

				try {
					String subchannel = in.readUTF();
					UUID uuid = UUID.fromString(subchannel);

					System.out.print(subchannel + " " + player.getUniqueId().toString());

					Player p = (Player) Bukkit.getPlayer(uuid);

					if(p.isOnline()){
						System.out.print("bungee message " + p.getUniqueId().toString());
						DataHandler.getData(p, DataHandler.callback);
						plugin.getLogger().severe("bungee connect " + System.nanoTime());
						//player.sendMessage(DataHandler.getData(player, DataHandler.callback) + "");
						/*
                String input = in.readUTF();
                obj.put(player, input);

                notifyAll();
						 */

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(plugin);
	}

	/*
	public synchronized Object get(Player p, boolean integer) {

        sendToBungeeCord(p, "get", integer ? "points" : "nickname");

        try {
            wait();
        } catch(InterruptedException e){}

        return obj.get(p);

    }*/

	public void sendToBungeeCord(Player p, String channel, String sub){
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF(channel);
			out.writeUTF(sub);
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
	}
}
