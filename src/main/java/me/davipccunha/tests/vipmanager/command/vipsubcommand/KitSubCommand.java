package me.davipccunha.tests.vipmanager.command.vipsubcommand;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.vipmanager.VIPManagerPlugin;
import me.davipccunha.tests.vipmanager.model.VIPType;
import me.davipccunha.tests.vipmanager.utils.VIPUtils;
import me.davipccunha.utils.messages.ErrorMessages;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class KitSubCommand implements VIPSubCommand {
    private final VIPManagerPlugin plugin;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("vipmanager.admin.kit")) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return true;
        }

        if (args.length <= 2) return false;

        final String playerName = args[1];
        final String vipName = args[2];

        final VIPType vipType = VIPType.getByName(vipName);

        if (vipType == null) {
            sender.sendMessage("§cTipo de Vip inválido.");
            return true;
        }

        VIPUtils.executeCommandsFromConfig(this.plugin.getConfig(), playerName, vipType, "rewards");

        return true;
    }

    @Override
    public String getUsage() {
        return "/vip kit <jogador> <vip>";
    }
}
