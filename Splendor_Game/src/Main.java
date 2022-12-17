import java.io.IOException;
import java.util.List;
import java.util.Scanner;
/**
 * Class Main, which initializes the game by retrieving the player's answers entered on the command line.
 * @author Christine LI - Clement LUC - Fandresena RAKOTOMAHEFA
 * @date //2022
 * @file Main.java
 */
public class Main {

    
    public static void main(String[] args) throws IOException{
        
        Scanner scanProg = new Scanner(System.in);
        System.out.println("===========================");
        System.out.println("> Bienvenue Dans Splendor !");
        System.out.println("===========================");
        var mode = Import.inputStr(scanProg, List.of(Rules.phase1, Rules.phase2), "Choisissez un mode [" + Rules.phase1 + "/" + Rules.phase2 + "]");        
        String fileCards = mode.equals("phase1") ? "./files/cards_list_phase1.csv" : "./files/cards_list.csv";
        String fileTiles = mode.equals("phase1") ? null : "./files/nobles.csv";
        var nbPlayer = Import.inputInt(scanProg, "> Entrez le Nombre de Joueurs :", 3);
        Game mainGame = new Game(nbPlayer, mode, scanProg, fileCards, fileTiles);
        
        while (mainGame.getNbPlayers() < nbPlayer) {
            System.out.println("> Entrez le nom du joueur : ");
            var name = scanProg.next();
            var age = Import.inputInt(scanProg, "> Entrez l'age du joueur : ", 100);
            mainGame.addPlayerToGame(new Player(name, age));
        }
        mainGame.playGame(mode);
        //List<Tile> listNobles = Import.loadTiles(Path.of("nobles.csv"));
        //var listDevCard = Import.loadDevCardsByLevel(Path.of("./files/cards_list_phase1.csv"));
        //System.out.println(listDevCard);
        //var listDevCard = Import.loadCards(Path.of("card_list_phase1.csv"), DevCard::fromText, DevCard::convertTextToCard);
    }
}
