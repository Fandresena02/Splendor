import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class Game, manage game cases.
 *
 * @author Christine LI - Clement LUC - Fandresena RAKOTOMAHEFA
 * @date 18/12/2022
 * @file Game.java
 */
public class Game {
    /**
     * Field of the Game Class.
     * @param MAXPLAYERS Number maximum of player.
     * @param VICTORY Number minimal of points to win the game.
     * @param PRINTMYTOKENS Writing tokens.
     * @param PRINTMYCARDS Writing cards.
     * 
     * @param ACTIONSAMETOKEN Writing of the action buying two same tokens.
     * @param ACTIONDIFFTOKEN Writing of the action buying three different tokens.
     * @param ACTIONBUYCARD Writing of the action buying a card.
     * @param ACTIONRESERVECARD Writing of the action buying a card from the list of reserved cards.
     * 
     * @param nbPlayers Number of players.
     * @param players List of all the players of the game.
     * @param tokensStack The stack of tokens. 
     * @param discoveryCards Discovered cards on the game board.
     * @param cardStack Stack of cards on the game board.
     * @param discoveryTiles Discovered tiles on the game board.
     * @param scan Scanner.
     */
    private final static int MAXPLAYERS = 4;
    private final static int VICTORY = 15;
    private final static String PRINTMYTOKENS = "tokens";
    private final static String PRINTMYCARDS = "cards";

    private final static String ACTIONSAMECOUNTER = "sametokens";
    private final static String ACTIONDIFFCOUNTER = "difftokens";
    private final static String ACTIONBUYCARD = "buycard";
    private final static String ACTIONRESERVECARD = "reservecard";

     private final Rules rules;
    private final int nbPlayers;
    private final ArrayList<Player> players;
    private final LinkedHashMap<Token, Integer> countersStack;
    private final HashMap<Integer, ArrayList<DevCard>> discoveryCards;
    private final HashMap<Integer, ArrayList<DevCard>> cardStack;
    private final ArrayList<Tile> discoveryTiles;
    //private final ArrayList<Tile> tilesStack;
    private final Scanner scan;

    /**
     * Constructor method of the Game class.
     * @param nbPlayers Number of players.
     * @param mode mode of the game
     * @param scan Scanner.
     * @param fileCardsPath path of file for cards
     * @param fileTiles path of file for tiles.
     */
    public Game(int nbPlayers, String mode, Scanner scan, String fileCardsPath, String fileTilesPath) throws IOException{
        Objects.requireNonNull(scan);
        Objects.requireNonNull(mode);
        Objects.requireNonNull(fileCardsPath);
        //Objects.requireNonNull(fileTilesPath);
        this.rules = Rules.initRulesFromString(mode);

        if (nbPlayers > MAXPLAYERS || nbPlayers < 2) {
            throw new IllegalArgumentException("Game Playable only with " + MAXPLAYERS + " Players And Minimum 2 Players");
        }
        var tempTiles = mode.equals(Rules.phase1) ? null : Import.loadTiles(Path.of(fileTilesPath));
        var tempCards = Import.loadDevCardsByLevel(Path.of(fileCardsPath));

        this.nbPlayers = nbPlayers;
        this.players = new ArrayList<Player>();
        this.countersStack = new LinkedHashMap<Token, Integer>();
        this.discoveryCards = new HashMap<Integer, ArrayList<DevCard>>();
        this.discoveryTiles = mode.equals(Rules.phase1) ? null : new ArrayList<Tile>(tempTiles.subList(0, nbPlayers + 1));
        //this.tilesStack =mode.equals(Rules.phase1) ? null : new ArrayList<Tile>(tempTiles.subList(nbPlayers + 1, tempTiles.size()));
        this.cardStack = new HashMap<Integer, ArrayList<DevCard>>(tempCards);
        this.scan = scan;
        //import tuiles nobles aussi
        //System.out.println(this.cardStack);
        //System.out.println(this.cardStack);
        
        initHashMapKeys(this.countersStack);
    }
    /**
     * Method to initialize the tokens in a HashMap 
     * @param map Map of tokens 
     * @return void
     */
    public void initHashMapKeys(HashMap<Token, Integer> map) {
        Objects.requireNonNull(map);
        for (int i = 0; i < 6; i++) {
            map.put(Token.values()[i], 0);
        }
    }
    /**
     * Method to sort a card according to its level
     * @param level Level of the card to sort
     * @param nbCardToExtract The maximum number of cards to be sorted 
     * @return void
     */
    public void drawCardFromStack(int level, int nbCardToExtract) {
        var iter = this.cardStack.get(level).iterator();
        for (int i = 0; i < nbCardToExtract + 1; i++) {
            DevCard card = iter.next();
            var lst = this.discoveryCards.putIfAbsent(level, new ArrayList<DevCard>());
            if (lst != null) {lst.add(card);} 
            iter.remove();
        }
    }

