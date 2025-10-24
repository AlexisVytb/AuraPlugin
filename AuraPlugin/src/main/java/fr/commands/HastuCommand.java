package fr.aura.commands;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HatsuCommand implements CommandExecutor {
    
    private final AuraPlugin plugin;
    
    public HatsuCommand(AuraPlugin plugin) {
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
            if (data.isHatsuActive()) {
                player.sendMessage("§cHatsu est déjà actif !");
                return true;
            }
            
            plugin.getAuraManager().applyHatsu(player, true);
            player.sendMessage("§aHatsu activé !");
            
        } else if (args[0].equalsIgnoreCase("rmv")) {
            if (!data.isHatsuActive()) {
                player.sendMessage("§cHatsu n'est pas actif !");
                return true;
            }
            
            plugin.getAuraManager().applyHatsu(player, false);
            player.sendMessage("§cHatsu désactivé !");
        }
        
        return true;
    }
}