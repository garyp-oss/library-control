package pio.daw;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
 
public class Library implements Controlable {
 
    private final Map<String, User> users;
 
    /**
     * Read the library register file (.txt) and create a Library object
     * with the current status of the users.
     * @param path Library registry file path.
     * @return Library object.
     */
    public static Library fromFile(Path path) throws IOException {
        Library library = new Library();
        Files.lines(path)
             .map(line -> line.split(";"))
             .filter(parts -> parts.length == 2)
             .forEach(parts -> {
                 String id = parts[0];
                 EventType event = parseEvent(parts[1]);
                 if (event != null) {
                     library.registerChange(id, event);
                 }
             });
        return library;
    }
 
    private static EventType parseEvent(String raw) {
        return switch (raw.trim().toUpperCase()) {
            case "ENTRADA" -> EventType.ENTRY;
            case "SALIDA"  -> EventType.EXIT;
            default        -> null;
        };
    }
 
    private Library() {
        this.users = new TreeMap<>();
    }
 
    @Override
    public void registerChange(String id, EventType e) {
        users.computeIfAbsent(id, User::new)
             .registerNewEvent(e);
    }
 
    @Override
    public List<User> getCurrentInside() {
        return users.values().stream()
                    .filter(User::isInside)
                    .collect(Collectors.toList());
    }
 
    @Override
    public List<User> getMaxEntryUsers() {
        int maxEntries = users.values().stream()
                              .mapToInt(User::getNEntries)
                              .max()
                              .orElse(0);
 
        return users.values().stream()
                    .filter(u -> u.getNEntries() == maxEntries)
                    .collect(Collectors.toList());
    }
 
    @Override
    public List<User> getUserList() {
        return users.values().stream()
                    .filter(u -> u.getNEntries() > 0)
                    .sorted(Comparator.comparing(User::getId))
                    .collect(Collectors.toList());
    }
 
    @Override
    public void printResume() {
        System.out.print("Usuarios actualmente dentro de la biblioteca:\n");
        getCurrentInside().stream()
                          .sorted(Comparator.comparing(User::getId))
                          .forEach(u -> System.out.print(u.getId() + "\n"));
 
        System.out.print("\n");
 
        System.out.print("Número de entradas por usuario:\n");
        getUserList().stream()
                     .sorted(Comparator.comparing(User::getId))
                     .forEach(u -> System.out.print(u.getId() + " -> " + u.getNEntries() + "\n"));
 
        System.out.print("\n");
 
        System.out.print("Usuario(s) con más entradas:\n");
        getMaxEntryUsers().stream()
                          .sorted(Comparator.comparing(User::getId))
                          .forEach(u -> System.out.print(u.getId() + "\n"));
    }
}
