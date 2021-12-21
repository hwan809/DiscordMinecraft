package me.vivace.discord;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MinecraftListener implements Listener {

    @EventHandler
    public void authEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (!DiscordListener.authCode.containsKey(p)) return;

        String certificateCode = DiscordListener.authCode.get(p);

        if (e.getMessage().equals(certificateCode)) {
            Main.authPlayersUUIDs.add(p.getUniqueId().toString());
            DiscordListener.authCode.remove(p);

            p.sendMessage(ChatColor.BLUE + "[+] " + ChatColor.GREEN + "인증이 완료되었습니다!");
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        String playerUUID = p.getUniqueId().toString();

        if (!Main.authPlayersUUIDs.contains(playerUUID)) e.setCancelled(true);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String playerUUID = p.getUniqueId().toString();

        if (!Main.authPlayersUUIDs.contains(playerUUID)) {
            p.sendMessage(ChatColor.RED + "[+] 계정이 인증되지 않았습니다.");
            p.sendMessage(ChatColor.GOLD + "공식 서버" + ChatColor.RED + "로 이동해 계정을 인증하세요.\n"
                    + ChatColor.BLUE + "https://discord.gg/x7RKwjeQ");
        }
    }
}
