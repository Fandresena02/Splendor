import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class defining a Player in the Splendor game.
 * @author Christine LI - Clement LUC - Fandresena RAKOTOMAHEFA
 * @date 16/12/2022
 * @file Player.java
 */

public class Player {
    /**
     * Fields of the Player class.
     * @param namePlayer Name of the player.
     * @param agePlayer Age of the player.
     * @param tokensAvailables tokens available in the player's deck.
     * @param playerCards List of cards that the player has purchased.
     * @param bonusAvailable Player bonuses.
     * @param reservedCards List of cards that the player can buy (keep on hand to buy later).
     * @param tiles List of nobles that the player owns.
     */
    private final String namePlayer;
    private final int agePlayer;
    private final LinkedHashMap<Token, Integer> tokensAvailables;
    private final ArrayList<DevCard> playerCards;
    private final HashMap<Token, Integer> bonusAvailable;
    private final ArrayList<DevCard> reservedCards;
    private final ArrayList<Tile> tiles;
    /**
     * Constructor method of the Player class.
     * @param namePlayer Name of the player.
     */
    public Player(String namePlayer, int agePlayer) {
        this.namePlayer = namePlayer;
        this.agePlayer = agePlayer;

        this.tokensAvailables = new LinkedHashMap<Token, Integer>();
        this.playerCards = new ArrayList<DevCard>();
        this.reservedCards = new ArrayList<DevCard>();
        this.tiles = new ArrayList<Tile>();
        this.bonusAvailable = new HashMap<Token, Integer>();

        Objects.requireNonNull(this.namePlayer);
        //le joueur peut ne plus avoir de jetons?
        //Objects.requireNonNull(this.tokensAvailables);
        Objects.requireNonNull(this.playerCards);
        Objects.requireNonNull(this.reservedCards);
        Objects.requireNonNull(this.tiles);
        this.initHashMapKeys(this.tokensAvailables);
        this.initHashMapKeys(this.bonusAvailable);
    }

    /**
     * Method rewriting equals for an Object of class Player.
     * @param other An object.
     * @return True, if the fields are the same, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        Objects.requireNonNull(other);

        return other instanceof Player p
                && this.agePlayer == p.agePlayer
                && this.namePlayer.equals(namePlayer)
                && this.tokensAvailables.equals(p.tokensAvailables)
                && this.playerCards.equals(p.playerCards)
                && this.reservedCards.equals(p.reservedCards)
                && this.tiles.equals(p.tiles);
    }

    /**
     * Method rewriting hashCode for an Object of the class Player.
     * @return Integer corresponding to the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.namePlayer, this.tokensAvailables,
                this.playerCards, this.reservedCards, this.tiles);
    }

    /**
     * Method to initialize the tokens in a HashMap.
     * @param map tokens
     * @return void
     */
    public void initHashMapKeys(HashMap<Token, Integer> map) {
        Objects.requireNonNull(map);

        for (int i = 0; i < 6; i++) {
            map.put(Token.values()[i], 0);
        }
    }

    /**
     * Method returning the string corresponding to the player's tokens (HashMap).
     * @return String of characters of all the tokens
     */
    public String toStringHashtoken() {
        StringBuilder strb = new StringBuilder();
        for (Token token : this.tokensAvailables.keySet()) {
            strb.append(token).append(":").append(this.tokensAvailables.get(token)).append("/ ");
        }
        return strb.toString();
    }

    /**
     * Method rewriting toString for an Object of the class Player.
     * @return String of a player.
     */
    @Override
    public String toString() {
        return "--------\nPlayer : " + this.namePlayer + "\n"
                + "Age : " + this.agePlayer + "\n"
                + "tokensAvailables :\n" + Shell.toStringTokenSmallFormat(this.tokensAvailables, "\n") + "\n"
                + "Player's Cards : " + this.playerCards + "\n"
                + "Reserved Cards : " + this.reservedCards + "\n--------\n";
    }

    /**
     * Method adding a number n of color token to the player.
     * @param token, The token to add.
     * @param n, Number of tokens to add.
     */
    public void addToken(Token token, int n) {
        Objects.requireNonNull(token);

        for (int i = 0; i < n; i++) {
            this.tokensAvailables.put(token, this.tokensAvailables.get(token) + 1);
        }
    }
    /**
     * Method adding the tile in the list of tiles
     * @param tile Tile to add in the list of tiles of the player.
     * @return void
     */
    public void addTile(Tile tile){
        Objects.requireNonNull(tile);
        this.tiles.add(tile);
        var value = this.tokensAvailables.get(Token.values()[5]);
        this.tokensAvailables.put(Token.values()[5], value + 1);
    }

    /**
     * Method to reserve a card.
     * @param card Card to add in the list of tiles of the player.
     * @return void
     */
    public void reserveCard(DevCard card) {
        Objects.requireNonNull(card);
        this.reservedCards.add(card);
    }

    /**
     * Method to buy a card.
     * @param card card to buy.
     * @return void
     */
    public void buyCard(DevCard card) {
        Objects.requireNonNull(card);
        this.playerCards.add(card);
        var key = Token.values()[card.getBonus()];
        this.bonusAvailable.put(key, this.bonusAvailable.get(key) + 1);
    }

