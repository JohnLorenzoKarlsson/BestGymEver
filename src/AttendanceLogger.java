import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

public class AttendanceLogger {
    public static void appendVisit(Path file, Member member, LocalDate date) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(
                file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String row = member.getName() + ";" + member.getSsn() + ";" + date.toString();
            bw.write(row);
            bw.newLine();
        }
    }
}