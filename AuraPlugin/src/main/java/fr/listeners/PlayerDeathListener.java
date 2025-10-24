package fr.aura.listeners;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    
    private final AuraPlugin plugin;
    
    public PlayerDeathListener(AuraPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        AuraPlayerData data = plugin.getAuraManager().getPlayerData(player.getUniqueId());
        
        // Première mort = attribution de l'aura
        if (!data.hasAura()) {
            plugin.getAuraManager().assignAura(player);
        }
    }
}