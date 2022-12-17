import java.util.List;
import java.util.Map;

/**
 * Class allowing to manage the display of the colors in command line.
 * @author Christine LI - Clement LUC - Fandresena RAKOTOMAHEFA
 * @date 18/12/2022
 * @file 
 * Shell.java
 */
public class Shell {

    public static final String RESET = "\033[0m";  // Text Reset
    // Regular Colors
    public static final String ONYX = "\033[1;30m";   // ONYX
    public static final String RUBIS = "\033[0;31m";     // RUBIS
    public static final String EMERAUDE = "\033[0;32m";   // EMERAUDE
    public static final String OR = "\033[0;33m";  // OR
    public static final String SAPHIR = "\033[0;34m";    // SAPHIR
    public static final String DIAMANT = "\033[0;37m";   // DIAMANT

    
    public static final List<String> LISTCOLORSSHELL = List.of(EMERAUDE, DIAMANT, SAPHIR, ONYX, RUBIS, OR);
    public static final List<String> LISTCOLORSSTRING = List.of("EMERAUDE", "DIAMANT", "SAPHIR", "ONYX", "RUBIS", "OR");

     /**
     * Method displaying the tokens according to the number of each token for each color.
     * @return String : string of characters of the tokens 
     */
    public static String toStringTokenLongFormat(Map<Token, Integer> map, String separator) {
        StringBuilder strb = new StringBuilder();
        for (Token token : map.keySet()) {
            var res = token.ordinal();
            String string = new String("o").repeat(map.get(token));
            strb.append(Shell.LISTCOLORSSHELL.get(res) + token.name()).append(": ");
            strb.append(string).append(separator);
        }
        strb.append(Shell.RESET);
        return strb.toString();
    }
    /**
     * Method displaying the tokens according to each color.
     * @param map map of tokens.
     * @param separator separator between each tokens.
     * @return String 
     */
    public static String toStringTokenSmallFormat(Map<Token, Integer> map, String separator) {
        StringBuilder strb = new StringBuilder();
        for (Token token : map.keySet()) {
            var res = token.ordinal();
            strb.append(Shell.LISTCOLORSSHELL.get(res) + token.name()).append(": ");
            strb.append(map.get(token)).append(separator);
        }
        strb.append(Shell.RESET);
        return strb.toString();
    }

}
