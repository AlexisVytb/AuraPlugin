package fr.aura.data;

import fr.aura.enums.AuraType;

import java.util.UUID;

public class AuraPlayerData {
    private final UUID uuid;
    private AuraType auraType;
    private boolean hasAura;
    
    // Niveaux des techniques
    private int tenLevel;
    private int zetsuLevel;
    private int renLevel;
    private int hatsuLevel;
    
    // Tags
    private boolean tenMax;
    private boolean renMax;
    private boolean enMax;
    
    // États actifs
    private boolean tenActive;
    private boolean zetsuActive;
    private boolean renActive;
    private boolean hatsuActive;
    private boolean enActive;
    private boolean shuActive;
    
    // Cooldowns
    private long koLastUsed;
    
    public AuraPlayerData(UUID uuid) {
        this.uuid = uuid;
        this.hasAura = false;
        this.tenLevel = 0;
        this.zetsuLevel = 0;
        this.renLevel = 0;
        this.hatsuLevel = 0;
        this.tenMax = false;
        this.renMax = false;
        this.enMax = false;
        this.koLastUsed = 0;
    }
    
    // Getters & Setters
    public UUID getUuid() {
        return uuid;
    }
    
    public AuraType getAuraType() {
        return auraType;
    }
    
    public void setAuraType(AuraType auraType) {
        this.auraType = auraType;
        this.hasAura = true;
    }
    
    public boolean hasAura() {
        return hasAura;
    }
    
    public void setHasAura(boolean hasAura) {
        this.hasAura = hasAura;
    }
    
    public int getTenLevel() {
        return tenLevel;
    }
    
    public void setTenLevel(int tenLevel) {
        this.tenLevel = tenLevel;
    }
    
    public int getZetsuLevel() {
        return zetsuLevel;
    }
    
    public void setZetsuLevel(int zetsuLevel) {
        this.zetsuLevel = zetsuLevel;
    }
    
    public int getRenLevel() {
        return renLevel;
    }
    
    public void setRenLevel(int renLevel) {
        this.renLevel = renLevel;
    }
    
    public int getHatsuLevel() {
        return hatsuLevel;
    }
    
    public void setHatsuLevel(int hatsuLevel) {
        this.hatsuLevel = hatsuLevel;
    }
    
    public boolean isTenMax() {
        return tenMax;
    }
    
    public void setTenMax(boolean tenMax) {
        this.tenMax = tenMax;
    }
    
    public boolean isRenMax() {
        return renMax;
    }
    
    public void setRenMax(boolean renMax) {
        this.renMax = renMax;
    }
    
    public boolean isEnMax() {
        return enMax;
    }
    
    public void setEnMax(boolean enMax) {
        this.enMax = enMax;
    }
    
    public boolean isTenActive() {
        return tenActive;
    }
    
    public void setTenActive(boolean tenActive) {
        this.tenActive = tenActive;
    }
    
    public boolean isZetsuActive() {
        return zetsuActive;
    }
    
    public void setZetsuActive(boolean zetsuActive) {
        this.zetsuActive = zetsuActive;
    }
    
    public boolean isRenActive() {
        return renActive;
    }
    
    public void setRenActive(boolean renActive) {
        this.renActive = renActive;
    }
    
    public boolean isHatsuActive() {
        return hatsuActive;
    }
    
    public void setHatsuActive(boolean hatsuActive) {
        this.hatsuActive = hatsuActive;
    }
    
    public boolean isEnActive() {
        return enActive;
    }
    
    public void setEnActive(boolean enActive) {
        this.enActive = enActive;
    }
    
    public boolean isShuActive() {
        return shuActive;
    }
    
    public void setShuActive(boolean shuActive) {
        this.shuActive = shuActive;
    }
    
    public long getKoLastUsed() {
        return koLastUsed;
    }
    
    public void setKoLastUsed(long koLastUsed) {
        this.koLastUsed = koLastUsed;
    }
    
    public boolean canUseAdvanced() {
        return hatsuActive;
    }
}