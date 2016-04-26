import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BetterLexer {

    private Map<Character, NewTokenId> charMap;
    private Map<String, NewTokenId> keywordMap;
    private Map<Character, NewTokenId> operatorMap;
    private Map<String, NewTokenId> extendedOperatorMap;

    public BetterLexer() {
        charMap = new HashMap<>();
        charMap.put('(', NewTokenId.LEFT_PARENTHESES);
        charMap.put(')', NewTokenId.RIGHT_PARENTHESES);
        charMap.put('{', NewTokenId.LEFT_BRACKETS);
        charMap.put('}', NewTokenId.RIGHT_BRACKETS);

        keywordMap = new HashMap<>();
        keywordMap.put("if", NewTokenId.IF);
        keywordMap.put("else", NewTokenId.ELSE);
        keywordMap.put("while", NewTokenId.WHILE);
        keywordMap.put("for", NewTokenId.FOR);
        keywordMap.put("var", NewTokenId.VAR);
        keywordMap.put("dyn", NewTokenId.DYN);
        keywordMap.put("const", NewTokenId.CONST);
        keywordMap.put("main", NewTokenId.MAIN);
        keywordMap.put("true", NewTokenId.TRUE);
        keywordMap.put("false", NewTokenId.FALSE);
        keywordMap.put("export", NewTokenId.EXPORT);

        operatorMap = new HashMap<>();
        operatorMap.put('=', NewTokenId.ASSIGNMENT);
        operatorMap.put('>', NewTokenId.GREATER_THAN);
        operatorMap.put('<', NewTokenId.LESS_THAN);
        operatorMap.put('+', NewTokenId.PLUS);
        operatorMap.put('-', NewTokenId.MINUS);
        operatorMap.put('/', NewTokenId.DIVIDE);
        operatorMap.put('*', NewTokenId.MULTIPLY);
        operatorMap.put('.', NewTokenId.DOT);
        operatorMap.put('?', NewTokenId.TERNARY);
        operatorMap.put(':', NewTokenId.COLON);

        extendedOperatorMap = new HashMap<>();
        extendedOperatorMap.put("==", NewTokenId.EQUAL_TO);
        extendedOperatorMap.put(">=", NewTokenId.GREATER_THAN_OR_EQUAL);
        extendedOperatorMap.put("<=", NewTokenId.LESS_THAN_OR_EQUAL);
        extendedOperatorMap.put("+=", NewTokenId.PLUS_EQUALS);
        extendedOperatorMap.put("-=", NewTokenId.MINUS_EQUALS);
        extendedOperatorMap.put("/=", NewTokenId.DIVIDE_EQUALS);
        extendedOperatorMap.put("*=", NewTokenId.MULTIPLY_EQUALS);
        extendedOperatorMap.put("++", NewTokenId.INCREMENT);
        extendedOperatorMap.put("--", NewTokenId.DECREMENT);
        extendedOperatorMap.put("...", NewTokenId.ETC);
    }

    public ArrayList<NewToken> lex(String fileToLex) {
        ArrayList<NewToken> tokens = new ArrayList<>();
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
                    tokens.add(new NewToken(charMap.get(next), String.valueOf(next)));
                } else if (operatorMap.containsKey(next)) {
                    tokens.add(scanOperator(next, reader));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tokens;
    }

    private NewToken scanIdentifier(char start, BufferedReader reader) throws IOException {
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
                return new NewToken(keywordMap.containsKey(lexeme) ? keywordMap.get(lexeme) : NewTokenId.IDENTIFIER, lexeme);
            }
            reader.mark(2);
        }

        return new NewToken(NewTokenId.ERROR, "");
    }

    private NewToken scanOperator(char start, BufferedReader reader) throws IOException {
        int i;
        char next;
        String lexeme = String.valueOf(start);
        NewTokenId tokenType = NewTokenId.ERROR;

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
                            tokenType = NewTokenId.ETC;
                        }
                    }
                    break;
                default:
                    reader.reset();
                    tokenType = operatorMap.get(start);
                    break;
            }
        }

        return new NewToken(tokenType, lexeme);
    }

    private NewToken scanSpecialNumber(char start, BufferedReader reader) throws IOException {
        int i;
        char next;
        String lexeme = String.valueOf(start);
        NewTokenId tokenType;

        if ((i = reader.read()) != -1) {
            next = (char) i;
            if (isNumber(next)) {
                tokenType = NewTokenId.OCTAL;
            } else if (next == 'b' || next == 'B') {
                tokenType = NewTokenId.BINARY;
            } else if (next == 'x' || next == 'X') {
                tokenType = NewTokenId.HEXADECIMAL;
            } else {
                return new NewToken(NewTokenId.ERROR, "");
            }

            while ((i = reader.read()) != -1) {
                next = (char) i;

                switch (tokenType) {
                    case BINARY:
                        if (isBinary(next)) {
                            lexeme += next;
                        } else {
                            return new NewToken(NewTokenId.BINARY, lexeme);
                        }
                        break;
                    case OCTAL:
                        if (isOctal(next)) {
                            lexeme += next;
                        } else {
                            return new NewToken(NewTokenId.OCTAL, lexeme);
                        }
                        break;
                    case HEXADECIMAL:
                        if (isHexadecimal(next)) {
                            lexeme += next;
                        } else {
                            return new NewToken(NewTokenId.HEXADECIMAL, lexeme);
                        }
                        break;
                }
            }
        }

        return new NewToken(NewTokenId.ERROR, "");
    }

    private NewToken scanNumber(char start, BufferedReader reader) throws IOException {
        int i;
        char next;
        String lexeme = String.valueOf(start);
        NewTokenId tokenType = NewTokenId.INTEGER;

        while ((i = reader.read()) != -1) {
            next = (char) i;

            if (next >= '0' && next <= '9')
                lexeme += next;
            else if (next == '.') {
                tokenType = NewTokenId.DECIMAL;
            } else {
                return new NewToken(tokenType, lexeme);
            }
        }

        return new NewToken(NewTokenId.ERROR, "");
    }

    private NewToken scanString(BufferedReader reader) throws IOException {
        int i;
        char next;
        String lexeme = "";

        while ((i = reader.read()) != -1) {
            next = (char) i;

            if (next != '"')
                lexeme += next;
            else
                return new NewToken(NewTokenId.STRING, lexeme);
        }

        return new NewToken(NewTokenId.ERROR, "");
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
