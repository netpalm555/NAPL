import java.util.List;

public class SeparatorChecker {

    List<Token> tokens;

    public SeparatorChecker(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void check() {
        int count = 0;
        int line = 1;
        for (Token t : tokens) {
            if (t.getId() == TokenID.NEW_LINE) line++;
            if (t.getId() == TokenID.L_SEPARATOR) {
                count++;
            }
            if (t.getId() == TokenID.R_SEPARATOR) {
                if (count == 0) System.out.println("Unmatched right separator on line " + line);
                else count--;
            }
        }
        if (count != 0) System.out.println("Left and Right Separators do not match up");
//        else System.out.println("Left and Right Separators match up");
        System.out.println("Separator checking finished");
    }

    public void betterCheck() {

    }

}
