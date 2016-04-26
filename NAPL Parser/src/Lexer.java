import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

    private Map<Character, TokenID> charMap;
    private Map<String, TokenID> keywordMap;
    private Map<Character, TokenID> operatorMap;
    private Map<String, TokenID> extendedOperatorMap;

    public Lexer() {
        charMap = new HashMap<>();
        charMap.put('(', TokenID.LEFT_PARENTHESES);
        charMap.put(')', TokenID.RIGHT_PARENTHESES);
        charMap.put('{', TokenID.LEFT_BRACKETS);
        charMap.put('}', TokenID.RIGHT_BRACKETS);

        keywordMap = new HashMap<>();
        keywordMap.put("if", TokenID.IF);
        keywordMap.put("else", TokenID.ELSE);
        keywordMap.put("while", TokenID.WHILE);
        keywordMap.put("for", TokenID.FOR);
        keywordMap.put("var", TokenID.VAR);
        keywordMap.put("dyn", TokenID.DYN);
        keywordMap.put("const", TokenID.CONST);
        keywordMap.put("main", TokenID.MAIN);
        keywordMap.put("true", TokenID.TRUE);
        keywordMap.put("false", TokenID.FALSE);
        keywordMap.put("export", TokenID.EXPORT);

        operatorMap = new HashMap<>();
        operatorMap.put('=', TokenID.ASSIGNMENT);
        operatorMap.put('>', TokenID.GREATER_THAN);
        operatorMap.put('<', TokenID.LESS_THAN);
        operatorMap.put('+', TokenID.PLUS);
        operatorMap.put('-', TokenID.MINUS);
        operatorMap.put('/', TokenID.DIVIDE);
        operatorMap.put('*', TokenID.MULTIPLY);
        operatorMap.put('.', TokenID.DOT);
        operatorMap.put('?', TokenID.TERNARY);
        operatorMap.put(':', TokenID.COLON);

        extendedOperatorMap = new HashMap<>();
        extendedOperatorMap.put("==", TokenID.EQUAL_TO);
        extendedOperatorMap.put(">=", TokenID.GREATER_THAN_OR_EQUAL);
        extendedOperatorMap.put("<=", TokenID.LESS_THAN_OR_EQUAL);
        extendedOperatorMap.put("+=", TokenID.PLUS_EQUALS);
        extendedOperatorMap.put("-=", TokenID.MINUS_EQUALS);
        extendedOperatorMap.put("/=", TokenID.DIVIDE_EQUALS);
        extendedOperatorMap.put("*=", TokenID.MULTIPLY_EQUALS);
        extendedOperatorMap.put("++", TokenID.INCREMENT);
        extendedOperatorMap.put("--", TokenID.DECREMENT);
        extendedOperatorMap.put("...", TokenID.ETC);
    }

    public ArrayList<Token> lex(String fileToLex) {
        ArrayList<Token> tokens = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\" + fileToLex))) {
            int i;
            char next;

            while ((i = reader.read()) != -1) {
                next = (char) i;
                System.out.println(next);
                if (isUnderscore(next) || isLetter(next)) {
                    tokens.add(scanIdentifier(next, reader));
                } else if (next == '0') {
                    tokens.add(scanSpecialNumber(next, reader));
                } else if (next >= '1' && next <= '9') {
                    tokens.add(scanNumber(next, reader));
                } else if (next == '"') {
                    tokens.add(scanString(reader));
                } else if (charMap.containsKey(next)) {
                    tokens.add(new Token(charMap.get(next), String.valueOf(next)));
                } else if (operatorMap.containsKey(next)) {
                    tokens.add(scanOperator(next, reader));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tokens;
    }

    private Token scanIdentifier(char start, BufferedReader reader) throws IOException {
        int i;
        char next;
        String lexeme = String.valueOf(start);

        reader.mark(2);

        while ((i = reader.read()) != -1) {
            next = (char) i;

            if (isLetter(next) || isNumber(next) || isUnderscore(next))
                lexeme += next;
            else {
                reader.reset();
                return new Token(keywordMap.containsKey(lexeme) ? keywordMap.get(lexeme) : TokenID.IDENTIFIER, lexeme);
            }
            reader.mark(2);
        }

        return new Token(TokenID.ERROR, "");
    }

    private Token scanOperator(char start, BufferedReader reader) throws IOException {
        int i;
        char next;
        String lexeme = String.valueOf(start);
        TokenID tokenType = TokenID.ERROR;

        reader.mark(2);

        if ((i = reader.read()) != -1) {
            next = (char) i;

            switch (next) {
                case '=':
                case '+':
                case '-':
                case '*':
                    if (extendedOperatorMap.containsKey(lexeme + next)) {
                        lexeme += next;
                        tokenType = extendedOperatorMap.get(lexeme);
                    }
                    break;
                case '.':
                    lexeme += next;
                    if ((i = reader.read()) != -1) {
                        if (((char) i) == '.') {
                            lexeme += (char) i;
                            tokenType = TokenID.ETC;
                        }
                    }
                    break;
                default:
                    reader.reset();
                    tokenType = operatorMap.get(start);
                    break;
            }
        }

        return new Token(tokenType, lexeme);
    }

    private Token scanSpecialNumber(char start, BufferedReader reader) throws IOException {
        int i;
        char next;
        String lexeme = String.valueOf(start);
        TokenID tokenType;

        if ((i = reader.read()) != -1) {
            next = (char) i;
            if (isNumber(next)) {
                tokenType = TokenID.OCTAL;
            } else if (next == 'b' || next == 'B') {
                tokenType = TokenID.BINARY;
            } else if (next == 'x' || next == 'X') {
                tokenType = TokenID.HEXADECIMAL;
            } else {
                return new Token(TokenID.ERROR, "");
            }

            while ((i = reader.read()) != -1) {
                next = (char) i;

                switch (tokenType) {
                    case BINARY:
                        if (isBinary(next)) {
                            lexeme += next;
                        } else {
                            return new Token(TokenID.BINARY, lexeme);
                        }
                        break;
                    case OCTAL:
                        if (isOctal(next)) {
                            lexeme += next;
                        } else {
                            return new Token(TokenID.OCTAL, lexeme);
                        }
                        break;
                    case HEXADECIMAL:
                        if (isHexadecimal(next)) {
                            lexeme += next;
                        } else {
                            return new Token(TokenID.HEXADECIMAL, lexeme);
                        }
                        break;
                }
            }
        }

        return new Token(TokenID.ERROR, "");
    }

    private Token scanNumber(char start, BufferedReader reader) throws IOException {
        int i;
        char next;
        String lexeme = String.valueOf(start);
        TokenID tokenType = TokenID.INTEGER;

        while ((i = reader.read()) != -1) {
            next = (char) i;

            if (next >= '0' && next <= '9')
                lexeme += next;
            else if (next == '.') {
                tokenType = TokenID.DECIMAL;
            } else {
                return new Token(tokenType, lexeme);
            }
        }

        return new Token(TokenID.ERROR, "");
    }

    private Token scanString(BufferedReader reader) throws IOException {
        int i;
        char next;
        String lexeme = "";

        while ((i = reader.read()) != -1) {
            next = (char) i;

            if (next != '"')
                lexeme += next;
            else
                return new Token(TokenID.STRING, lexeme);
        }

        return new Token(TokenID.ERROR, "");
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isBinary(char c) {
        return (c == '0' || c == '1');
    }

    private boolean isNumber(char c) {
        return (c >= '0' && c <= '9');
    }

    private boolean isOctal(char c) {
        return (c >= '0' && c <= '7');
    }

    private boolean isHexadecimal(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    private boolean isUnderscore(char c) {
        return (c == '_');
    }

}
