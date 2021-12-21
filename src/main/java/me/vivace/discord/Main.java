package me.vivace.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    public final static String CERTIFICATE_CHANNEL_ID = "921973248953028699";
    public final static String DISCORD_BOT_TOKEN = "OTE5NjA5MDQzMTAwNzkwNzg0.YbYSqQ.ZxwHS3XBW-nj1BPvZbmvn2p0xp4";

    public static List<String> authPlayersUUIDs = new ArrayList<>();
    public static JDA discordJda;

    public final static String CONFIG_UUIDS_PATH = "certificate_players";

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

        if (getConfig().isList(CONFIG_UUIDS_PATH)) {
            authPlayersUUIDs = getConfig().getStringList(CONFIG_UUIDS_PATH);
        }

        try {
            JDABuilder builder = JDABuilder.createDefault(DISCORD_BOT_TOKEN);
            builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
            builder.addEventListeners(new DiscordListener());
            discordJda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(new MinecraftListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getConfig().set(CONFIG_UUIDS_PATH, authPlayersUUIDs);
        saveConfig();
    }
}
