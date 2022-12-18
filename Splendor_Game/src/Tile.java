import java.util.Map;
import java.util.Objects;

/**
 * Class defining a Noble Tile in the game Splendor.
 * @author Christine LI - Clement LUC - Fandresena RAKOTOMAHEFA
 * @date 18/12/2022
 * @file Tile.java
 */
public class Tile{

    /**
     * Fields of the Tile class.
     * @param prestiges Prestige points of the tile.
     * @param name Name of the tile.
     * @param ressources The resources needed to acquire a noble.
     */
    private final int prestiges;
    private final String name;
    private final Map<Token, Integer> ressources;

    /**
     * Constructor method of the Tile class.
     * @param name Name of the noble.
     * @param prestiges Prestige points of the tile.
     * @param ressources The resources needed to acquire a noble.
     */
    public Tile(String name, int prestiges, Map<Token, Integer> ressources) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(ressources);
        this.prestiges = prestiges;
        this.name = name;
        this.ressources = ressources;
    }

    /**
     * Method rewriting equals for an Object of class Tile.
     * @param other An object.
     * @return True, if the fields are the same, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        Objects.requireNonNull(other);

        return other instanceof Tile c
                && this.prestiges == c.prestiges
                && this.ressources.equals(c.ressources);
    }

    /**
     * Method rewriting hashCode for an Object of the class Tile.
     * @return Integer corresponding to the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.prestiges, this.ressources);
    }

    /**
     * Method rewriting toString for an Object of the class Tile.
     * @return String of a Tile.
     */
    @Override
    public String toString() {
        return "\nName : " + this.name + "\n" +
                "Prestiges : " + this.prestiges + "\n" +
                "Ressources : " + Shell.toStringTokenSmallFormat(this.ressources, "  ") + "\n----------";
    }
    /**
     * Method that takes a string as argument and splits each character and initializes a tile's card from these elements.
     * @param line The line of the file to analyse
     * @param refLine String that is considered as reference if one element is empty
     * @return A card.
     */
    public static Tile fromText(String line, String refline) {
        Objects.requireNonNull(line);
        var arrayLine = line.split(",");

        var name = arrayLine[0];
        var prestige = Integer.parseInt(arrayLine[1]);
        var ressource = Import.importRessources(arrayLine, 3);
        Tile newTile = new Tile(name, prestige, ressource);
        return newTile;
    }
    /**
     * Methode to take a reference from a file line
     * @param line The line of the file to analyse
     * @param separator Separator
     * @return return always True
     */
    public static boolean isRefLine(String line, String separator) {
        return true;
    }

    /**
     * Getter method for an Object of type Tile.
     * @return Number of prestiges of the tile.
     */
    public int getPrestiges() {
        return this.prestiges;
    }

}
