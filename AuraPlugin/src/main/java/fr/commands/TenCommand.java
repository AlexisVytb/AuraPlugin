package fr.aura.commands;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TenCommand implements CommandExecutor {
    
    private final AuraPlugin plugin;
    
    public TenCommand(AuraPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCette commande ne peut être utilisée que par un joueur.");
            return true;
        }
        
        Player player = (Player) sender;
        AuraPlayerData data = plugin.getAuraManager().getPlayerData(player.getUniqueId());
        
        if (!data.hasAura()) {
            player.sendMessage("§cVous n'avez pas encore d'aura ! Mourez une première fois.");
            return true;
        }
        
        if (args.length == 0) {
            // Activer Ten
            if (data.isTenActive()) {
                player.sendMessage("§cTen est déjà actif !");
                return true;
            }
            
            plugin.getAuraManager().applyTen(player, true);
            player.sendMessage("§aTen activé !");
            
        } else if (args[0].equalsIgnoreCase("rmv")) {
            // Désactiver Ten
            if (!data.isTenActive()) {
                player.sendMessage("§cTen n'est pas actif !");
                return true;
            }
            
            plugin.getAuraManager().applyTen(player, false);
            player.sendMessage("§cTen désactivé !");
        }
        
        return true;
    }
}