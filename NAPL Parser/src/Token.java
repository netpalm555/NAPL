public class Token {

    String lexeme;
    TokenID id;

    public Token(String lexeme, TokenID id) {
        this.lexeme = lexeme;
        this.id = id;
    }

    public String getString() {
        return lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String s) {
        this.lexeme = s;
    }

    public TokenID getId() {
        return id;
    }

    public void setId(TokenID id) {
        this.id = id;
    }

    public String toString() {
        return "[\"" + lexeme + "\"," + id + "]";
    }


}


