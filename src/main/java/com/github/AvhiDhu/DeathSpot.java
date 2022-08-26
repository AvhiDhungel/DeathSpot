package dhu.avhi.deathspot;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathSpot extends JavaPlugin {

    FileConfiguration config = getConfig();
    private SqlDatabaseConnection conn;

    @Override
    public void onEnable() {
        setupConfig();
        setupDB();
        setupListeners();
        setupCommands();
    }

    @Override
    public void onDisable() {
        conn.close();
    }

    public void setupConfig() {
        config.addDefault("sql.url", "127.0.0.1:3306");
        config.addDefault("sql.database", "mc_local");
        config.addDefault("sql.user", "avhi");
        config.addDefault("sql.password", "medhavi");

        config.options().copyDefaults(true);
        saveConfig();
    }

    public void setupDB() {
        String url = config.getString("sql.url");
        String db = config.getString("sql.database");
        String user = config.getString("sql.user");
        String password = config.getString("sql.password");

        conn = new SqlDatabaseConnection(url, db, user, password);
        InitializeDB.execute(conn);
    }

    public void setupListeners() {
        EventListeners l = new EventListeners(conn);
        getServer().getPluginManager().registerEvents(l, this);
    }

    public void setupCommands() {
        Runnable reloadConfig = new Runnable() {
            @Override
            public void run() {config = getConfig();}
        };

        this.getCommand("ds").setExecutor(new Commands(reloadConfig, conn));
        this.getCommand("deathspot").setExecutor(new Commands(reloadConfig, conn));
    }




}
