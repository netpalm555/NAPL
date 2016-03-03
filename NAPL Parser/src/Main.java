import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main (String[] args) {
        List<String> argsList = Arrays.asList(args);
        Iterator cliInputs = argsList.iterator();
        if (cliInputs.hasNext()) {
            switch ((String) cliInputs.next()) {
                case "-h":
                case "--help":
                    System.out.println("This will be the help file for the NAPL parser.");
                    break;
                case "--syntaxcheck":
                    if (cliInputs.hasNext()) {
                        SyntaxChecker checker = new SyntaxChecker((String) cliInputs.next());
                        checker.checkSyntax();
                    } else {
                        System.out.println("Please provide a file to check for syntax");
                    }
                    break;
                default:
                    System.out.println("Error: Please see -h for valid options");
                    break;
            }
        }
    }

}
