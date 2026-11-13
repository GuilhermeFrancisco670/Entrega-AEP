package br.edu.filazero.domain;

public enum ConsciousnessLevel {
    ALERTA(0, "Alerta"), RESPOSTA_A_VOZ(2, "Resposta à voz"),
    RESPOSTA_A_DOR(3, "Resposta à dor"), SEM_RESPOSTA(4, "Sem resposta");
    private final int points;
    private final String label;
    ConsciousnessLevel(int points, String label) { this.points = points; this.label = label; }
    public int points() { return points; }
    public String label() { return label; }
    public static ConsciousnessLevel fromOption(int option) {
        return switch (option) {
            case 1 -> ALERTA; case 2 -> RESPOSTA_A_VOZ; case 3 -> RESPOSTA_A_DOR; case 4 -> SEM_RESPOSTA;
            default -> throw new IllegalArgumentException("Opção de consciência inválida.");
        };
    }
}
