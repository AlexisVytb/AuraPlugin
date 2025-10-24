package fr.aura.commands;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenCommand implements CommandExecutor {
    
    private final AuraPlugin plugin;
    
    public RenCommand(AuraPlugin plugin) {
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
        
        if (args.length == 0) {
            if (data.isRenActive()) {
                player.sendMessage("§cRen est déjà actif !");
                return true;
            }
            
            plugin.getAuraManager().applyRen(player, true);
            player.sendMessage("§aRen activé !");
            
        } else if (args[0].equalsIgnoreCase("rmv")) {
            if (!data.isRenActive()) {
                player.sendMessage("§cRen n'est pas actif !");
                return true;
            }
            
            plugin.getAuraManager().applyRen(player, false);
            player.sendMessage("§cRen désactivé !");
        }
        
        return true;
    }
}