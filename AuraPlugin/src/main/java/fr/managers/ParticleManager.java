package fr.aura.managers;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class ParticleManager {
    
    private final AuraPlugin plugin;
    private BukkitTask particleTask;
    
    public ParticleManager(AuraPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void startParticleTask() {
        if (!plugin.getConfig().getBoolean("particles.enabled", true)) {
            return;
        }
        
        int interval = plugin.getConfig().getInt("particles.update_interval_ticks", 10);
        
        particleTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                AuraPlayerData data = plugin.getAuraManager().getPlayerData(player.getUniqueId());
                
                if (!data.hasAura()) continue;
                
                // Particules pour Ren
                if (data.isRenActive()) {
                    spawnRenParticles(player, data);
                }
                
                // Particules pour En
                if (data.isEnActive()) {
                    spawnEnParticles(player, data);
                    applyEnEffects(player, data);
                }
            }
        }, 0L, interval);
    }
    
    private void spawnRenParticles(Player player, AuraPlayerData data) {
        int radius = plugin.getConfig().getInt("effects.ren.particle_radius", 5);
        Location loc = player.getLocation();
        
        // Cercle de particules autour du joueur
        for (int i = 0; i < 20; i++) {
            double angle = 2 * Math.PI * i / 20;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            Location particleLoc = loc.clone().add(x, 0.5, z);
            player.getWorld().spawnParticle(
                Particle.DUST,
                particleLoc,
                1,
                new Particle.DustOptions(data.getAuraType().getParticleColor(), 1.0f)
            );
        }
    }
    
    private void spawnEnParticles(Player player, AuraPlayerData data) {
        int radius = plugin.getAuraManager().getEnRadius(player);
        Location loc = player.getLocation();
        
        // Cercle de particules plus grand
        for (int i = 0; i < 30; i++) {
            double angle = 2 * Math.PI * i / 30;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            
            Location particleLoc = loc.clone().add(x, 0.5, z);
            player.getWorld().spawnParticle(
                Particle.DUST,
                particleLoc,
                2,
                new Particle.DustOptions(data.getAuraType().getParticleColor(), 1.5f)
            );
        }
    }
    
    private void applyEnEffects(Player player, AuraPlayerData data) {
        int radius = plugin.getAuraManager().getEnRadius(player);
        Location loc = player.getLocation();
        int blindLevel = plugin.getConfig().getInt("effects.en.enemy_blindness_level", 2);
        
        for (Player nearby : player.getWorld().getPlayers()) {
            if (nearby.equals(player)) continue;
            
            double distance = nearby.getLocation().distance(loc);
            if (distance <= radius) {
                // Retirer strength et resistance
                nearby.removePotionEffect(PotionEffectType.STRENGTH);
                nearby.removePotionEffect(PotionEffectType.RESISTANCE);
                
                // Appliquer blindness et glowing blanc
                nearby.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 
                    40, blindLevel - 1, false, false));
                nearby.setGlowing(true);
            }
        }
    }
    
    public void stopParticleTask() {
        if (particleTask != null) {
            particleTask.cancel();
        }
    }
}