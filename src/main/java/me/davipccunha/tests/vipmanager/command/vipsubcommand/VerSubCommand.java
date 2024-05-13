package me.davipccunha.tests.vipmanager.command.vipsubcommand;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.vipmanager.VIPManagerPlugin;
import me.davipccunha.tests.vipmanager.model.VIPType;
import me.davipccunha.tests.vipmanager.model.VIPUser;
import me.davipccunha.utils.messages.ErrorMessages;
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

        if (!playerName.equalsIgnoreCase(sender.getName()) && !sender.hasPermission("vipmanager.admin.check")) {
            sender.sendMessage(ErrorMessages.NO_PERMISSION.getMessage());
            return true;
        }

        final VIPUser vipUser = this.plugin.getVIPUserCache().get(playerName.toLowerCase());
        if (vipUser == null) {
            if (sender.getName().equalsIgnoreCase(playerName)) {
                sender.sendMessage("");
                sender.sendMessage("§cVocê não possui nenhum VIP ativo.");
                sender.sendMessage("§eAdquira um VIP em nosso site §floja.pluncky.com");
                sender.sendMessage("");
            } else {
                sender.sendMessage("§cEsse jogador não possui nenhum VIP ativo.");
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

        StringBuilder sb = new StringBuilder();
        if (remainingDays > 0) {
            sb.append(remainingDays).append(" dia");
            if (remainingDays > 1) sb.append("s");
            sb.append(" e ");
        }

        sb.append(remainingHours).append(" hora");
        if (remainingHours > 1) sb.append("s");

        return sb.toString();
    }

    @Override
    public String getUsage() {
        return "/vip ver <jogador>";
    }
}
