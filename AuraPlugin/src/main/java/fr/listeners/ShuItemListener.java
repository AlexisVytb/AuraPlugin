package fr.aura.listeners;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShuItemListener implements Listener {
    
    private final AuraPlugin plugin;
    
    public ShuItemListener(AuraPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        AuraPlayerData data = plugin.getAuraManager().getPlayerData(player.getUniqueId());
        
        if (!data.isShuActive()) {
            return;
        }
        
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack oldItem = player.getInventory().getItem(event.getPreviousSlot());
        
        // Retirer les enchantements de l'ancien item
        if (oldItem != null && oldItem.getType() != Material.AIR) {
            removeTemporaryEnchantments(oldItem);
        }
        
        // Ajouter les enchantements au nouvel item
        if (newItem != null && newItem.getType() != Material.AIR) {
            applyTemporaryEnchantments(newItem, data);
        }
    }
    
    private void applyTemporaryEnchantments(ItemStack item, AuraPlayerData data) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        Material type = item.getType();
        
        // Épées
        if (type.name().endsWith("_SWORD")) {
            int sharpnessLevel = plugin.getConfig().getInt("effects.shu.sword_sharpness_level", 5);
            meta.addEnchant(Enchantment.SHARPNESS, sharpnessLevel, true);
        }
        // Pioches
        else if (type.name().endsWith("_PICKAXE")) {
            int efficiencyLevel = plugin.getConfig().getInt("effects.shu.tool_efficiency_level", 5);
            meta.addEnchant(Enchantment.EFFICIENCY, efficiencyLevel, true);
        }
        // Pelles
        else if (type.name().endsWith("_SHOVEL")) {
            int efficiencyLevel = plugin.getConfig().getInt("effects.shu.tool_efficiency_level", 5);
            meta.addEnchant(Enchantment.EFFICIENCY, efficiencyLevel, true);
        }
        // Haches
        else if (type.name().endsWith("_AXE")) {
            int sharpnessLevel = plugin.getConfig().getInt("effects.shu.sword_sharpness_level", 5);
            int efficiencyLevel = plugin.getConfig().getInt("effects.shu.tool_efficiency_level", 5);
            meta.addEnchant(Enchantment.SHARPNESS, sharpnessLevel, true);
            meta.addEnchant(Enchantment.EFFICIENCY, efficiencyLevel, true);
        }
        
        item.setItemMeta(meta);
    }
    
    private void removeTemporaryEnchantments(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        // Retirer les enchantements temporaires de Shu
        meta.removeEnchant(Enchantment.SHARPNESS);
        meta.removeEnchant(Enchantment.EFFICIENCY);
        
        item.setItemMeta(meta);
    }
}