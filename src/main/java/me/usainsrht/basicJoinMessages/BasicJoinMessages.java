package me.usainsrht.basicJoinMessages;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public final class BasicJoinMessages extends JavaPlugin {

    private BasicJoinMessages instance;
    private boolean isPaper;
    private boolean hasPapi;

    @Override
    public void onEnable() {
        instance = this;

        try {
            Class.forName("com.destroystokyo.paper.MaterialSetTag");
            isPaper = true;
        } catch (Exception | Error e) {
            isPaper = false;
        }

        hasPapi = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");

        loadConfig();

        getCommand("basicjoinmessages").setExecutor((sender, cmd, label, args) -> {
            if (cmd.getPermission() != null && sender.hasPermission(cmd.getPermission())) {
                reload();
                sender.sendMessage("reloaded");
            }
            return true;
        });

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
    }

    @Override
    public void onDisable() {

    }

    public void loadConfig() {
        saveDefaultConfig();
    }

    public void reload() {
        reloadConfig();

        loadConfig();
    }

    public BasicJoinMessages getInstance() {
        return instance;
    }

    public boolean isPaper() {
        return isPaper;
    }

    public boolean hasPapi() {
        return hasPapi;
    }

    public String getJoinMessage(Player player) {
        ConfigurationSection groups = getConfig().getConfigurationSection("groups");
        for (String groupName : groups.getKeys(false)) {
            ConfigurationSection group = groups.getConfigurationSection(groupName);
            if (player.hasPermission(group.getString("permission"))) {
                List<String> list = group.getStringList("join");
                if (list.isEmpty()) return "";
                return list.get(new Random().nextInt(list.size()));
            }
        }

        ConfigurationSection group = getConfig().getConfigurationSection("default");
        List<String> list = group.getStringList("join");
        if (list.isEmpty()) return "";
        return list.get(new Random().nextInt(list.size()));
    }

    public String getQuitMessage(Player player) {
        ConfigurationSection groups = getConfig().getConfigurationSection("groups");
        for (String groupName : groups.getKeys(false)) {
            ConfigurationSection group = groups.getConfigurationSection(groupName);
            if (player.hasPermission(group.getString("permission"))) {
                List<String> list = group.getStringList("quit");
                if (list.isEmpty()) return "";
                return list.get(new Random().nextInt(list.size()));
            }
        }

        ConfigurationSection group = getConfig().getConfigurationSection("default");
        List<String> list = group.getStringList("quit");
        if (list.isEmpty()) return "";
        return list.get(new Random().nextInt(list.size()));
    }



}
