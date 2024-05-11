package me.davipccunha.tests.vipmanager.command.vipsubcommand;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.vipmanager.VIPManagerPlugin;
import me.davipccunha.tests.vipmanager.model.VIPType;
import me.davipccunha.tests.vipmanager.model.VIPUser;
import me.davipccunha.tests.vipmanager.utils.VIPUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
public class AdicionarSubCommand implements VIPSubCommand {
    private final VIPManagerPlugin plugin;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("vipmanager.admin.add")) {
            sender.sendMessage("§cVocê não tem permissão para executar esse comando.");
            return true;
        }

        if (args.length < 4) return false;

        final String playerName = args[1];

        final VIPType vipType = VIPType.getByName(args[2].toUpperCase());
        if (vipType == null) {
            sender.sendMessage("§cTipo de VIP inválido.");
            return true;
        }

        final int days = NumberUtils.toInt(args[3]);
        if (days <= 0) {
            sender.sendMessage("§cNúmero de dias inválido.");
            return true;
        }

        final VIPUser temp = this.plugin.getVIPUserCache().get(playerName);
        final VIPUser vipUser = temp == null ? new VIPUser(playerName) : temp;

        vipUser.addTime(vipType, days * 24 * 60 * 60 * 1000L);

        VIPUtils.executeCommandsFromConfig(this.plugin.getConfig(), playerName, vipType, "activation");

        this.plugin.getVIPUserCache().add(playerName, vipUser);

        if (Arrays.stream(args).noneMatch(arg -> arg.equalsIgnoreCase("-s")))
            this.announceNewVIP(playerName, vipType.getPrefix());

        final Player player = this.plugin.getServer().getPlayer(playerName);
        if (player != null) {
            final String[] message = {
                    "",
                    "",
                    String.format(" §d| §fVocê adquiriu %s §fpor %d dias!", vipType.getPrefix(), days),
                    String.format(" §d| §fObrigado por contribuir com o servidor, §6%s§f!", playerName),
                    " §d| §fVocê pode recolher seus itens utilizando /correio",
                    "",
                    ""
            };

            player.sendMessage(message);
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    private void announceNewVIP(String playerName, String vipPrefix) {
        for (Player player : this.plugin.getServer().getOnlinePlayers())
            player.sendTitle(vipPrefix, String.format("§f%s §eadquiriu %s", playerName, vipPrefix));
    }

    @Override
    public String getUsage() {
        return "§c/vip adicionar <jogador> <vip> <dias>";
    }
}