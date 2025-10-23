import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private static final Path MEMBERS_FILE    = Path.of("members.csv");
    private static final Path ATTENDANCE_FILE = Path.of("pt_attendance.csv");

    public static void main(String[] args) {
        MemberRegistry registry;
        try {
            registry = MemberRegistry.load(MEMBERS_FILE);
            System.out.println("Läste in " + registry.getAll().size() + " poster från " + MEMBERS_FILE);
        } catch (Exception e) {
            System.err.println("Fel: kunde inte läsa medlemsfilen: " + e.getMessage());
            return;
        }

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Best Gym Ever – Reception");
            System.out.println("Skriv personnummer (ÅÅMMDD-XXXX / 12 siffror) eller fullständigt namn.");
            System.out.println("Tryck bara Enter för att avsluta.");

            while (true) {
                System.out.print("> ");
                String input = sc.nextLine();
                if (input == null || input.trim().isEmpty()) {
                    System.out.println("Avslutar.");
                    break;
                }

                Member m = findMember(registry, input);
                if (m == null) {
                    System.out.println("OBEHÖRIG: personen finns inte i registret.");
                    continue;
                }

                LocalDate today = LocalDate.now();
                if (m.isCurrent(today)) {
                    System.out.printf("NUVARANDE MEDLEM (%s). Välkommen, %s!%n",
                            m.getType().displayName(), m.getName());
                    try {
                        AttendanceLogger.appendVisit(ATTENDANCE_FILE, m, today);
                        System.out.println("PT-logg uppdaterad: " + ATTENDANCE_FILE);
                    } catch (Exception e) {
                        System.err.println("Varning: kunde inte skriva PT-logg: " + e.getMessage());
                    }
                } else {
                    System.out.printf("F.D. MEDLEM (senast betalt: %s). Hej %s, vill du förnya?%n",
                            m.getLastPaymentDate(), m.getName());
                }
            }
        }
    }
    private static Member findMember(MemberRegistry registry, String input) {
        String digitsOnly = input.replaceAll("\\D", "");
        if (digitsOnly.length() == 10 || digitsOnly.length() == 12) {
            Member bySsn = registry.findBySsn(input);
            if (bySsn != null) return bySsn;
        }
        return registry.findByName(input);
    }
}