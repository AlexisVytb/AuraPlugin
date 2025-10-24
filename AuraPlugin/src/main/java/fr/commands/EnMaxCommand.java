package fr.aura.commands;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnMaxCommand implements CommandExecutor {
    
    private final AuraPlugin plugin;
    
    public EnMaxCommand(AuraPlugin plugin) {
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
        
        if (data.isEnMax()) {
            player.sendMessage("§cVous avez déjà En max !");
            return true;
        }
        
        int requiredLevels = plugin.getConfig().getInt("xp_requirements.enmax", 500);
        
        if (player.getLevel() < requiredLevels) {
            player.sendMessage("§cVous avez besoin de " + requiredLevels + " niveaux d'XP ! (Actuellement: " + player.getLevel() + ")");
            return true;
        }
        
        data.setEnMax(true);
        plugin.getAuraManager().savePlayerData(player.getUniqueId());
        
        player.sendMessage("§a§lEn max débloqué ! Votre En peut maintenant atteindre 100 blocs de rayon !");
        
        return true;
    }
}