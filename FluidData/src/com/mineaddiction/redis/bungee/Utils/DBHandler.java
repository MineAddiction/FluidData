package com.mineaddiction.redis.bungee.Utils;

import husky.mysql.MySQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.md_5.bungee.api.plugin.Plugin;

import com.mineaddiction.redis.bungee.BungeeMain;

public class DBHandler {
	static MySQL mySQL;
	static Connection c;
	static Plugin plugin;
	static DBHandler instance;
	
	public DBHandler (){
		instance = this;
		mySQL = new MySQL("localhost", "3306", "Core", "core", "YellowZebra6");
		c = mySQL.openConnection();
		plugin = BungeeMain.instance;
	}
	
	public static DBHandler getInstance(){
		return instance;
	}
	
	public String getData(String uuid) {
        Statement statement = null;
        ResultSet resultSet = null;
        String data = null;
        
        try {
            statement = c.createStatement();
            resultSet = statement.executeQuery("SELECT  * FROM " + "Test" + " WHERE uuid='" + uuid.toString() + "'");
 
            while (resultSet.next()) {
                data = resultSet.getString("data1");
            }
        } catch (Exception e) {
            plugin.getLogger().warning("SQL error getting info.");
            plugin.getLogger().warning("ERROR: " + e);
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                	plugin.getLogger().warning("ERROR: " + ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                	plugin.getLogger().warning("ERROR: " + ex);
                }
            }
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    plugin.getLogger().warning("ERROR: " + ex);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                	plugin.getLogger().warning("ERROR: " + ex);
                }
            }
        }
		return data;
    }
}
