package fr.aura.enums;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;

public enum AuraType {
    BLEU("Renforcement", NamedTextColor.BLUE, Color.BLUE, "§9"),
    VERT("Transformation", NamedTextColor.GREEN, Color.GREEN, "§a"),
    JAUNE("Matérialisation", NamedTextColor.YELLOW, Color.YELLOW, "§e"),
    ROUGE("Émission", NamedTextColor.RED, Color.RED, "§c"),
    ORANGE("Manipulation", TextColor.color(255, 165, 0), Color.ORANGE, "§6"),
    VIOLET("Spécialisation", TextColor.color(148, 0, 211), Color.PURPLE, "§5");
    
    private final String category;
    private final TextColor textColor;
    private final Color particleColor;
    private final String colorCode;
    
    AuraType(String category, TextColor textColor, Color particleColor, String colorCode) {
        this.category = category;
        this.textColor = textColor;
        this.particleColor = particleColor;
        this.colorCode = colorCode;
    }
    
    public String getCategory() {
        return category;
    }
    
    public TextColor getTextColor() {
        return textColor;
    }
    
    public Color getParticleColor() {
        return particleColor;
    }
    
    public String getColorCode() {
        return colorCode;
    }
    
    public static AuraType getRandom() {
        AuraType[] values = values();
        return values[(int) (Math.random() * values.length)];
    }
    
    public static AuraType fromString(String name) {
        for (AuraType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}