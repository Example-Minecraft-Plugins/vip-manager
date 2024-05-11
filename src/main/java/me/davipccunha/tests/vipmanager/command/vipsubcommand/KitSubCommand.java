package me.davipccunha.tests.vipmanager.command.vipsubcommand;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.vipmanager.VIPManagerPlugin;
import me.davipccunha.tests.vipmanager.model.VIPType;
import me.davipccunha.tests.vipmanager.utils.VIPUtils;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class KitSubCommand implements VIPSubCommand {
    private final VIPManagerPlugin plugin;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("vipmanager.admin.kit")) {
            sender.sendMessage("§cVocê não tem permissão para executar esse comando.");
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
        return "§e/vip kit <jogador> <vip>";
    }
}
