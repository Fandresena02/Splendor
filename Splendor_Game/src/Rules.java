import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rules {
    private final int pointsForVictory = 15;
    private final int maxPlayers = 3;

    public final static String phase1 = "Basic";
    public final static String phase2 = "Complet"; 
    
    private final int nbLevel;
    private final int nbCardByLevel;
    private final int nbTokens;
    private final String mode;


    private final ArrayList<String> listActions =   new ArrayList<String>(
        List.of("sametokens", "difftokens", "buycard", 
        "cards", "tokens", "reservecard"));
    private final ArrayList<String> listDescriptionActions = new ArrayList<String>(
        List.of("Ajout de 2 Mêmes jetons", "Ajout de 3 jetons Différents", "Achat d'une Carte", 
        "Affichage de mes cartes", "Affichage de mes Jetons", "Reserve d'une Carte")); 


    public Rules(int nbLevel, int nbCardByLevel, int nbTokens, String mode) {
        Objects.requireNonNull(mode);

        this.nbLevel = nbLevel;
        this.nbCardByLevel = nbCardByLevel;
        this.nbTokens = nbTokens;
        this.mode = mode;
    }
    /**
     * Method to guide the player in his choice of action during the game
     * Methode permettant de guider le joueur dans son choix d'action au cours du jeu
     */
    public String helpUser() {
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < listActions.size(); i++) {
            strb.append(" * ").append(listActions.get(i)).append(" : ");
            strb.append(listDescriptionActions.get(i)).append("\n");
        }
        return strb.toString();
    }

    /**
     * Methode permettant de retirer une action du jeu
     */
    public void removeActions(String actionToRemove) {
        var index = this.listActions.indexOf(actionToRemove);
        if (index == -1) {
            throw new IllegalArgumentException("actionToremove is not present in list");
        }
        this.listActions.remove(actionToRemove);
        this.listDescriptionActions.remove(index);
    }

    /**
     * Methode qui initialise les regles du jeu
     */
    public static Rules initRulesFromString(String mode) {
        if (mode.equals(phase1)) {
            var ruleGame = new Rules(1, 4, 5, Rules.phase1); 
            ruleGame.removeActions("reservecard");
            return ruleGame;
        }
        else if (mode.equals(phase2)) {
            return new Rules(3, 4, 6, Rules.phase2);
        }
        throw new IllegalArgumentException("mode not accepted");
    }

    @Override
    public boolean equals(Object other) {
        Objects.requireNonNull(other);
        return other instanceof Rules r
        && this.nbLevel == r.nbLevel
        && this.nbTokens == r.nbTokens
        && this.nbCardByLevel == r.nbCardByLevel
        && this.pointsForVictory == r.pointsForVictory
        && this.maxPlayers == r.maxPlayers
        && this.mode.equals(r.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nbLevel, this.nbTokens, this.pointsForVictory, this.nbCardByLevel, this.mode, this.maxPlayers);
    }

    public int getNbLevel() {
        return this.nbLevel;
    }

    public int getNbCardsByLevel() {
        return this.nbCardByLevel;
    }

    public int getNbTokens() {
        return this.nbTokens;
    }

    public String getMode() {
        return this.mode;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }
    public List<String> getListActions() {
        return this.listActions;
    }
}
