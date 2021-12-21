package me.vivace.discord;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CertificateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return true;

        if (!command.getName().equals("인증")) return true;
        if (args.length < 1) return true;

        return false;
    }
}
