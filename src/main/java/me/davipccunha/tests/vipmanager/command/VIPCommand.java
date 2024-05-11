package me.davipccunha.tests.vipmanager.command;

import me.davipccunha.tests.vipmanager.VIPManagerPlugin;
import me.davipccunha.tests.vipmanager.command.vipsubcommand.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class VIPCommand implements CommandExecutor {
    private static String COMMAND_USAGE;
    private final Map<String, VIPSubCommand> subCommands = new HashMap<>();

    public VIPCommand(VIPManagerPlugin plugin) {
        this.subCommands.put("adicionar", new AdicionarSubCommand(plugin));
        this.subCommands.put("remover", new RemoverSubCommand(plugin));
        this.subCommands.put("kit", new KitSubCommand(plugin));
        this.subCommands.put("ver", new VerSubCommand(plugin));

        this.updateUsage();
    }

    private void updateUsage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("§c/vip <");
        for (String subCommand : this.subCommands.keySet()) {
            stringBuilder.append(subCommand).append(" | ");
        }
        stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length());
        stringBuilder.append(">");

        COMMAND_USAGE = stringBuilder.toString();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cUso: " + COMMAND_USAGE);
            return false;
        }

        final VIPSubCommand subCommand = this.subCommands.get(args[0]);

        if (subCommand == null) {
            sender.sendMessage("§cSubcomando não encontrado.");
            sender.sendMessage("§cUso: " + COMMAND_USAGE);
            return false;
        }

        if (!subCommand.execute(sender, args)) {
            sender.sendMessage("§cUso: " + subCommand.getUsage());
            return false;
        }

        return true;
    }
}
