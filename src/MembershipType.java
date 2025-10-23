public enum MembershipType {
    STANDARD("Standard"),
    GULD("Guld"),
    PLATINA("Platina");

    private final String display;
    MembershipType(String display) { this.display = display; }
    public String displayName() { return display; }

    public static MembershipType fromString(String s) {
        if (s == null) throw new IllegalArgumentException("Tom medlemsnivå");
        String k = s.trim().toUpperCase();
        switch (k) {
            case "STANDARD": return STANDARD;
            case "GULD":     return GULD;
            case "PLATINA":  return PLATINA;
            default: throw new IllegalArgumentException("Okänd medlemsnivå: " + s);
        }
    }
}