package me.vivace.discord;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DiscordListener extends ListenerAdapter {
    public static Map<Player, String> authCode = new HashMap<>();

    public static TextChannel certificateChannel;

    @Override
    public void onReady(ReadyEvent event) {
        certificateChannel = event.getJDA().getTextChannelById(Main.CERTIFICATE_CHANNEL_ID);
    }

    //인증 서버
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isWebhookMessage()) return;

        TextChannel eventChannel = event.getChannel();
        String[] message = event.getMessage().getContentRaw().split(" ");

        if (!eventChannel.equals(certificateChannel)) return;

        if (message[0].equalsIgnoreCase("!인증")) {
            User u = event.getMember().getUser();

            try {
                Bukkit.getPlayer(message[1]);
            } catch (Exception e) {
                u.openPrivateChannel().queue(c -> {
                    c.sendMessage("그런 유저명이 없습니다.").queue();
                });

                return;
            }

            Player p = Bukkit.getPlayer(message[1]);
            if (p == null) return;

            if (Main.authPlayersUUIDs.contains(p.getUniqueId().toString())) {
                u.openPrivateChannel().queue(c -> {
                    c.sendMessage("이미 인증되어 있습니다.").queue();
                });
                return;
            }

            if (authCode.containsKey(p)) {
                u.openPrivateChannel().queue(c -> {
                    c.sendMessage("이미 인증코드가 전송되어 있습니다.").queue();
                });
                return;
            }

            String certificateCode = randomString(8);

            u.openPrivateChannel().queue(c -> {
                c.sendMessage("인증코드: " + certificateCode + "\n" +
                        "인증코드를 **" + p.getName() + "** 계정으로 입력하세요.").queue();
            });

            authCode.put(p, certificateCode);

            new BukkitRunnable() {

                @Override
                public void run() {
                    if (!authCode.containsKey(p)) {cancel(); return;}

                    authCode.remove(p);
                    u.openPrivateChannel().flatMap(c -> c.sendMessage("인증코드가 만료되었습니다.")).queue();
                }
            }.runTaskLater(Main.getPlugin(Main.class), 60 * 20);
        }
    }

    public static String randomString(int length) {
        StringBuilder temp = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }

        return temp.toString();
    }
}
