package br.edu.filazero.domain;

public final class SystolicPressureRule implements ScoringRule {
    public int pointsFor(VitalSigns s) {
        int v = s.systolicPressure();
        if (v < 90 || v >= 180) return 3;
        if (v <= 99 || v >= 160) return 2;
        return 0;
    }
    public String name() { return "Pressão sistólica"; }
}
