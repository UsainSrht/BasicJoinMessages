package me.usainsrht.basicJoinMessages;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class QuitListener implements Listener {

    BasicJoinMessages plugin;

    public QuitListener(BasicJoinMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String message = plugin.getQuitMessage(player);

        message = message.replace("%player%", player.getName());
        if (plugin.hasPapi()) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        if (plugin.isPaper()) {
            try {
                Component component = message.isEmpty() ? null : MiniMessage.miniMessage().deserialize(message);
                Method method = e.getClass().getDeclaredMethod("quitMessage", Component.class);
                method.setAccessible(true);
                method.invoke(e, component);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
            //e.quitMessage(message.isEmpty() ? null : MiniMessage.miniMessage().deserialize(message));
        } else {
            e.setQuitMessage(message.isEmpty() ? null : ChatColor.translateAlternateColorCodes('&', message));
        }
    }



}