    /**
     * Method to add a player in the list of all the players of the game.
     * @param player Player to add in the list.
     * @return void
     */
    public void addPlayerToGame(Player player) {
        Objects.requireNonNull(player);
        if (this.players.size() > this.nbPlayers) {
            throw new IndexOutOfBoundsException("Number for Players is maximum"); 
        }
        this.players.add(player);
    }
    /**
     * Method to add n tokens in the stack of tokens of the game.
     * @param token Token(s) to add in the stack.
     * @param n Number of tokens to add in the stack.
     * @return void
     */
    public void addTokensToGame(Token counter, int n) {
        Objects.requireNonNull(counter);
        this.countersStack.put(counter, this.countersStack.get(counter) + n);
    }
    /**
     * Method to remove n tokens in the stack of tokens of the game. 
     * @param token Token(s) to remove from the stack of tokens.
     * @param n Number of tokens to remove from the stack.
     * @return void
     */
    public void removeCountersFromGame(Token counter, int n) {
        Objects.requireNonNull(counter);
        this.countersStack.put(counter, this.countersStack.get(counter) - n);
    }
    /**
     * Method for counting and comparing a number of tokens according to a given maximum.
     * @param player A player of the game.
     * @param defaultValue Default number of tokens.
     * @return True, if the player has more or same number of defaultValue of tokens, false otherwise.
     */
    private boolean verifyNumberCounters(Player player, int defaultValue){
        Objects.requireNonNull(player);
        int sum = 0;
        for (Token counter : player.getTokensAvailables().keySet()){
            sum += player.getTokensAvailables().get(counter);
        }
        return sum < defaultValue;
    }
    /**
     * Method verify if the player can win according to his number of points.
     * @param player A player of the game.
     * @return True, if the player can win, false otherwise.
     */
    public boolean victory(Player player) {
        Objects.requireNonNull(player);
        if (player.getAllPoints() >= VICTORY) {
            return true;
        };
        return false;
    }
    /**
     * Method that gives the winner of the game.
     * @return The winner.
     */
    public Player winner() {
        Player winnerOfGame = this.players.get(0);
        for (int i = 0; i < this.players.size(); i++){
            var playerActual = this.players.get(i); 
            if (victory(playerActual)) {
                if (playerActual.getPlayerCards().size() < winnerOfGame.getPlayerCards().size()) {
                    winnerOfGame = playerActual;
                }
            }
        }
        return winnerOfGame;
    }

    /**
     * Method to manage the action of taking two same tokens from the deck of the game.
     * @param player A player.
     * @return True, if the player has took the tokens from the stack of the game, false otherwise.
     */
    public boolean actionSameCounters(Player player) {
        Objects.requireNonNull(player);

        if (!verifyNumberCounters(player, 10)) {
            System.out.println("> Vous avez plus de 10 jetons deja !"); 
            return false;
        }
        var userChoice = Import.inputStr(this.scan, Shell.LISTCOLORSSTRING,"Vous avez choisi de prendre 2 jetons de la même couleur : Veuillez indiquer la couleur : ");
        if (this.countersStack.get(Token.values()[Shell.LISTCOLORSSTRING.indexOf(userChoice)]) < 3) {
            System.out.println("Il n y a pas assez de jetons dans la pile"); 
            return false;
        }
        player.addToken(Token.values()[Shell.LISTCOLORSSTRING.indexOf(userChoice)], 2);
        this.removeCountersFromGame(Token.values()[Shell.LISTCOLORSSTRING.indexOf(userChoice)], 2);
        return true;
    }

