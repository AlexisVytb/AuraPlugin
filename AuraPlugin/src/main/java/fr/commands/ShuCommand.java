package fr.aura.commands;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShuCommand implements CommandExecutor {
    
    private final AuraPlugin plugin;
    
    public ShuCommand(AuraPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCette commande ne peut être utilisée que par un joueur.");
            return true;
    }
}
        }
        
        Player player = (Player) sender;
        AuraPlayerData data = plugin.getAuraManager().getPlayerData(player.getUniqueId());
        
        if (!data.hasAura()) {
            player.sendMessage("§cVous n'avez pas encore d'aura !");
            return true;
        }
        
        if (!data.canUseAdvanced()) {
            player.sendMessage("§cVous devez d'abord activer Hatsu pour utiliser Shu !");
            return true;
        }
        
        if (!data.isTenMax()) {
            player.sendMessage("§cVous devez avoir Ten max pour utiliser Shu !");
            return true;
        }
        
        if (args.length == 0) {
            if (data.isShuActive()) {
                player.sendMessage("§cShu est déjà actif !");
                return true;
            }
            
            plugin.getAuraManager().applyShu(player, true);
            
        } else if (args[0].equalsIgnoreCase("rmv")) {
            if (!data.isShuActive()) {
                player.sendMessage("§cShu n'est pas actif !");
                return true;
            }
            
            plugin.getAuraManager().applyShu(player, false);
        }
        
        return true;