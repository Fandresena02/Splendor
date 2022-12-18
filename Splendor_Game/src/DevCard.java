import java.util.Objects;
import java.util.List;
import java.util.Map;

/**
 * Class defining a Development Card in the Splendor game.
 * @author Christine LI - Clement LUC - Fandresena RAKOTOMAHEFA
 * @date 18/12/2022
 * @file DevCard.java
 */
public class DevCard{
    /**
     * Fields of the DevCard class.
     * @param prestiges Prestige points of the card.
     * @param level Level of the card.
     * @param bonus Color of the card, which is defined by a color of counters.
     * @param illustration Illustration of the card
     * @param ressources The necessary counters for the purchase of the card.
     */
    private final Integer prestiges;
    private final int level;
    private final int bonus;
    private final String illustration; 
    private final Map<Token, Integer> ressources;

    /**
     * Constructor method of the DevCard class.
     * @param prestiges Prestige points of the card.
     * @param level Level of the card.
     * @param ressources The necessary counters for the purchase of the card.
     * @param bonus Color of the card, which is defined by a color of counters.
     */
    public DevCard(Integer prestiges, int level, String illustration, Map<Token, Integer> ressources, int bonus) {
        Objects.requireNonNull(ressources);

        this.prestiges = prestiges;
        this.level = level;
        this.ressources = ressources;
        this.bonus = bonus;
        this.illustration = illustration;
    }

    /**
     * Method rewriting equals for an Object of class DevCard.
     * @param other An object.
     * @return True, if the fields are the same, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        Objects.requireNonNull(other);
        return other instanceof DevCard c
        && this.prestiges == c.prestiges
        && this.level == c.level 
        && this.bonus == c.bonus
        && this.ressources.equals(c.ressources)
        && this.illustration.equals(c.illustration);
    }

   /**
     * Method rewriting hashCode for an Object of the class DevCard.
     * @return Integer corresponding to the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.prestiges, this.illustration, this.level, this.ressources, this.bonus);
    }

    /**
     * Method returning the string corresponding to the resources needed to buy a card (HashMap).
     * @return String of characters symbolizing the tokens needed to buy the card.
     */
    public String toStringHash() {
        StringBuilder strb = new StringBuilder();
        for (Token counter : this.ressources.keySet()) {
            strb.append(counter.toString()).append(":").append(this.ressources.get(counter)).append("/");
        }
        return strb.toString();
    }

    /**
     * Method rewriting toString for an Object of the class DevCard.
     * @return String of a card.
     */
    @Override
    public String toString() {
        var prest = this.getPrestiges() == null ? "" : this.prestiges;
        var illustStr = this.illustration == null ? "" : this.illustration;
        return "\nL" + this.level + " P" + prest +" B" 
        + this.bonus + " ILL:" + illustStr + "\n"
        + Shell.toStringTokenSmallFormat(this.ressources,  "  ") 
        + "\n---------------------------------";
    }
    /**
     * Method that takes a string as argument and splits each character and initializes a dev's card from these elements.
     * @param line The line of the file to analyse
     * @param refLine String that is considered as reference if one element is empty 
     * @return A card.
     */
    public static DevCard fromText(String line, String refLine) {
        Objects.requireNonNull(line);
        Objects.requireNonNull(refLine);

        var separator = ",";
        var arrayLine = line.split(separator);
        var arrayRefLine = refLine.split(separator);

        var colorBonus = Import.colorHash(List.of("green", "white", "blue", "black", "red"));

        Integer prestige = !arrayLine[2].equals("") ? Integer.parseInt(arrayLine[2]) : null;
        int level = !arrayLine[0].equals("") ? Integer.parseInt(arrayLine[0]) : Integer.parseInt(arrayRefLine[0]);
        var bonus = !arrayLine[1].equals("") ? colorBonus.get(arrayLine[1]) : colorBonus.get(arrayRefLine[1]);
        var illustStr = !arrayLine[4].equals("") ? arrayLine[4] : arrayRefLine[4];

        DevCard newCard = new DevCard(prestige, level, illustStr, Import.importRessources(arrayLine, 5), bonus);
        return newCard;
    }
    /**
     * Methode to take a reference from a file line
     * @param line The line of the file to analyse
     * @param separator Separator 
     * @return False if the first element(column) of the file is empty, true otherwise. 
     */
    public static boolean isRefLine(String line, String separator) {
        var array = line.split(separator);
        if (array[0].equals("")) {
            return false;
        }
        return true;
    }
    /**
     * Getter method for an Object of type DevCard.
     * @return Number of prestiges of the card.
     */
    public Integer getPrestiges() {
        return this.prestiges;
    }

    /**
     * Getter method for an Object of type DevCard.
     * @return Level of the card.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Getter method for an Object of type DevCard.
     * @return Counters to buy a card.
     */
    public Map<Token, Integer> getRessources() {
        return this.ressources;
    }

    /**
     * Getter method for an Object of type DevCard.
     * @return Bonus of the card.
     */
    public int getBonus() {
        return this.bonus;
    }

    /**
     * Getter method for an Object of type DevCard.
     * @return Illustration of the card.
     */
    public String getIllustration() {
        return this.illustration;
    }
}
