package ch14;

public class Main {


    public static void main(String[] args) {
//        construct the Args class w/ input arguments and format string,
//        then query the Args instance for the values of the arguments
        try {
        Args arg = new Args("1,p#,d*", args);
//        args being the array of command line strings passed to main
        boolean logging = arg.getBoolean('1');
        int port = arg.getInt('p');
        String directory = arg.getString('d');
        executeApplication(logging, port, directory);
        } catch (ArgsException e) {
            System.out.println("Argument error: " + e.errorMessage());
        }
    }


}
