import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class PasswordUtilsTest {

    @Test
    public void testGenerateSalt() {
        String salt1 = PasswordUtils.generateSalt();
        String salt2 = PasswordUtils.generateSalt();

        Assertions.assertNotNull(salt1);
        Assertions.assertNotNull(salt2);
        Assertions.assertNotEquals(salt1, salt2); // Salts devem ser únicos
        Assertions.assertTrue(salt1.length() > 0);
    }

    @Test
    public void testHashPassword() throws SecurityException {
        String password = "MinhaSenh@123";
        String salt = PasswordUtils.generateSalt();

        String hash1 = PasswordUtils.hashPassword(password, salt);
        String hash2 = PasswordUtils.hashPassword(password, salt);

        Assertions.assertNotNull(hash1);
        Assertions.assertNotNull(hash2);
        Assertions.assertEquals(hash1, hash2); // Mesmo salt e senha devem gerar mesmo hash
        Assertions.assertTrue(hash1.length() > 0);
    }

    @Test
    public void testVerifyPassword() throws SecurityException {
        String password = "MinhaSenh@123";
        String salt = PasswordUtils.generateSalt();
        String hash = PasswordUtils.hashPassword(password, salt);

        Assertions.assertTrue(PasswordUtils.verifyPassword(password, hash, salt));
        Assertions.assertFalse(PasswordUtils.verifyPassword("SenhaErrada", hash, salt));
        Assertions.assertFalse(PasswordUtils.verifyPassword(null, hash, salt));
    }

    @Test
    public void testHashPasswordComSenhaNull() {
        String salt = PasswordUtils.generateSalt();
        Assertions.assertThrows(SecurityException.class, () -> {
            PasswordUtils.hashPassword(null, salt);
        });
    }

    @Test
    public void testVerifyPasswordComHashNull() {
        String password = "MinhaSenh@123";
        String salt = PasswordUtils.generateSalt();

        Assertions.assertThrows(SecurityException.class, () -> {
            PasswordUtils.verifyPassword(password, null, salt);
        });
    }
}