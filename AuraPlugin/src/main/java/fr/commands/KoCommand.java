package fr.aura.commands;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KoCommand implements CommandExecutor {
    
    private final AuraPlugin plugin;
    
    public KoCommand(AuraPlugin plugin) {
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
            player.sendMessage("§cVous devez d'abord activer Hatsu pour utiliser Ko !");
            return true;
        }
        
        // Vérifier le cooldown
        long lastUsed = data.getKoLastUsed();
        long cooldownHours = plugin.getConfig().getInt("effects.ko.cooldown_hours", 24);
        long cooldownMillis = cooldownHours * 60 * 60 * 1000;
        long timeSinceLastUse = System.currentTimeMillis() - lastUsed;
        
        if (timeSinceLastUse < cooldownMillis) {
            long timeRemaining = cooldownMillis - timeSinceLastUse;
            long hoursRemaining = timeRemaining / (60 * 60 * 1000);
            long minutesRemaining = (timeRemaining % (60 * 60 * 1000)) / (60 * 1000);
            
            player.sendMessage("§cKo est en cooldown ! Temps restant: " + hoursRemaining + "h " + minutesRemaining + "m");
            return true;
        }
        
        // Activer Ko
        plugin.getAuraManager().applyKo(player);
        
        return true;
    }
}