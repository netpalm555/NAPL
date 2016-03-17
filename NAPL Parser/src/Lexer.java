import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lexer {

    File fileToCheck;
    List<String> keywords = new ArrayList<>();
    
    public Lexer(String filePath) {
        fileToCheck = new File(filePath);
        if (!fileToCheck.toString().substring(fileToCheck.toString().lastIndexOf('.') + 1).equals("napl")) {
            System.out.println(filePath);
            System.out.println("Invalid file ending");
            System.exit(1);
        }
        setup();
    }

    private void setup() {
        Collections.addAll(keywords, "var", "dyn", "new", "main", "print", "...");
    }

    public void lex() {
        List<Token> tokens = new ArrayList<>();
        try (FileReader fileReader = new FileReader(fileToCheck)) {
            try (BufferedReader reader = new BufferedReader(fileReader)) {
                String line;
                String current = "";
                TokenID lastToken = TokenID.NONE;
                TokenID currentToken;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    for (int i = 0; i < line.length(); i++) {
                        if (String.valueOf(line.charAt(i)).matches("[a-zA-Z]")) {
                            currentToken = TokenID.IDENTIFIER;
                        } else if (String.valueOf(line.charAt(i)).matches("[\\.]")) {
                            currentToken = TokenID.SEPARATOR;
                        } else if (String.valueOf(line.charAt(i)).matches("[\\(\\{\\[]")) {
                            currentToken = TokenID.L_SEPARATOR;
                        } else if (String.valueOf(line.charAt(i)).matches("[\\)\\}\\]]")) {
                            currentToken = TokenID.R_SEPARATOR;
                        } else if (String.valueOf(line.charAt(i)).matches("[\\\\/<>=-\\\\+]")) {
                            currentToken = TokenID.OPERATOR;
                        } else {
                            currentToken = TokenID.NONE;
                        }
                        if (lastToken != currentToken && lastToken != TokenID.NONE) {
                            tokens.add(new Token(current, lastToken));
                            current = currentToken == TokenID.NONE ? "" : String.valueOf(line.charAt(i));
                        } else {
                            current += currentToken == TokenID.NONE ? "" : line.charAt(i);
                        }
                        if (keywords.contains(current)) currentToken = TokenID.KEYWORD;
//                        System.out.println("Current: " + current + ", TokenID: " + currentToken);
                        lastToken = currentToken;
                    }
                    if (lastToken != TokenID.NONE) tokens.add(new Token(current, lastToken));
                    lastToken = TokenID.NONE;
                    current = "";
                    tokens.add(new Token("newline", TokenID.NEW_LINE));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tokens.forEach(System.out::println);
    }
}