
/**
 * Enum class PollutionLevel 
 * 
 * Used to associate colour with pollution severity
 *
 * @author Zainudin Farah
 * @version 24/03/2025
 */
public enum PollutionLevel {
    PURPLE("0x800080ff", "Very Low"),
    CYAN("0x00ffffff", "Low"),
    GREEN("0x008000ff", "Moderate"),
    YELLOW("0xffff00ff", "High"),
    ORANGE("0xffa500ff", "Very High"),
    RED("0xff0000ff", "Hazardous");
    
    private final String hex;
    private final String label;

    // Constructor that assigns the colour and label to each constant
    PollutionLevel(String hex, String label) {
        this.hex = hex;
        this.label = label;
    }

    // Getter methods for enum values
    public String getHex() {
        return hex;
    }

    public String getLabel() {
        return label;
    }
}