    /**
     * Method to manage the action of taking three different tokens from the deck of the game.
     * @param player A player.
     * @return True, if the player has took the tokens from the stack of the game, false otherwise.
     */
    public boolean actionDiffCounters(Player player, int nbCounters) {
        Objects.requireNonNull(player);

        if (!verifyNumberCounters(player, 10)) {
            System.out.println("> Vous avez plus de 10 jetons deja !"); 
            return false;
        }
        boolean noProblem = true;
        var temp = new ArrayList<Token>();
        for (int i = 0; i < nbCounters; i++) {
            var userChoice = Import.inputStr(this.scan, Shell.LISTCOLORSSTRING,"> Vous avez choisi de prendre " + (nbCounters - i) + " jetons différents : Veuillez indiquer la couleur: ");
            if (this.countersStack.get(Token.values()[Shell.LISTCOLORSSTRING.indexOf(userChoice)]) < 1 || temp.contains(Token.values()[Shell.LISTCOLORSSTRING.indexOf(userChoice)])) {
                System.out.println("> Il n y a pas assez de jetons dans la pile\n> Veuillez mettre 3 jetons différents"); 
                noProblem = false; 
                break;
            }
            temp.add(Token.values()[Shell.LISTCOLORSSTRING.indexOf(userChoice)]);
        }
        if (noProblem) {
            temp.forEach(counter -> player.addToken(counter, 1));
            temp.forEach(counter -> this.removeCountersFromGame(counter, 1));
        }
        return noProblem;
    }
    /**
     * Method to manage the action of buying a development card from the discovery cards of the game or from the list of reserved cards of the player.
     * @param player A player.
     * @return True, if the player has buy the card, false otherwise.
     */
    public boolean actionBuyCard(Player player) {
        Objects.requireNonNull(player);
        if (this.actionBuyReserveCard(player)) {
            return true;
        }
        System.out.println("> Dans les cartes Découvertes");
        var userChoice = Import.inputInt(this.scan, "> Quelle Carte voulez vous acheter Niveau [1/2/3] :", 3);
        int numberCard = Import.inputInt(this.scan, "Choisissez quelle carte de niveau " + userChoice + " vous voulez", this.discoveryCards.get(userChoice).size());
        var cardUser = this.discoveryCards.get(userChoice).get(numberCard - 1);
        var payCard = player.leftToPay(cardUser);
        if (payCard == null || !payCard.equals(Map.of(Token.values()[0], -1))) {
            // la carte peut deja etre payé avec les bonus ou jetons
            player.buyCard(cardUser);
            this.discoveryCards.get(cardUser.getLevel()).remove(cardUser);            
            this.drawCardFromStack(cardUser.getLevel(), 1);
            if (payCard != null){
                //la carte doit etre payé avec un reste de jetons
                player.removeAllTokenOfCard(payCard);
                payCard.forEach((key, value) -> this.countersStack.put(key, value + this.countersStack.get(key)));
            }
            System.out.println("> Vous avez acheté la carte " + cardUser);
            return true;
        }
        else {
            // la carte ne peut pas etre payé ni avec les bonus ni avec les jetons du joueur
            System.out.println("> Vous n'avez pas assez de ressources pour acheter cette carte");
            return false;
        }
    }
    /**
     * Method to manage the action of buying a reserved card from the list of the player.
     * @param player A player.
     * @return True, if the card has been buy by the player, false otherwise.
     */
    public boolean actionBuyReserveCard(Player player) {
        Objects.requireNonNull(player);

        var userChoice = Import.inputStr(this.scan, List.of("oui", "non"), "Voulez vous acheter une carte réservé" + List.of("oui", "non"));
        if (userChoice.equals("non") || player.getReservedCards().size() == 0) {
            System.out.println("> Quit");
            return false;
        }
        int numberCard = Import.inputInt(this.scan, "Choisissez quel carte réservé vous voulez acheter [1-" + player.getReservedCards().size() +"]", player.getReservedCards().size());
        var card = player.getReservedCards().get(numberCard - 1);
        var payCard = player.leftToPay(card); 
        if (payCard == null || !payCard.equals(Map.of(Token.values()[0], -1))) {
            player.buyCard(card);
            player.removeReservedCard(card);
            if (payCard != null){
                //la carte doit etre payé avec un reste de jetons
                player.removeAllTokenOfCard(payCard);
                payCard.forEach((key, value) -> this.countersStack.put(key, value + this.countersStack.get(key)));
            }
            return true;
        }
        else {
            System.out.println("> Vous n'avez pas assez de ressources pour acheter cette carte");
            return false;
        }
    }
    /**
     * Method to manage the action of reserved a card from the discovery cards of the game or from the stack of cards.
     * @param player A player.
     * @return True, if the player has reserved the card, false otherwise.
     */
    public boolean actionReserveCard(Player player) {
        Objects.requireNonNull(player);

        if (player.getReservedCards().size() >= 3) {
            System.out.println("> Trop de cartes réservés");
            return false;
        }
        DevCard cardUser;
        var userChoice = Import.inputStr(this.scan, List.of("stack", "discovery"), "choisissez une carte dans la pioche ou des cartes visibles" + List.of("stack", "discovery"));
        var level = Import.inputInt(this.scan, "> Choisissez quel carte de niveau vous voulez : [1/2/3]", 4);
        if (userChoice.equals("discovery")) {
            var numberCard = Import.inputInt(this.scan, "Choisissez quel carte de niveau " + level + " vous voulez", this.discoveryCards.get(level).size());
            cardUser = this.discoveryCards.get(level).get(numberCard - 1);
            player.reserveCard(cardUser);
            this.discoveryCards.get(level).remove(cardUser);
            this.drawCardFromStack(cardUser.getLevel(), 1);
        }
        else {
            cardUser = this.cardStack.get(level).get(0);
            player.reserveCard(cardUser);
            this.cardStack.get(level).remove(cardUser);
        }
        System.out.println("> Vous avez réservé la carte " + cardUser);
        return true;
    }


