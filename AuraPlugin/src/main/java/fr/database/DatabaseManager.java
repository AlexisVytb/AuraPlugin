package fr.aura.database;

import fr.aura.AuraPlugin;
import fr.aura.data.AuraPlayerData;
import fr.aura.enums.AuraType;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class DatabaseManager {
    
    private final AuraPlugin plugin;
    private Connection connection;
    
    public DatabaseManager(AuraPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void initialize() {
        try {
            String type = plugin.getConfig().getString("database.type", "sqlite");
            
            if (type.equalsIgnoreCase("sqlite")) {
                File dataFolder = plugin.getDataFolder();
                if (!dataFolder.exists()) {
                    dataFolder.mkdirs();
                }
                
                String path = dataFolder.getAbsolutePath() + File.separator + "aura.db";
                connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            } else {
                // MySQL configuration
                String host = plugin.getConfig().getString("database.host");
                int port = plugin.getConfig().getInt("database.port");
                String database = plugin.getConfig().getString("database.database");
                String username = plugin.getConfig().getString("database.username");
                String password = plugin.getConfig().getString("database.password");
                
                connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database,
                    username, password
                );
            }
            
            createTables();
            plugin.getLogger().info("Base de données connectée avec succès !");
            
        } catch (SQLException e) {
            plugin.getLogger().severe("Erreur lors de la connexion à la base de données !");
            e.printStackTrace();
        }
    }
    
    private void createTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS aura_players (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "aura_type VARCHAR(20)," +
                "has_aura BOOLEAN," +
                "ten_level INTEGER," +
                "zetsu_level INTEGER," +
                "ren_level INTEGER," +
                "hatsu_level INTEGER," +
                "ten_max BOOLEAN," +
                "ren_max BOOLEAN," +
                "en_max BOOLEAN," +
                "ko_last_used BIGINT" +
                ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    public void savePlayer(AuraPlayerData data) {
        String sql;
        String dbType = plugin.getConfig().getString("database.type", "sqlite");
        
        if (dbType.equalsIgnoreCase("mysql")) {
            sql = "INSERT INTO aura_players (uuid, aura_type, has_aura, ten_level, " +
                    "zetsu_level, ren_level, hatsu_level, ten_max, ren_max, en_max, ko_last_used) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "aura_type=VALUES(aura_type), has_aura=VALUES(has_aura), " +
                    "ten_level=VALUES(ten_level), zetsu_level=VALUES(zetsu_level), " +
                    "ren_level=VALUES(ren_level), hatsu_level=VALUES(hatsu_level), " +
                    "ten_max=VALUES(ten_max), ren_max=VALUES(ren_max), " +
                    "en_max=VALUES(en_max), ko_last_used=VALUES(ko_last_used)";
        } else {
            sql = "INSERT OR REPLACE INTO aura_players (uuid, aura_type, has_aura, ten_level, " +
                    "zetsu_level, ren_level, hatsu_level, ten_max, ren_max, en_max, ko_last_used) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, data.getUuid().toString());
            stmt.setString(2, data.getAuraType() != null ? data.getAuraType().name() : null);
            stmt.setBoolean(3, data.hasAura());
            stmt.setInt(4, data.getTenLevel());
            stmt.setInt(5, data.getZetsuLevel());
            stmt.setInt(6, data.getRenLevel());
            stmt.setInt(7, data.getHatsuLevel());
            stmt.setBoolean(8, data.isTenMax());
            stmt.setBoolean(9, data.isRenMax());
            stmt.setBoolean(10, data.isEnMax());
            stmt.setLong(11, data.getKoLastUsed());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Erreur lors de la sauvegarde du joueur " + data.getUuid());
            e.printStackTrace();
        }
    }
    
    public AuraPlayerData loadPlayer(UUID uuid) {
        String sql = "SELECT * FROM aura_players WHERE uuid = ?";
        AuraPlayerData data = new AuraPlayerData(uuid);
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String auraTypeName = rs.getString("aura_type");
                if (auraTypeName != null) {
                    data.setAuraType(AuraType.valueOf(auraTypeName));
                }
                data.setHasAura(rs.getBoolean("has_aura"));
                data.setTenLevel(rs.getInt("ten_level"));
                data.setZetsuLevel(rs.getInt("zetsu_level"));
                data.setRenLevel(rs.getInt("ren_level"));
                data.setHatsuLevel(rs.getInt("hatsu_level"));
                data.setTenMax(rs.getBoolean("ten_max"));
                data.setRenMax(rs.getBoolean("ren_max"));
                data.setEnMax(rs.getBoolean("en_max"));
                data.setKoLastUsed(rs.getLong("ko_last_used"));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Erreur lors du chargement du joueur " + uuid);
            e.printStackTrace();
        }
        
        return data;
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
}