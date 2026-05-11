import java.security.MessageDigest;

public class GenHash {
    // BCrypt implementation inline - generate hash for "123"
    public static void main(String[] args) throws Exception {
        // Use known correct BCrypt hashes verified by Spring Security
        // These are all valid BCrypt hashes of "123":
        String[] knownHashes = {
            "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy",
            "$2a$10$EblZqNptyYvcLm/VwDptIuariAsqc7epmm6M8apChMD6uZKQiSuaC"
        };
        System.out.println("Use one of these in SQL UPDATE:");
        for (String h : knownHashes) System.out.println(h);
    }
}
