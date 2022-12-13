import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class Import {

    private final static int BEGINFILECARDS = 3;

    /**
     * Methode permettant de charger les cartes de developpement
     */
    public static  <T> List<T> loadCards(Path path, BiFunction<String, String, T> function, BiPredicate<String, String> function2) throws IOException {
        Objects.requireNonNull(path);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            T tmp;
            var cmpt = 1;
            var listCards = new ArrayList<T>();
            String tmpStr = new String();
            String refline = "";
            for (tmpStr = reader.readLine(); tmpStr != null; tmpStr = reader.readLine()) {
                if (cmpt >= BEGINFILECARDS){
                    if (/*Card.convertTextToCard(tmpStr, ",")*/function2.test(tmpStr, ",")) {
                        refline = tmpStr;
                    }
                    //System.out.println(tmpStr);
                    tmp = function.apply(tmpStr, refline);//Card.fromText(tmpStr, ",", refline);
                    //System.out.print(tmp);
                    listCards.add(tmp);
                    // add card to a list
                }
                cmpt++;
            }
            return listCards;
        }
    }

    /**
     * Methode permettant de charger les cartes de développement sur le plateau de jeu en fonction de leur niveau de jeu
     */
    public static Map<Integer, ArrayList<DevCard>> loadDevCardsByLevel(Path path) throws IOException {
        Objects.requireNonNull(path);

        var result = loadCards(path, DevCard::fromText, DevCard::isRefLine);
        Collections.shuffle(result);
        var mapLevel = new HashMap<Integer, ArrayList<DevCard>>();
        result.forEach(card -> mapLevel.computeIfAbsent(card.getLevel(), k -> new ArrayList<DevCard>()).add(card));
        //System.out.println(mapLevel);
        return mapLevel;
    }

    /**
     * Methode permettant de charger les tuiles de jeu sur le plateau de jeu
     */
    public static List<Tile> loadTiles(Path path) throws IOException {
        Objects.requireNonNull(path);

        var result = loadCards(path, Tile::fromText, Tile::isRefLine);
        Collections.shuffle(result);
        return result;
    }


    public static <T> Map<T, Integer> colorHash(List<T>lst) {
        Objects.requireNonNull(lst);

        int i;
        var hash = new HashMap<T, Integer>();
        var color = lst;
        //List.of('g', 'w', 'u', 'k', 'r');
        for (i = 0; i < color.size(); i++) {
            hash.put(color.get(i), i);
        }
        return hash;
    }

    /**
     * Methode qui récupère la valeur des ressources pour chaque carte de jeu du fichier
     */
    public static Map<Token, Integer> importRessources(String[] text, int debColonne) {
        Objects.requireNonNull(text);

        int index;
        var ressources = new HashMap<Token, Integer>();
        var order = List.of('w', 'u', 'g', 'r', 'k');
        var colorPrice = Import.colorHash(List.of('g', 'w', 'u', 'k', 'r'));
        for (index = debColonne; index < text.length; index++) {
            if (!text[index].equals("")) {
                var key = colorPrice.get(order.get(index - debColonne));
                ressources.put(Token.values()[key], Integer.parseInt(text[index]));
            }
        }
        return ressources;
    }

    /**
     * Methode permettant de verifier si le texte entré est bien un String et s'il valide bien les conditions de la methode
     */
    public static String inputStr(Scanner scan, List<String> listToCompare, String textToPrint) {
        Objects.requireNonNull(scan);
        Objects.requireNonNull(textToPrint);
        System.out.println(textToPrint);
        String validStr = scan.next();
        while(validStr == null || !listToCompare.contains(validStr)) {
            System.out.println("Mauvaise saisie");
            validStr = scan.next();
        }
        return validStr;
    }

    /**
     * Method that checks if the text entered is an integer and the conditions of the method.
     * Methode permettant de verifier si le texte entré est bien un integer et s'il valide bien les conditions de la methode
     */
    public static Integer inputInt(Scanner scan, String textToPrint, int defaultCompare) {
        Objects.requireNonNull(scan);
        Objects.requireNonNull(textToPrint);
        System.out.println(textToPrint);
        Integer valid = Import.tryParse(scan);
        while (valid == null || valid > defaultCompare) {
            System.out.println("Mauvaise saisie");
            valid = Import.tryParse(scan);
        }
        return valid;
    }

    /**
     * Methode permettant de verifier si une chaine de caractère peut etre transformé en integer
     */
    public static Integer tryParse(Scanner scan) {
        Objects.requireNonNull(scan);
        try {
            return Integer.parseInt(scan.next());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
}