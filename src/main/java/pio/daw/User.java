package pio.daw;

public class User implements Localizable {

    private final String id;
    private int nEntries;
    private boolean inside;

    public User(String id) {
        this.id    = id;
        this.nEntries = 0;
        this.inside   = false;
    }

    public String getId() {
        return id;
    }

    public void registerNewEvent(EventType e) {
        switch (e) {
            case ENTRY -> {
                if (!inside) {
                    inside = true;
                    nEntries++;
                }
            }
            case EXIT -> {
                if (inside) {
                    inside = false;
                }
            }
        }
    }

    public Integer getNEntries() {
        return nEntries;
    }

    /**
     * Check if the object is inside something
     * @return true if it is inside
     */
    @Override
    public Boolean isInside() {
        return inside;
    }
}