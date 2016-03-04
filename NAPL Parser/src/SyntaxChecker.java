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
                line = Arrays.asList(scan.nextLine().split(" ", 0));
                lineIterator = line.iterator();
                while (lineIterator.hasNext()) {
                    System.out.println(lineIterator.next());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
