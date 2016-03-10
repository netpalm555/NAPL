import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SyntaxChecker {

    File fileToCheck;
    int lineNum;

    public SyntaxChecker(String filepath) {
        fileToCheck = new File(filepath);
        if (!fileToCheck.toString().substring(fileToCheck.toString().lastIndexOf('.') + 1).equals("napl")) {
            System.out.println(filepath);
            System.out.println("Invalid file ending");
            System.exit(1);
        }
        lineNum = 0;
    }

    public void checkSyntax() {
        try (Scanner scan = new Scanner(fileToCheck)) {
            lineNum = 1;
            scan.useDelimiter(" ");
            List<String> line;
            Iterator lineIterator;
            while (scan.hasNext()) {
                switch (scan.next()) {
                    case "var":
                        checkVariable(FuncType.VAR, scan);
                        break;
                    case "dyn":
                        checkVariable(FuncType.DYN, scan);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkVariable(FuncType type, Iterator lineIterator) {
        System.out.println("Type is " + type.toString());
        if (lineIterator.hasNext()) {
            String varName = (String) lineIterator.next();
            String[] varNameSplit = varName.split("=");
            if (varNameSplit.length > 1) {
                varName = varNameSplit[0];
            }
            System.out.println("Variable name: " + varName);
            if (!varName.matches("^[a-zA-z0-9]+$")) {
                System.out.println("Error on line: " + lineNum);
                System.out.println("Invalid variable name: " + varName);
            }
            if (lineIterator.hasNext()) {
                
            }
        }
    }

}

enum FuncType{
    DYN, VAR
}
