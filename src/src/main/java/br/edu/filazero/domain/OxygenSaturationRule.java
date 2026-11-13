package br.edu.filazero.domain;

public final class OxygenSaturationRule implements ScoringRule {
    public int pointsFor(VitalSigns s) {
        int v = s.oxygenSaturation();
        if (v < 90) return 3;
        if (v <= 93) return 2;
        return 0;
    }
    public String name() { return "Saturação de oxigênio"; }
}
