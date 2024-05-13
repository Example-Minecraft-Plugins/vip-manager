package me.davipccunha.tests.vipmanager.command.vipsubcommand;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.vipmanager.VIPManagerPlugin;
import me.davipccunha.tests.vipmanager.model.VIPType;
import me.davipccunha.tests.vipmanager.model.VIPUser;
import me.davipccunha.tests.vipmanager.utils.VIPUtils;
import me.davipccunha.utils.messages.ErrorMessages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
public class AdicionarSubCommand implements VIPSubCommand {
    private final VIPManagerPlugin plugin;

    @Override
    @SuppressWarnings("deprecation")
    public boolean execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("vipmanager.admin.add")) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return true;
        }

        if (args.length < 4) return false;

        final String playerName = Bukkit.getPlayer(args[1]) != null ? Bukkit.getPlayer(args[1]).getName() : args[1];
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
            sender.sendMessage(ErrorMessages.PLAYER_NOT_FOUND.getMessage());
            return true;
        }

        final VIPType vipType = VIPType.getByName(args[2].toUpperCase());
        if (vipType == null) {
            sender.sendMessage("§cTipo de VIP inválido.");
            return true;
        }

        final int days = NumberUtils.toInt(args[3]);
        if (days <= 0) {
            sender.sendMessage(ErrorMessages.INVALID_AMOUNT.getMessage());
            return true;
        }

        final VIPUser temp = this.plugin.getVIPUserCache().get(playerName.toLowerCase());
        final VIPUser vipUser = temp == null ? new VIPUser(playerName) : temp;

        vipUser.addTime(vipType, days * 24 * 60 * 60 * 1000L);

        VIPUtils.executeCommandsFromConfig(this.plugin.getConfig(), playerName, vipType, "activation");

        this.plugin.getVIPUserCache().add(playerName.toLowerCase(), vipUser);

        if (Arrays.stream(args).noneMatch(arg -> arg.equalsIgnoreCase("-s")))
            this.announceNewVIP(playerName, vipType.getPrefix());

        final Player player = this.plugin.getServer().getPlayer(playerName);
        if (player != null) {
            final String[] message = {
                    "",
                    "",
                    String.format(" §d| §fVocê adquiriu %s §fpor %d dias!", vipType.getPrefix(), days),
                    String.format(" §d| §fObrigado por contribuir com o servidor, §6%s§f!", player.getName()),
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
        return "/vip adicionar <jogador> <vip> <dias>";
    }
}