package pio.daw;
 
import java.nio.file.Path;
 
public class App {
 
    /**
     * Parse the arguments of the program to get the library registry file
     * path. Exits the program if the args are not correct or the file does
     * not exists.
     * @param args program args.
     * @return Path to file if exists.
     */
    public static Path getPathFromArgs(String[] args) throws Exception {
        boolean argsInvalid = args == null || args.length != 1;
        if (argsInvalid) {
            throw new Exception("Se esperaba exactamente 1 argumento, se recibieron: "
                    + (args == null ? 0 : args.length));
        }
 
        Path path = Path.of(args[0]).toAbsolutePath();
 
        boolean isTxtFile = path.getFileName().toString().endsWith(".txt");
        if (!isTxtFile) {
            throw new Exception("El archivo no tiene extensión .txt: " + path);
        }
 
        return path;
    }
 
    public static void main(String[] args) throws Exception {
        Path path = getPathFromArgs(args);
        Controlable controller = Library.fromFile(path);
        controller.printResume();
    }
}