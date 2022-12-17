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

/**
 * Class allowing to read a file and to extract information from it.
 * @author Adam GUEDDARI / Christine LI
 * @date 22/10/2021
 * @file Import.java
 */
public class Import {
    /**
     * Fields of the Import class.
     * @param BEGINFILECARDS 
     * @param BEGINFILETILES 
     */
    private final static int BEGINFILECARDS = 3;

    /**
     * Method that loads a type of cards.
     * @param <T>
     * @param <T>
     * @param path Path of the file.
     * @return List of the card of the file.
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
     * Method that loads the cards in the deck according to their level.
     * @param path Path of the file.
     * @return List of the card of the file.
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
     * Method that loads the game tiles.
     * @param path Path of the file.
     * @return List of the tile of the file.
     */
    public static List<Tile> loadTiles(Path path) throws IOException {
        Objects.requireNonNull(path);

        var result = loadCards(path, Tile::fromText, Tile::isRefLine);
        Collections.shuffle(result);
        return result;
    }
    
    /**
     * Method that assigns to each element of the list an index, starting at 0
     * @param lst List to modify
     * @return Map of the element from the list
     */
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
     * Method that imports the resources from the file. 
     * @param text The text to import from the file.
     * @param debColonne The column from which you start to browse the values
     * @return The resources of the file 
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
     * Method that checks if the text entered is a String and the conditions of the method.
     * @param scan Scanner
     * @param listToCompare list to compare
     * @param textToPrint String to print
     * @return String
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
     * @param scan Scanner
     * @param textToPrint Text to print
     * @param defaultCompare Integer 
     * @return Integer
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
     * Method verify if the string can be converted to an integer
     * @param scan Scanner
     * @return Integer
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