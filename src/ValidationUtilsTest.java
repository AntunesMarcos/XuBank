import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ValidationUtilsTest {

    @Test
    public void testValidarCPFValido() {
        Assertions.assertTrue(ValidationUtils.validarCPF("11144477735"));
        Assertions.assertTrue(ValidationUtils.validarCPF("111.444.777-35"));
    }

    @Test
    public void testValidarCPFInvalido() {
        Assertions.assertFalse(ValidationUtils.validarCPF("12345678900"));
        Assertions.assertFalse(ValidationUtils.validarCPF("111.111.111-11"));
        Assertions.assertFalse(ValidationUtils.validarCPF("123"));
        Assertions.assertFalse(ValidationUtils.validarCPF(null));
        Assertions.assertFalse(ValidationUtils.validarCPF(""));
        Assertions.assertFalse(ValidationUtils.validarCPF("abcdefghijk"));
    }

    @Test
    public void testValidarNomeValido() {
        Assertions.assertTrue(ValidationUtils.validarNome("João Silva"));
        Assertions.assertTrue(ValidationUtils.validarNome("Maria José"));
        Assertions.assertTrue(ValidationUtils.validarNome("José"));
        Assertions.assertTrue(ValidationUtils.validarNome("Ana Luíza"));
    }

    @Test
    public void testValidarNomeInvalido() {
        Assertions.assertFalse(ValidationUtils.validarNome("J"));
        Assertions.assertFalse(ValidationUtils.validarNome(""));
        Assertions.assertFalse(ValidationUtils.validarNome(null));
        Assertions.assertFalse(ValidationUtils.validarNome("João123"));
        Assertions.assertFalse(ValidationUtils.validarNome("João@Silva"));
    }

    @Test
    public void testValidarSenhaValida() {
        Assertions.assertTrue(ValidationUtils.validarSenha("MinhaSenh@123"));
        Assertions.assertTrue(ValidationUtils.validarSenha("Password#456"));
        Assertions.assertTrue(ValidationUtils.validarSenha("Teste$789"));
    }

    @Test
    public void testValidarSenhaInvalida() {
        Assertions.assertFalse(ValidationUtils.validarSenha("123"));
        Assertions.assertFalse(ValidationUtils.validarSenha("password"));
        Assertions.assertFalse(ValidationUtils.validarSenha("PASSWORD"));
        Assertions.assertFalse(ValidationUtils.validarSenha("Password123"));
        Assertions.assertFalse(ValidationUtils.validarSenha(null));
    }

    @Test
    public void testValidarValor() {
        Assertions.assertTrue(ValidationUtils.validarValor(100.50));
        Assertions.assertTrue(ValidationUtils.validarValor(0));
        Assertions.assertTrue(ValidationUtils.validarValor(0.01));

        Assertions.assertFalse(ValidationUtils.validarValor(-1));
        Assertions.assertFalse(ValidationUtils.validarValor(Double.POSITIVE_INFINITY));
        Assertions.assertFalse(ValidationUtils.validarValor(Double.NEGATIVE_INFINITY));
        Assertions.assertFalse(ValidationUtils.validarValor(Double.NaN));
    }

    @Test
    public void testSanitizarString() {
        Assertions.assertEquals("João Silva", ValidationUtils.sanitizarString("João Silva"));
        Assertions.assertEquals("João Silva", ValidationUtils.sanitizarString("João Silva<>"));
        Assertions.assertEquals("João Silva", ValidationUtils.sanitizarString("  João Silva  "));
        Assertions.assertEquals("", ValidationUtils.sanitizarString(null));
        Assertions.assertEquals("", ValidationUtils.sanitizarString(""));
    }
}