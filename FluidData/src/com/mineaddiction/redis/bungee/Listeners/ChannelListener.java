package com.mineaddiction.redis.bungee.Listeners;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.mineaddiction.redis.bungee.BungeeMain;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChannelListener implements Listener {

	static BungeeMain plugin;
	
	public ChannelListener(){
		this.plugin = BungeeMain.instance;
	}
	
	public static void onPluginMessage(PluginMessageEvent e) {
        if (e.getTag().equalsIgnoreCase("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            try {
                String channel = in.readUTF(); // channel we delivered
                if(channel.equals("get")){
                	
                    ServerInfo server = plugin.getProxy().getInstance().getPlayer(e.getReceiver().toString()).getServer().getInfo();
                    String input = in.readUTF(); // the inputstring
                    /*
                    if(input.equals("nickname")){
                        sendToBukkit(channel, Main.nicks.get(e.getReceiver().toString()), server);
                    } else {
                        sendToBukkit(channel, Main.points.get(e.getReceiver().toString()).toString(), server);
                    }
                    */
              
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void sendToBukkit(String channel, String message, ServerInfo server, String uuid) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(uuid);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("send to server");
        server.sendData(channel, stream.toByteArray());

    }

}
