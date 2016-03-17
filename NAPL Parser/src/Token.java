public class Token {

    String s;
    TokenID id;

    public Token(String s, TokenID id) {
        this.s = s;
        this.id = id;
    }

    public String toString() {
        return "[\"" + s + "\"," + id + "]";
    }


}


