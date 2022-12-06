import java.util.Map;
import java.util.Objects;

public class Tile{

    private final int prestiges;
    private final String name; 
    private final Map<Token, Integer> ressources;

    public Tile(String name, int prestiges, Map<Token, Integer> ressources) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(ressources);
        this.prestiges = prestiges;
        this.name = name;
        this.ressources = ressources;
    }

    @Override
    public boolean equals(Object other) {
        Objects.requireNonNull(other);
        
        return other instanceof Tile c
        && this.prestiges == c.prestiges
        && this.ressources.equals(c.ressources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.prestiges, this.ressources);
    }
    

    @Override
    public String toString() {
        return "\nName : " + this.name + "\n" +
        "Prestiges : " + this.prestiges + "\n" +
        "Ressources : " + Shell.toStringTokenSmallFormat(this.ressources, "  ") + "\n----------";
    }

  
    public int getPrestiges() {
        return this.prestiges;
    }
    
}
