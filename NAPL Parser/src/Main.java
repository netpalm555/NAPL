import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main (String[] args) {
        List<String> argsList = Arrays.asList(args);
//        argsList.forEach(System.out::println);
        Iterator cliInputs = argsList.iterator();
        if (cliInputs.hasNext()) {
            switch ((String) cliInputs.next()) {
                case "-h":
                case "--help":
                    Class cls;
                    try {
                        cls = Class.forName("Main");
                        ClassLoader cloader = cls.getClassLoader();
                        URL helpURL = cloader.getResource("resc/help.txt");
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(helpURL.openStream()))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "--lex":
                    if (cliInputs.hasNext()) {
                        Lexer lex = new Lexer((String) cliInputs.next());
                        lex.lex();
                    } else {
                        System.out.println("Please provide a file to lex");
                    }
                    break;
                case "--checksyntax":
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