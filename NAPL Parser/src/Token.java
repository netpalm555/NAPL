public class Token {

    String s;
    TokenID id;

    public Token(String s, TokenID id) {
        this.s = s;
        this.id = id;
    }

    public String getString() {
        return s;
    }

    public void setString(String s) {
        this.s = s;
    }

    public TokenID getId() {
        return id;
    }

    public void setId(TokenID id) {
        this.id = id;
    }

    public String toString() {
        return "[\"" + s + "\"," + id + "]";
    }


}


