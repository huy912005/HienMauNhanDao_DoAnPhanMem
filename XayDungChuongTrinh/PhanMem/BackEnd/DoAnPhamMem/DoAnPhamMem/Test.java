import java.security.MessageDigest;
public class Test {
    public static void main(String[] args) {
        System.out.println(org.springframework.security.crypto.bcrypt.BCrypt.hashpw("123", org.springframework.security.crypto.bcrypt.BCrypt.gensalt()));
    }
}