    /**
     * Method to remove a card from the list of reserved card.
     * @param card card to remove.
     * @return void
     */
    public void removeReservedCard(DevCard card) {
        this.reservedCards.remove(card);
    }

    /**
     * Method to check if the player has the necessary resources to buy a card.
     * @param cardressources, The necessary tokens to buy the card.
     * @param map, the remaining resources to buy a card. (hashMap)
     * @return
     */
    private static Map<Token, Integer> canBuyCardWithObject(Map<Token, Integer> cardressources, Map<Token, Integer> map) {
        Objects.requireNonNull(cardressources);
        Objects.requireNonNull(map);

        HashMap<Token, Integer> tempcardressource = new HashMap<Token, Integer>(cardressources);
        for (Token token : tempcardressource.keySet()) {
            var value = tempcardressource.get(token);
            var res = value - map.get(token);
            tempcardressource.put(token, res < 0 ? 0 : res);
            //tempcard.getRessources().get(token);
        }
        return tempcardressource;
    }

    /**
     * Method of removing the player's tokens for the purchase of a card
     * @param cardressources, Resources to be removed from the player's game
     * @return void
     */
    public void removeAllTokenOfCard(Map<Token, Integer> cardressources) {
        Objects.requireNonNull(cardressources);

        /*on suppose que on peut acheter la carte*/
        for (Token token : cardressources.keySet()) {
            var temp = this.tokensAvailables.get(token);
            this.tokensAvailables.put(token, temp - cardressources.get(token));
        }
    }

    /**
     * Method that checks if the player has enough tokens to buy a card.
     * @param map, Resources to purcharse a card.
     * @return False if the player hasn't enough to pay, true otherwise.
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

    /**
     * Method of purchasing a card with bonuses and tokens of the player.
     * @param card card to buy.
     * @return True if the card had been purchase, false otherwise.
     */
    public Map<Token, Integer> leftToPay(DevCard card) {
        Objects.requireNonNull(card);

        var tempCardWithBonus = canBuyCardWithObject(card.getRessources(), this.bonusAvailable);
        System.out.println(tempCardWithBonus);
        if (enoughToPay(tempCardWithBonus)) {
            System.out.println("> Achat avec Seulement Bonus des Cartes");
            return null;
        }
        var tempCardWithBonustokens = canBuyCardWithObject(tempCardWithBonus, this.tokensAvailables);
        System.out.println(tempCardWithBonustokens);
        if (enoughToPay(tempCardWithBonustokens)) {
            System.out.println("> Achat avec Jetons et Bonus Si présents");
            return tempCardWithBonus;
        }
        return Map.of(Token.values()[0], -1);
    }

    /**
     * Getter method for an Object of type Player.
     * @param tileRessources Ressources of the tile.
     * @param bonusAvailable Bonuses of the player.
     * @return True if the player has enough to get the visit of the noble, false otherwise.
     */
    public boolean canVisitTile(Map<Token, Integer> tileRessources, HashMap<Token, Integer> bonusAvailable){
        Objects.requireNonNull(tileRessources);
        Objects.requireNonNull(bonusAvailable);

        if(Player.enoughToPay(Player.canBuyCardWithObject(tileRessources, bonusAvailable))){
            return true;
        }
        return false;
    }

    /**
     * Getter method for an Object of type Player.
     * @return Name of the player.
     */
    public String getNamePlayer(){
        return this.namePlayer;
    }

    /**
     * Getter method for an Object of type Player.
     * @return All tokens of the player.
     */
    public Map<Token, Integer> getTokensAvailables(){
        return this.tokensAvailables;
    }

    /**
     * Getter method for an Object of type Player.
     * @return Age of the player.
     */
    public int getAgePlayer() {
        return this.agePlayer;
    }

    /**
     * Getter method for an Object of type Player.
     * @return List of development cards of the player.
     */
    public List<DevCard> getPlayerCards() {
        return this.playerCards;
    }

    /**
     * Getter method for an Object of type Player.
     * @return List of reserved cards of the player.
     */
    public List<DevCard> getReservedCards() {
        return this.reservedCards;
    }

    /**
     * Getter method for an Object of type Player.
     * @return List of tiles of the player.
     */
    public List<Tile> getTiles() {
        return this.tiles;
    }

    /**
     * Getter method for an Object of type Player.
     * @return the player's bonuses.
     */
    public Map<Token, Integer> getBonusAvailable() {
        return this.bonusAvailable;
    }

    /**
     * Method counting the number of player's prestiges.
     * @return int, the total number of prestiges.
     */
    public int getAllPoints() {
        Integer points;
        int sum = 0;
        for (int i = 0; i < this.getPlayerCards().size(); i++) {
            points = this.getPlayerCards().get(i).getPrestiges();
            sum += points == null ? 0 : points;
        }
        for (int i = 0; i < this.getTiles().size();i++) {sum += this.getTiles().get(i).getPrestiges();}
        return sum;
    }
}



