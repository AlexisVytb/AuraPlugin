package fr.aura.commands;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import fr.aura.enums.AuraType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AuraModifyCommand implements CommandExecutor {
    
    private final AuraPlugin plugin;
    
    public AuraModifyCommand(AuraPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("aura.admin")) {
            sender.sendMessage("§cVous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /auramodify <joueur> <bleu|vert|jaune|rouge|orange|violet>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cJoueur introuvable !");
            return true;
        }
        
        AuraType newType = AuraType.fromString(args[1]);
        if (newType == null) {
            sender.sendMessage("§cType d'aura invalide ! Utilisez: bleu, vert, jaune, rouge, orange, ou violet");
            return true;
        }
        
        AuraPlayerData data = plugin.getAuraManager().getPlayerData(target.getUniqueId());
        AuraType oldType = data.getAuraType();
        
        data.setAuraType(newType);
        plugin.getAuraManager().savePlayerData(target.getUniqueId());
        
        // Mettre à jour l'équipe
        plugin.getAuraManager().addToTeam(target, newType);
        
        // Notifier
        sender.sendMessage("§aAura de " + target.getName() + " changée de " + 
            (oldType != null ? oldType.name() : "aucune") + " à " + newType.name());
        
        target.sendMessage(Component.text("§eVotre aura a été modifiée en ")
            .append(Component.text(newType.name()).color(newType.getTextColor()))
            .append(Component.text(" - " + newType.getCategory())));
        
        return true;
    }
}