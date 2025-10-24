package fr.aura.commands;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenUpCommand implements CommandExecutor {
    
    private final AuraPlugin plugin;
    
    public RenUpCommand(AuraPlugin plugin) {
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
        
        int currentLevel = data.getRenLevel();
        
        if (currentLevel >= 10) {
            player.sendMessage("§cVotre Ren est déjà au niveau maximum !");
            return true;
        }
        
        int nextLevel = currentLevel + 1;
        String levelKey = nextLevel == 10 ? "level_max" : "level_" + nextLevel;
        int requiredXP = plugin.getConfig().getInt("xp_requirements.ren." + levelKey, 10);
        
        if (player.getLevel() < requiredXP) {
            player.sendMessage("§cVous avez besoin de " + requiredXP + " niveaux d'XP pour améliorer Ren ! (Actuellement: " + player.getLevel() + ")");
            return true;
        }
        
        data.setRenLevel(nextLevel);
        
        if (nextLevel == 10) {
            data.setRenMax(true);
            player.sendMessage("§a§lREN MAX ATTEINT ! Vous pouvez maintenant utiliser En !");
        } else {
            player.sendMessage("§aRen amélioré au niveau " + nextLevel + " !");
        }
        
        plugin.getAuraManager().savePlayerData(player.getUniqueId());
        
        return true;
    }
}