    /**
     * Method to manage a game turn 
     * @param player A player.
     * @param allActionsAvailables List of all the actions that the player can choose.
     * @return void
     */
    public void playRound(Player player) {
        Objects.requireNonNull(player);

        boolean noPlay = false;
        while (!noPlay) {
            System.out.println("> Que voulez vous faire :");
            System.out.println(this.rules.helpUser());
            //var userChoice = Import.inputStr(this.scan, new String());
            var userChoice = Import.inputStr(this.scan, this.rules.getListActions(), "> Choisissez : ");
            switch (userChoice) {
                case ACTIONSAMECOUNTER: 
                    noPlay = this.actionSameCounters(player); break;
                case ACTIONDIFFCOUNTER: 
                    noPlay = this.actionDiffCounters(player, 3); break;
                case ACTIONBUYCARD:   
                    noPlay = this.actionBuyCard(player); break;
                case ACTIONRESERVECARD:   
                    noPlay = this.actionReserveCard(player);break;
                case PRINTMYCARDS:      
                    System.out.println(player.getPlayerCards()); break;
                case PRINTMYTOKENS:   
                    System.out.println(Shell.toStringTokenLongFormat(player.getTokensAvailables(), "\n")); break;
                default:
                    break;
            }
        }
    }
    /**
     * Method to initialize the game board
     * @param nbLevel A level.
     * @param nbCardByLevel The number of card for each level.
     * @param nbTokens The number of token.
     * @return void
     */
    public void initGame(int nbLevel, int nbCardByLevel, int nbTokens) {
        int i;
        for (i = 0; i < nbTokens; i++) 
            {this.addTokensToGame(Token.values()[i], 2 + this.nbPlayers);}
        for (i = 0; i < nbLevel; i++) 
            {this.drawCardFromStack(i + 1, nbCardByLevel);
        }
    }
    /**
     * Method to manage a game.
     * @param phaseChoice The choice of the game phase (phase 1 will be played with 2 players)
     * @return void
     */
    public void playGame(String phaseChoice) {

        if (this.players.size() < this.nbPlayers) {
            throw new IllegalAccessError("Missing Players to Play a Game");
        }
        initGame(this.rules.getNbLevel(), this.rules.getNbCardsByLevel(), this.rules.getNbTokens());
        boolean playing = true;        
        while (playing) {
            for (int i = 0; i < this.players.size(); i++) {
                var playerActual = this.players.get(i);
                if (this.victory(playerActual)) {playing = false;} 
                System.out.println(this);
                System.out.println(playerActual);
                this.playRound(playerActual);
            }
        }
        winner();
        System.out.println(this.classement(this.players));
    }
    /**
     * Method for ranking players at the end of the game.
     * @param players The list of the players of the game.
     * @return The classement of the game.
     */
    public String classement(ArrayList<Player> players) {
        Stream <String> streamClassement = players.stream()
            .sorted(Comparator.comparingInt(Player::getAllPoints)
            .reversed()).map(player -> "> Joueur : " + player.getNamePlayer() + " : " + player.getAllPoints());
        var result = streamClassement.collect(Collectors.joining("\n"));  
        return "==============\n> Classement\n==============\n" + result;
    }
    /**
     * Method rewriting equals for an Object of the class Game
     * @param other object quelconcque.
     * @return vrai, si les champs sont les mêmes, sinon faux 
     */
    @Override 
    public boolean equals(Object o){
        Objects.requireNonNull(o);

        return o instanceof Game g
        && this.countersStack.equals(g.countersStack)
        && this.discoveryCards.equals(g.discoveryCards);
    }

    /**
     * Method rewriting hashCode for an Object of the class Game
     * @return entier correspondant à l'objet.
     */
    @Override 
    public int hashCode(){
        return Objects.hash(this.countersStack, this.discoveryCards);
    }
    /**
     * Method rewriting toString for an Object of the class Game
     * @return chaine de caractère associés à l'affichage
     */
    @Override 
    public String toString(){
        return "----------- On the board of the game -----------\n"
        + "Tiles from the deck :\n" + (this.discoveryTiles == null ? "" : this.discoveryTiles) + "\n"
        + "Cards from the deck : " + this.discoveryCards + "\n"
        + "Counters from the deck :\n" + Shell.toStringTokenLongFormat(this.countersStack, "\n");
    }
    /**
     * Getter method for an Object of type Game.
     * @return Number of players.
     */
    public int getNbPlayers() {
        return this.players.size();
    }
}
