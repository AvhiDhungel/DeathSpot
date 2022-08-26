package com.github.AvhiDhu;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> li = new ArrayList<String>();
        li.add("reload");
        li.add("help");

        for (Player p : Bukkit.getOnlinePlayers()) {
            li.add(p.getName());
        }

        return li;
    }
}
