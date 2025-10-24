package fr.aura.managers;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import fr.aura.enums.AuraType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuraManager {
    
    private final AuraPlugin plugin;
    private final Map<UUID, AuraPlayerData> playerDataMap;
    
    public AuraManager(AuraPlugin plugin) {
        this.plugin = plugin;
        this.playerDataMap = new HashMap<>();
    }
    
    public AuraPlayerData getPlayerData(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, 
            k -> plugin.getDatabaseManager().loadPlayer(uuid));
    }
    
    public void savePlayerData(UUID uuid) {
        AuraPlayerData data = playerDataMap.get(uuid);
        if (data != null) {
            plugin.getDatabaseManager().savePlayer(data);
        }
    }
    
    public void assignAura(Player player) {
        AuraPlayerData data = getPlayerData(player.getUniqueId());
        
        if (!data.hasAura()) {
            AuraType auraType = AuraType.getRandom();
            data.setAuraType(auraType);
            
            // Afficher le title
            Component title = Component.text("Aura débloquée !")
                .color(auraType.getTextColor());
            Component subtitle = Component.text(auraType.getCategory())
                .color(auraType.getTextColor());
            
            player.showTitle(Title.title(title, subtitle, 
                Title.Times.times(Duration.ofMillis(500), 
                    Duration.ofSeconds(3), Duration.ofSeconds(1))));
            
            // Ajouter à l'équipe
            addToTeam(player, auraType);
            
            // Appliquer le glowing
            applyGlowing(player, true);
            
            // Sauvegarder
            savePlayerData(player.getUniqueId());
            
            player.sendMessage(Component.text("Vous avez débloqué l'aura ")
                .append(Component.text(auraType.name()).color(auraType.getTextColor()))
                .append(Component.text(" - " + auraType.getCategory())));
        }
    }
    
    public void addToTeam(Player player, AuraType auraType) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        String teamName = "aura_" + auraType.name().toLowerCase();
        
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.color(auraType.getTextColor());
        }
        
        team.addEntry(player.getName());
    }
    
    public void applyGlowing(Player player, boolean glowing) {
        player.setGlowing(glowing);
    }
    
    public void applyTen(Player player, boolean active) {
        AuraPlayerData data = getPlayerData(player.getUniqueId());
        data.setTenActive(active);
        
        if (active) {
            int resistanceLevel = plugin.getConfig().getInt("effects.ten.resistance_level", 2);
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 
                Integer.MAX_VALUE, resistanceLevel - 1, false, false));
            applyGlowing(player, true);
        } else {
            player.removePotionEffect(PotionEffectType.RESISTANCE);
            applyGlowing(player, false);
        }
    }
    
    public void applyZetsu(Player player, boolean active) {
        AuraPlayerData data = getPlayerData(player.getUniqueId());
        data.setZetsuActive(active);
        
        if (active) {
            // Retirer ten/ren si actifs
            if (data.isTenActive()) applyTen(player, false);
            if (data.isRenActive()) applyRen(player, false);
            
            int invisLevel = plugin.getConfig().getInt("effects.zetsu.invisibility_level", 2);
            int speedLevel = plugin.getConfig().getInt("effects.zetsu.speed_level", 1);
            int blindLevel = plugin.getConfig().getInt("effects.zetsu.blindness_level", 2);
            
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 
                Integer.MAX_VALUE, invisLevel - 1, false, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 
                Integer.MAX_VALUE, speedLevel - 1, false, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 
                Integer.MAX_VALUE, blindLevel - 1, false, true));
            applyGlowing(player, false);
        } else {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }
    
    public void applyRen(Player player, boolean active) {
        AuraPlayerData data = getPlayerData(player.getUniqueId());
        data.setRenActive(active);
        
        if (active) {
            int strengthLevel = plugin.getConfig().getInt("effects.ren.strength_level", 4);
            int resistanceLevel = plugin.getConfig().getInt("effects.ren.resistance_level", 1);
            
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 
                Integer.MAX_VALUE, strengthLevel - 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 
                Integer.MAX_VALUE, resistanceLevel - 1, false, false));
            applyGlowing(player, true);
        } else {
            player.removePotionEffect(PotionEffectType.STRENGTH);
            player.removePotionEffect(PotionEffectType.RESISTANCE);
            applyGlowing(player, false);
        }
    }
    
    public void applyHatsu(Player player, boolean active) {
        AuraPlayerData data = getPlayerData(player.getUniqueId());
        data.setHatsuActive(active);
        
        if (active) {
            int resistanceLevel = plugin.getConfig().getInt("effects.hatsu.resistance_level", 1);
            int jumpLevel = plugin.getConfig().getInt("effects.hatsu.jump_boost_level", 2);
            
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 
                Integer.MAX_VALUE, resistanceLevel - 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 
                Integer.MAX_VALUE, jumpLevel - 1, false, false));
            applyGlowing(player, true);
            
            player.sendMessage(Component.text("§aVous avez débloqué /en, /shu et /ko !"));
        } else {
            player.removePotionEffect(PotionEffectType.RESISTANCE);
            player.removePotionEffect(PotionEffectType.JUMP_BOOST);
            applyGlowing(player, false);
        }
    }
    
    public void applyEn(Player player, boolean active) {
        AuraPlayerData data = getPlayerData(player.getUniqueId());
        data.setEnActive(active);
        
        if (active) {
            int strengthLevel = plugin.getConfig().getInt("effects.en.strength_level", 6);
            int resistanceLevel = plugin.getConfig().getInt("effects.en.resistance_level", 5);
            
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 
                Integer.MAX_VALUE, strengthLevel - 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 
                Integer.MAX_VALUE, resistanceLevel - 1, false, false));
            applyGlowing(player, true);
        } else {
            player.removePotionEffect(PotionEffectType.STRENGTH);
            player.removePotionEffect(PotionEffectType.RESISTANCE);
        }
    }
    
    public void applyShu(Player player, boolean active) {
        AuraPlayerData data = getPlayerData(player.getUniqueId());
        data.setShuActive(active);
        
        if (active) {
            player.sendMessage(Component.text("§aShu activé ! Tenez une arme ou un outil."));
        } else {
            player.sendMessage(Component.text("§cShu désactivé."));
        }
    }
    
    public void applyKo(Player player) {
        int strengthLevel = plugin.getConfig().getInt("effects.ko.strength_level", 17);
        int duration = plugin.getConfig().getInt("effects.ko.duration_seconds", 5);
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 
            duration * 20, strengthLevel - 1, false, false));
        
        player.sendMessage(Component.text("§c§lKO ! §ePuissance maximale pendant " + duration + " secondes !"));
        
        // Enregistrer le temps d'utilisation
        AuraPlayerData data = getPlayerData(player.getUniqueId());
        data.setKoLastUsed(System.currentTimeMillis());
        savePlayerData(player.getUniqueId());
        
        // Log
        plugin.getLogger().info(player.getName() + " a utilisé Ko.");
    }
    
    public int getEnRadius(Player player) {
        int baseRadius = plugin.getConfig().getInt("effects.en.base_radius", 35);
        int radiusPerLevel = plugin.getConfig().getInt("effects.en.radius_per_10_levels", 1);
        int maxRadius = plugin.getConfig().getInt("effects.en.max_radius", 100);
        
        int playerLevel = player.getLevel();
        int bonusRadius = (playerLevel / 10) * radiusPerLevel;
        
        return Math.min(baseRadius + bonusRadius, maxRadius);
    }
    
    public boolean canUpgradeTechnique(Player player, String technique, int currentLevel) {
        if (currentLevel >= 10) {
            return false;
        }
        
        int nextLevel = currentLevel + 1;
        String levelKey = nextLevel == 10 ? "level_max" : "level_" + nextLevel;
        int requiredXP = plugin.getConfig().getInt("xp_requirements." + technique + "." + levelKey, 10);
        
        return player.getLevel() >= requiredXP;
    }
    
    public void unloadPlayer(UUID uuid) {
        savePlayerData(uuid);
        playerDataMap.remove(uuid);
    }
}