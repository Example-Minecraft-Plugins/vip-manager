package me.davipccunha.tests.vipmanager.command.vipsubcommand;

import org.bukkit.command.CommandSender;

public interface VIPSubCommand {
    boolean execute(CommandSender sender, String[] args);

    String getUsage();
}
