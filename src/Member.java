import java.time.LocalDate;

public class Member {
    private final String name;
    private final String address;
    private final String email;
    private final String ssn;                // normaliserat: YYMMDDXXXX
    private final LocalDate joinedDate;
    private final LocalDate lastPaymentDate; // senast betalt årsavgift
    private final MembershipType type;

    public Member(String name, String address, String email,
                  String ssnNormalized, LocalDate joinedDate,
                  LocalDate lastPaymentDate, MembershipType type) {

        if (name == null || name.isBlank()) throw new IllegalArgumentException("Namn saknas");
        if (ssnNormalized == null || ssnNormalized.isBlank()) throw new IllegalArgumentException("Personnummer saknas");
        if (joinedDate == null || lastPaymentDate == null) throw new IllegalArgumentException("Datum saknas");
        if (type == null) throw new IllegalArgumentException("Medlemsnivå saknas");

        this.name = name.trim();
        this.address = address == null ? "" : address.trim();
        this.email = email == null ? "" : email.trim();
        this.ssn = ssnNormalized;
        this.joinedDate = joinedDate;
        this.lastPaymentDate = lastPaymentDate;
        this.type = type;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public String getSsn() { return ssn; }
    public LocalDate getJoinedDate() { return joinedDate; }
    public LocalDate getLastPaymentDate() { return lastPaymentDate; }
    public MembershipType getType() { return type; }

    public boolean isCurrent(LocalDate today) {
        return !lastPaymentDate.isBefore(today.minusYears(1));
    }

    public static String normalizeSsn(String raw) {
        if (raw == null) return "";
        String digits = raw.replaceAll("\\D", ""); // endast siffror
        if (digits.length() == 12) digits = digits.substring(2); // 19700101xxxx → 700101xxxx
        return digits; // vi accepterar 10 siffror (YYMMDDXXXX); annan längd hanteras i söksteget
    }
}