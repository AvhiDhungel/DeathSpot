package com.github.AvhiDhu;

import com.github.AvhiDh.SqlUtilities.*;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.time.LocalDateTime;

public class EventListeners implements Listener {

    private SqlDatabaseConnection conn;

    public EventListeners(SqlDatabaseConnection conn) {
        this.conn = conn;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player pl = e.getEntity().getPlayer();
        Location l = pl.getLocation();

        SqlQueryBuilder sql = new SqlQueryBuilder();
        sql.appendLine("INSERT INTO av_tblDeathSpot ");
        sql.appendLine("(fldPlayerId, fldXPos, fldYPos, fldZPos, fldWorld, fldDate) ");
        sql.appendLine("VALUES('%s','%s','%s','%s','%s','%s')",
                pl.getUniqueId(),
                (int) l.getX(),
                (int) l.getY(),
                (int) l.getZ(),
                l.getWorld().getName(),
                LocalDateTime.now().toString()
                );

        conn.executeNonQuery(sql);
    }

}
