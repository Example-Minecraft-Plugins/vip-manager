package me.davipccunha.tests.vipmanager.command.vipsubcommand;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.vipmanager.VIPManagerPlugin;
import me.davipccunha.tests.vipmanager.model.VIPType;
import me.davipccunha.tests.vipmanager.model.VIPUser;
import me.davipccunha.tests.vipmanager.utils.VIPUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

@RequiredArgsConstructor
public class RemoverSubCommand implements VIPSubCommand {
    private final VIPManagerPlugin plugin;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("vipmanager.admin.remove")) {
            sender.sendMessage("§cVocê não tem permissão para executar esse comando.");
            return true;
        }

        if (args.length < 3) return false;

        final String playerName = args[1];
        final String vipName = args[2];

        final VIPUser vipUser = this.plugin.getVIPUserCache().get(playerName);
        if (vipUser == null) {
            sender.sendMessage("§cEsse jogador não possui um VIP ativo.");
            return true;
        }

        final VIPType vipType = VIPType.getByName(vipName);
        if (vipType == null) {
            sender.sendMessage("§cTipo de Vip inválido.");
            return true;
        }

        if (!vipUser.hasVIP(vipType)) {
            sender.sendMessage("§cEsse jogador não possui " + vipType.getPrefix());
            return true;
        }

        if (args.length >= 4) {
            final int hours = NumberUtils.toInt(args[3]);
            if (hours <= 0) {
                sender.sendMessage("§cNúmero de horas inválido.");
                return true;
            }

            final boolean belowZero = vipUser.removeTime(vipType, hours * 60 * 60 * 1000L);

            sender.sendMessage(String.format("§aRemovidas %d horas de %s de §f%s§a.", hours, vipType.getPrefix(), playerName));
            if (belowZero) {
                this.removeVIP(vipUser, vipType);
                sender.sendMessage(String.format("§aRemovido %s de §f%s§a.", vipType.getPrefix(), playerName));
            }

        } else {
            this.removeVIP(vipUser, vipType);
            sender.sendMessage(String.format("§aRemovido %s de §f%s§a.", vipType.getPrefix(), playerName));
        }

        if (!vipUser.hasAnyVIP()) {
            this.plugin.getVIPUserCache().remove(vipUser.getName());
        } else {
            this.plugin.getVIPUserCache().add(vipUser.getName(), vipUser);
        }

        return true;
    }

    private void removeVIP(VIPUser user, VIPType vipType) {
        user.removeVIP(vipType);
        VIPUtils.executeCommandsFromConfig(this.plugin.getConfig(), user.getName(), vipType, "deactivation");
    }

    @Override
    public String getUsage() {
        return "§c/vip remover <jogador> <vip> [horas]";
    }
}
