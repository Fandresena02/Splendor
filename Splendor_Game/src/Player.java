import java.util.Objects;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Player {

    //tokens valables (de la pile de jeu)
    //tokens bonus
    private final String namePlayer;
    private final int agePlayer;
    private final ArrayList<DevCard> playerCards;
    private final ArrayList<DevCard> reservedCards;
    private final ArrayList<Tile> tiles;
    /**
     * Constructor method of the Player class.
     * @param namePlayer Name of the player.
     */
    public Player(String namePlayer, int agePlayer) {
        this.namePlayer = namePlayer;
        this.agePlayer = agePlayer;

        this.playerCards = new ArrayList<DevCard>();
        this.reservedCards = new ArrayList<DevCard>();
        this.tiles = new ArrayList<Tile>();
        
        Objects.requireNonNull(this.namePlayer);
        //le joueur peut ne plus avoir de jetons? 
        //Objects.requireNonNull(this.tokensAvailables);
        Objects.requireNonNull(this.playerCards);
        Objects.requireNonNull(this.reservedCards);
        Objects.requireNonNull(this.tiles);
    }


    @Override
    public boolean equals(Object other) {
        Objects.requireNonNull(other);
        
        return other instanceof Player p
        && this.agePlayer == p.agePlayer
        && this.namePlayer.equals(namePlayer)
        && this.playerCards.equals(p.playerCards)
        && this.reservedCards.equals(p.reservedCards)
        && this.tiles.equals(p.tiles);    
    }

    /**
     * Methode permettant d'initialiser les tokens dans une hashmap
     */
    public void initHashMapKeys(HashMap<Token, Integer> map) {
        Objects.requireNonNull(map);

        for (int i = 0; i < 6; i++) {
            map.put(Token.values()[i], 0);
        }
    }


    @Override
    public String toString() {
        return "--------\nPlayer : " + this.namePlayer + "\n" 
        + "Age : " + this.agePlayer + "\n" 
        + "tokensAvailables :\n" + "\n"
        + "Player's Cards : " + this.playerCards + "\n"
        + "Reserved Cards : " + this.reservedCards + "\n--------\n";
    }


    /**
     * Methode permettant d'ajouter des tuiles a la liste des tuiles du jeu
     */
    public void addTile(Tile tile){
        Objects.requireNonNull(tile);
    }

    /**
     * Methode permettant de reserver une card du jeu (donc ajouter sur carte a la liste des cartes reservé du joueur)
     */
    public void reserveCard(DevCard card) {
        Objects.requireNonNull(card);
        this.reservedCards.add(card);
    }

    /**
     * Methode pour acheter une carte
     */
    public void buyCard(DevCard card) {
        Objects.requireNonNull(card);
        this.playerCards.add(card);
        var key = Token.values()[card.getBonus()];
        //ajout des bonus de tokens
    }

    /**
     * Methode pour retirer une carte de la liste des cartes reservées
     */
    public void removeReservedCard(DevCard card) {
        this.reservedCards.remove(card);
    }

    /**
     * Methode permettant de verifier si un joueur possède les ressources necessaires pour acheter une carte
     * A COMPLETER
     */
    /*
    private static Map<Token, Integer> canBuyCardWithObject(Map<Token, Integer> cardressources, Map<Token, Integer> map) {
        ;
    }
    */

    /**
     * Methode perrmettant de retirer les jetons du joueur necessaire afin d'acheter une carte
     * A COMPLETER
     */
    public void removeAllTokenOfCard(Map<Token, Integer> cardressources) {
        Objects.requireNonNull(cardressources);
    }

    /**
     * Methode permettant de verifier si le joueur a les jetons necessaires pour achater une carte
     */
    private static boolean enoughToPay(Map<Token,Integer> map) {
        Objects.requireNonNull(map);
        
        for (Token token : map.keySet()) {
            if (map.get(token) > 0) {
                return false;
            }
        }
        return true;
    }

    public String getNamePlayer(){
        return this.namePlayer;
    }

    public int getAgePlayer() {
        return this.agePlayer;
    }
    public List<DevCard> getPlayerCards() {
        return this.playerCards;
    }
    public List<DevCard> getReservedCards() {
        return this.reservedCards;
    }

    public List<Tile> getTiles() {
        return this.tiles;
    }


}

