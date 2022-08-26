package com.github.AvhiDhu;


import com.github.AvhiDh.SqlUtilities.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

public class Commands implements CommandExecutor {

    private Runnable reloadConfig;
    private SqlDatabaseConnection conn;

    private static String prefix = String.format("%s[%sDeathSpot%s] %s",
            ChatColor.GRAY, ChatColor.RED, ChatColor.GRAY, ChatColor.GREEN);

    public Commands(Runnable reloadConfig, SqlDatabaseConnection conn) {
        this.reloadConfig = reloadConfig;
        this.conn = conn;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            SendMessage(sender, ChatColor.DARK_GRAY + "Usage: " + ChatColor.GRAY + "/ds <playername>");
        } else {

            String arg = args[0];

            if (arg.equalsIgnoreCase("reload")) {
                reloadConfig.run();
                SendMessage(sender, ChatColor.GREEN + "Config Reloaded!");

            } else if (arg.equalsIgnoreCase("help")) {
                SendMessage(sender, ChatColor.DARK_GRAY + "Usage: " + ChatColor.GRAY + "/ds <playername>");

            } else {

                Player pl = Bukkit.getPlayer(arg);

                if (pl == null) {
                    SendMessage(sender, ChatColor.RED + "No player found by the name of " + ChatColor.YELLOW + arg);
                }
                else {

                    SqlQueryBuilder sql = new SqlQueryBuilder();
                    sql.appendLine("SELECT * FROM av_tblDeathSpot ");
                    sql.appendLine("WHERE fldPlayerId = '%s' ", pl.getUniqueId().toString());
                    sql.appendLine("ORDER BY fldDate DESC ");
                    sql.appendLine("LIMIT 1");

                    Integer xPos, yPos, zPos;
                    String world, msg;
                    Date d;

                    SqlDataReader dr = conn.execute(sql);

                    if (dr.read()) {
                        xPos = dr.getInteger("fldXPos");
                        yPos = dr.getInteger("fldYPos");
                        zPos = dr.getInteger("fldZPos");
                        world = dr.getString("fldWorld");
                        d = dr.getDate("fldDate");

                        msg = String.format("%s%s %slast died in %s%s %s %s %s %son %s%s",
                                ChatColor.YELLOW, pl.getName(), ChatColor.GREEN,
                                ChatColor.YELLOW, xPos, yPos, zPos, world,
                                ChatColor.GREEN, ChatColor.YELLOW, d.toString());

                    } else {

                        msg = "No previous death location found for " + ChatColor.YELLOW + pl.getName();
                    }

                    SendMessage(sender, msg);
                }
            }

        }
        return true;
    }

    private void SendMessage(CommandSender s, String msg) {
        s.sendMessage(prefix + msg);
    }
}
