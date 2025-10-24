package fr.aura;

import fr.aura.commands.*;
import fr.aura.database.DatabaseManager;
import fr.aura.listeners.PlayerDeathListener;
import fr.aura.listeners.PlayerJoinListener;
import fr.aura.listeners.ShuItemListener;
import fr.aura.managers.AuraManager;
import fr.aura.managers.ParticleManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AuraPlugin extends JavaPlugin {
    
    private static AuraPlugin instance;
    private DatabaseManager databaseManager;
    private AuraManager auraManager;
    private ParticleManager particleManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Sauvegarder la config par défaut
        saveDefaultConfig();
        
        // Initialiser les managers
        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();
        
        auraManager = new AuraManager(this);
        particleManager = new ParticleManager(this);
        
        // Enregistrer les commandes
        registerCommands();
        
        // Enregistrer les listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new ShuItemListener(this), this);
        
        // Démarrer les tasks
        particleManager.startParticleTask();
        
        getLogger().info("AuraSystem activé avec succès !");
    }
    
    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("AuraSystem désactivé !");
    }
    
    private void registerCommands() {
        getCommand("ten").setExecutor(new TenCommand(this));
        getCommand("zetsu").setExecutor(new ZetsuCommand(this));
        getCommand("ren").setExecutor(new RenCommand(this));
        getCommand("hatsu").setExecutor(new HatsuCommand(this));
        getCommand("en").setExecutor(new EnCommand(this));
        getCommand("enmax").setExecutor(new EnMaxCommand(this));
        getCommand("shu").setExecutor(new ShuCommand(this));
        getCommand("ko").setExecutor(new KoCommand(this));
        getCommand("tenup").setExecutor(new TenUpCommand(this));
        getCommand("zetsuup").setExecutor(new ZetsuUpCommand(this));
        getCommand("renup").setExecutor(new RenUpCommand(this));
        getCommand("hatsuup").setExecutor(new HatsuUpCommand(this));
        getCommand("auramodify").setExecutor(new AuraModifyCommand(this));
    }
    
    public static AuraPlugin getInstance() {
        return instance;
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public AuraManager getAuraManager() {
        return auraManager;
    }
    
    public ParticleManager getParticleManager() {
        return particleManager;
    }
}