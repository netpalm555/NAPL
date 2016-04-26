public class Token {

    String lexeme;
    TokenID id;

    public Token(TokenID id, String lexeme) {
        this.lexeme = lexeme;
        this.id = id;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public TokenID getId() {
        return id;
    }

    public void setId(TokenID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "[\"" + lexeme + "\"," + id + "]";
    }

}
