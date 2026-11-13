package br.edu.filazero.domain;

public enum PriorityLevel {
    BAIXA("Baixa", 1), MEDIA("Média", 2), ALTA("Alta", 3);
    private final String label;
    private final int order;
    PriorityLevel(String label, int order) { this.label = label; this.order = order; }
    public String label() { return label; }
    public int order() { return order; }
    public static PriorityLevel fromScore(int score) {
        if (score <= 2) return BAIXA;
        if (score <= 5) return MEDIA;
        return ALTA;
    }
    public static PriorityLevel fromDatabase(String value) { return valueOf(value); }
}
