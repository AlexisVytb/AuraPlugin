package fr.aura.commands;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnCommand implements CommandExecutor {
    
    private final AuraPlugin plugin;
    
    public EnCommand(AuraPlugin plugin) {
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
            player.sendMessage("§cVous n'avez pas encore d'aura !");
            return true;
        }
        
        if (!data.canUseAdvanced()) {
            player.sendMessage("§cVous devez d'abord activer Hatsu pour utiliser En !");
            return true;
        }
        
        if (!data.isTenMax() || !data.isRenMax()) {
            player.sendMessage("§cVous devez avoir Ten max ET Ren max pour utiliser En !");
            return true;
        }
        
        if (args.length == 0) {
            if (data.isEnActive()) {
                player.sendMessage("§cEn est déjà actif !");
                return true;
            }
            
            int radius = plugin.getAuraManager().getEnRadius(player);
            plugin.getAuraManager().applyEn(player, true);
            player.sendMessage("§aEn activé ! Rayon: " + radius + " blocs");
            
        } else if (args[0].equalsIgnoreCase("rmv")) {
            if (!data.isEnActive()) {
                player.sendMessage("§cEn n'est pas actif !");
                return true;
            }
            
            plugin.getAuraManager().applyEn(player, false);
            player.sendMessage("§cEn désactivé !");
        }
        
        return true;
    }
}