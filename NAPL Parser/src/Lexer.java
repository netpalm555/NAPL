public class Lexer {
    
    File file;
    
    public Lexer(String filePath) {
        file = new File(filePath);
        if (!fileToCheck.toString().substring(fileToCheck.toString().lastIndexOf('.') + 1).equals("napl")) {
            System.out.println(filepath);
            System.out.println("Invalid file ending");
            System.exit(1);
        }
    }
    
}

public enum Token {
    KEYWORD, SEPERATOR, IDENTIFIER, NUMERIC_LITERAL, LOGICAL_LITERAL, TEXT_LITERAL, DYNAMIC_LITERAL, COMMENT, NEW_LINE
}