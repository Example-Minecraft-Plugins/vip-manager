package me.davipccunha.tests.vipmanager.command.vipsubcommand;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.vipmanager.VIPManagerPlugin;
import me.davipccunha.tests.vipmanager.model.VIPType;
import me.davipccunha.tests.vipmanager.model.VIPUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class VerSubCommand implements VIPSubCommand {
    private final VIPManagerPlugin plugin;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2 && sender instanceof Player) {
            Bukkit.dispatchCommand(sender, "vip ver " + sender.getName());
            return true;
        }

        final String playerName = args[1];

        if (!playerName.equals(sender.getName()) && !sender.hasPermission("vipmanager.admin.check")) {
            sender.sendMessage("§cVocê não tem permissão para executar esse comando.");
            return true;
        }

        final VIPUser vipUser = this.plugin.getVIPUserCache().get(playerName);
        if (vipUser == null) {
            if (sender.getName().equals(playerName)) {
                sender.sendMessage("");
                sender.sendMessage("§cVocê não possui nenhum VIP ativo.");
                sender.sendMessage("§eAdquira um VIP em nosso site §floja.pluncky.com");
                sender.sendMessage("");
            } else {
                sender.sendMessage("§cEste jogador não possui nenhum VIP ativo.");
            }

            return true;
        }

        List<String> message = new ArrayList<>(List.of(""));
        for (VIPType vipType : VIPType.values()) {
            if (vipUser.hasVIP(vipType)) {
                final long remainingTime = vipUser.getExpirationTimestamp(vipType) - System.currentTimeMillis();

                message.add(String.format(" %s §f- %s",
                        vipType.getPrefix(), this.formatVIPTime(remainingTime))
                );
            }
        }
        message.add("");

        sender.sendMessage(message.toArray(new String[0]));

        return true;
    }

    private String formatVIPTime(long remainingTime) {
        final long remainingDays = remainingTime / 1000 / 60 / 60 / 24;
        final long remainingHours = remainingTime / 1000 / 60 / 60 % 24;

        return String.format("%d dias e %d horas", remainingDays, remainingHours);
    }

    @Override
    public String getUsage() {
        return "§c/vip ver <jogador>";
    }
}
