package br.edu.filazero.domain;

public final class HeartRateRule implements ScoringRule {
    public int pointsFor(VitalSigns s) {
        int v = s.heartRate();
        if (v < 50 || v > 120) return 3;
        if (v <= 59 || v >= 101) return 2;
        return 0;
    }
    public String name() { return "Frequência cardíaca"; }
}
