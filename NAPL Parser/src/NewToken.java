public class NewToken {

    String lexeme;
    NewTokenId id;

    public NewToken(NewTokenId id, String lexeme) {
        this.lexeme = lexeme;
        this.id = id;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public NewTokenId getId() {
        return id;
    }

    public void setId(NewTokenId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "[\"" + lexeme + "\"," + id + "]";
    }

}
