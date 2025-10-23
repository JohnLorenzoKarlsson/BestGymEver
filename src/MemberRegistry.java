import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class MemberRegistry {
    private final List<Member> members = new ArrayList<>();

    public List<Member> getAll() { return members; }

    public static MemberRegistry load(Path file) throws IOException {
        MemberRegistry reg = new MemberRegistry();

        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                row++;
                line = line.trim();
                if (line.isEmpty()) continue;

                // Hoppa över header-rad (din första rad)
                if (row == 1 && line.toLowerCase().startsWith("namn;")) continue;

                String[] parts = line.split(";");
                if (parts.length < 7) {
                    System.err.println("Varning: rad " + row + " har fel antal kolumner (" + parts.length + ")");
                    continue;
                }

                String name   = parts[0].trim();
                String addr   = parts[1].trim(); // får gärna innehålla kommatecken
                String email  = parts[2].trim();
                String ssnRaw = parts[3].trim();
                String joined = parts[4].trim(); // YYYY-MM-DD
                String paid   = parts[5].trim(); // YYYY-MM-DD
                String level  = parts[6].trim();

                try {
                    String ssnNorm = Member.normalizeSsn(ssnRaw);
                    if (ssnNorm.length() != 10) throw new IllegalArgumentException("Fel personnummerformat: " + ssnRaw);
                    LocalDate joinedDate = LocalDate.parse(joined); // ISO-8601
                    LocalDate lastPaid   = LocalDate.parse(paid);
                    MembershipType type  = MembershipType.fromString(level);

                    Member m = new Member(name, addr, email, ssnNorm, joinedDate, lastPaid, type);
                    reg.members.add(m);
                } catch (DateTimeParseException | IllegalArgumentException e) {
                    System.err.println("Hoppar över rad " + row + " p.g.a. ogiltiga data: " + e.getMessage());
                }
            }
        }
        return reg;
    }

    public Member findBySsn(String input) {
        String norm = Member.normalizeSsn(input);
        if (norm.length() != 10) return null;
        for (Member m : members) {
            if (m.getSsn().equals(norm)) return m;
        }
        return null;
    }

    public Member findByName(String input) {
        if (input == null) return null;
        String needle = input.trim().toLowerCase();
        for (Member m : members) {
            if (m.getName().toLowerCase().equals(needle)) return m;
        }
        return null;
    }
}