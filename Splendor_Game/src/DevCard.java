import java.util.Objects;
import java.util.List;
import java.util.Map;

public class DevCard{
    private final Integer prestiges;
    private final int level;
    private final int bonus;
    private final String illustration; 
    private final Map<Token, Integer> ressources;

    public DevCard(Integer prestiges, int level, String illustration, Map<Token, Integer> ressources, int bonus) {
        Objects.requireNonNull(ressources);

        this.prestiges = prestiges;
        this.level = level;
        this.ressources = ressources;
        this.bonus = bonus;
        this.illustration = illustration;
    }


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

    @Override
    public int hashCode() {
        return Objects.hash(this.prestiges, this.illustration, this.level, this.ressources, this.bonus);
    }

    @Override
    public String toString() {
        var prest = this.getPrestiges() == null ? "" : this.prestiges;
        var illustStr = this.illustration == null ? "" : this.illustration;
        return "\nL" + this.level + " P" + prest +" B" 
        + this.bonus + " ILL:" + illustStr + "\n"
        + Shell.toStringTokenSmallFormat(this.ressources,  "  ") 
        + "\n---------------------------------";
    }


    public Integer getPrestiges() {
        return this.prestiges;
    }

    public int getLevel() {
        return this.level;
    }


    public Map<Token, Integer> getRessources() {
        return this.ressources;
    }


    public int getBonus() {
        return this.bonus;
    }

    public String getIllustration() {
        return this.illustration;
    }
}
