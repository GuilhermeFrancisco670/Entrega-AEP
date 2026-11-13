package br.edu.filazero.domain;

public final class RespiratoryRateRule implements ScoringRule {
    public int pointsFor(VitalSigns s) {
        int v = s.respiratoryRate();
        if (v < 10 || v > 30) return 3;
        if (v <= 11 || v >= 21) return 2;
        return 0;
    }
    public String name() { return "Frequência respiratória"; }
}
