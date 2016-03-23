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

    public List<Token> lex() {
        List<Token> tokens = new ArrayList<>();
        try (FileReader fileReader = new FileReader(fileToCheck)) {
            try (BufferedReader reader = new BufferedReader(fileReader)) {
                String line;
                String current = "";
                String last = "";
                TokenID lastToken = TokenID.NONE;
                TokenID currentToken;
                boolean exportNow = false;
                System.out.println("Original Code:");
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    for (int i = 0; i < line.length(); i++) {
                        current = String.valueOf(line.charAt(i));
                        if (current.matches("[a-zA-Z]")) {
                            currentToken = TokenID.IDENTIFIER;
                            exportNow = false;
                        } else if (current.matches("[\\.]")) {
                            currentToken = TokenID.SEPARATOR;
                            exportNow = false;
                        } else if (current.matches("[\\(\\{\\[]")) {
                            currentToken = TokenID.L_SEPARATOR;
                            exportNow = true;
                        } else if (current.matches("[\\)\\}\\]]")) {
                            currentToken = TokenID.R_SEPARATOR;
                            exportNow = true;
                        } else if (current.matches("[\\\\/<>=-\\\\+]")) {
                            currentToken = TokenID.OPERATOR;
                            exportNow = false;
                        } else {
                            currentToken = TokenID.NONE;
                            exportNow = false;
                        }
                        if (exportNow) {
                            tokens.add(new Token(current, currentToken));
                            current = "";
                            currentToken = TokenID.NONE;
                        }
                        if (lastToken != currentToken && lastToken != TokenID.NONE) {
                            tokens.add(new Token(last, lastToken));
                            last = currentToken == TokenID.NONE ? "" : current;
                        } else {
                            last += currentToken == TokenID.NONE ? "" : current;
                        }
                        if (keywords.contains(last)) currentToken = TokenID.KEYWORD;
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
        System.out.println("\nCaptured Tokens:");
        tokens.forEach(System.out::println);
        return tokens;
    }
}