public class Main {

    public static void main (String[] args) {
        Iterator cliInputs = args.iterator();
        if (cliInputs.hasNext()) {
            switch (cliInputs.next()) {
                case "-h":
                case "--help":
                    System.out.println("This will be the help file for the NAPL parser.");
                    break;
                case "--syntaxcheck":
                    if (cliInputs.hasNext()) {
                        SyntaxChecker checker = new SyntaxChecker(cliInputs.next());
                    } else {
                        System.out.println("Please provide a file to check for syntax");
                    }
                    break;
            }
        }
    }

}
