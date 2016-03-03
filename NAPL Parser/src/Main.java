public class Main {

    public static void main (String[] args) {
        for(int i = 0; i  < args.length; i++) {
            System.out.println(args[i]);
            if(i == 0) {
                switch (args[i]) {
                    case "-h":
                    case "--help":
                        System.out.println("This will be the help file for the NAPL parser.");
                        break;
                }
            }
        }
    }

}
