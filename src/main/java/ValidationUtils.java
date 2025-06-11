import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");
    private static final Pattern NOME_PATTERN = Pattern.compile("^[a-zA-ZÀ-ÿ\\s]{2,50}$");

    public static boolean validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        if (!CPF_PATTERN.matcher(cpfLimpo).matches()) {
            return false;
        }

        // Verificar se todos os dígitos são iguais
        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Validar dígitos verificadores
        return validarDigitosCPF(cpfLimpo);
    }

    private static boolean validarDigitosCPF(String cpf) {
        try {
            int[] digitos = new int[11];
            for (int i = 0; i < 11; i++) {
                digitos[i] = Integer.parseInt(cpf.substring(i, i + 1));
            }

            // Validar primeiro dígito
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += digitos[i] * (10 - i);
            }
            int resto = soma % 11;
            int dv1 = (resto < 2) ? 0 : 11 - resto;

            if (dv1 != digitos[9]) {
                return false;
            }

            // Validar segundo dígito
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += digitos[i] * (11 - i);
            }
            resto = soma % 11;
            int dv2 = (resto < 2) ? 0 : 11 - resto;

            return dv2 == digitos[10];
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validarNome(String nome) {
        return nome != null && NOME_PATTERN.matcher(nome.trim()).matches();
    }

    public static boolean validarSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            return false;
        }

        boolean temMaiuscula = false;
        boolean temMinuscula = false;
        boolean temDigito = false;
        boolean temEspecial = false;

        for (char c : senha.toCharArray()) {
            if (Character.isUpperCase(c)) temMaiuscula = true;
            else if (Character.isLowerCase(c)) temMinuscula = true;
            else if (Character.isDigit(c)) temDigito = true;
            else if ("!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(c) >= 0) temEspecial = true;
        }

        return temMaiuscula && temMinuscula && temDigito && temEspecial;
    }

    public static boolean validarValor(double valor) {
        return valor >= 0 && Double.isFinite(valor) && valor <= Double.MAX_VALUE / 2;
    }

    public static String sanitizarString(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[<>\"'&]", "");
    }
}