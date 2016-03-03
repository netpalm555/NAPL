import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SyntaxChecker {

    File fileToCheck;

    public SyntaxChecker(String filepath) {
        fileToCheck = new File(filepath);
        if (!fileToCheck.toString().substring(fileToCheck.toString().lastIndexOf('.') + 1).equals("napl")) {
            System.out.println("Invalid file ending");
            System.exit(0);
        }
    }

    public void checkSyntax() {
        try {
            Scanner scan = new Scanner(fileToCheck);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
