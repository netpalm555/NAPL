import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SyntaxChecker {

    File fileToCheck;

    public SyntaxChecker(String filepath) {
        fileToCheck = new File(filepath);
        if (!fileToCheck.toString().substring(fileToCheck.toString().lastIndexOf('.') + 1).equals("napl")) {
            System.out.println(filepath);
            System.out.println("Invalid file ending");
            System.exit(1);
        }
    }

    public void checkSyntax() {
        try (Scanner scan = new Scanner(fileToCheck)) {
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

    private boolean checkVariable(FuncType type, Iterator lineIterator) {
        System.out.println("Type is " + type.toString());
        if (lineIterator.hasNext()) {
            String varName = (String) lineIterator.next();
            if (!varName.matches("")) return false;
            System.out.println("Variable name: " + lineIterator.next());
            if (lineIterator.hasNext()) {
                
            }
        }
        return true;
    }

}

enum FuncType{
    DYN, VAR
}
