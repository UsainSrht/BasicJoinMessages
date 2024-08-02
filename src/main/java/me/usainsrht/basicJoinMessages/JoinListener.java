package me.usainsrht.basicJoinMessages;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JoinListener implements Listener {

    BasicJoinMessages plugin;

    public JoinListener(BasicJoinMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String message = plugin.getJoinMessage(player);


        message = message.replace("%player%", player.getName());
        if (plugin.hasPapi()) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }


        if (plugin.isPaper()) {
            try {
                Component component = message.isEmpty() ? null : MiniMessage.miniMessage().deserialize(message);
                Method method = e.getClass().getDeclaredMethod("joinMessage", Component.class);
                method.setAccessible(true);
                method.invoke(e, component);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
            //e.joinMessage(message.isEmpty() ? null : MiniMessage.miniMessage().deserialize(message));
        } else {
            e.setJoinMessage(message.isEmpty() ? null : ChatColor.translateAlternateColorCodes('&', message));
        }
    }

}
