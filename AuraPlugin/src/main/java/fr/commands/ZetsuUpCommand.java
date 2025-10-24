package fr.aura.commands;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ZetsuUpCommand implements CommandExecutor {
    
    private final AuraPlugin plugin;
    
    public ZetsuUpCommand(AuraPlugin plugin) {
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
        
        int currentLevel = data.getZetsuLevel();
        
        if (currentLevel >= 10) {
            player.sendMessage("§cVotre Zetsu est déjà au niveau maximum !");
            return true;
        }
        
        int nextLevel = currentLevel + 1;
        String levelKey = nextLevel == 10 ? "level_max" : "level_" + nextLevel;
        int requiredXP = plugin.getConfig().getInt("xp_requirements.zetsu." + levelKey, 10);
        
        if (player.getLevel() < requiredXP) {
            player.sendMessage("§cVous avez besoin de " + requiredXP + " niveaux d'XP pour améliorer Zetsu ! (Actuellement: " + player.getLevel() + ")");
            return true;
        }
        
        data.setZetsuLevel(nextLevel);
        
        if (nextLevel == 10) {
            player.sendMessage("§a§lZETSU MAX ATTEINT !");
        } else {
            player.sendMessage("§aZetsu amélioré au niveau " + nextLevel + " !");
        }
        
        plugin.getAuraManager().savePlayerData(player.getUniqueId());
        
        return true;
    }
}