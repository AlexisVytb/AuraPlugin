package fr.aura.listeners;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {
    
    private final AuraPlugin plugin;
    
    public PlayerJoinListener(AuraPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AuraPlayerData data = plugin.getAuraManager().getPlayerData(player.getUniqueId());
        
        // Si le joueur a déjà une aura, l'ajouter à la team et appliquer glowing
        if (data.hasAura()) {
            plugin.getAuraManager().addToTeam(player, data.getAuraType());
            plugin.getAuraManager().applyGlowing(player, true);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getAuraManager().unloadPlayer(player.getUniqueId());
    }
}