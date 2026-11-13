package br.edu.filazero.domain;

public final class TemperatureRule implements ScoringRule {
    public int pointsFor(VitalSigns s) {
        double v = s.temperatureCelsius();
        if (v < 35 || v >= 40) return 3;
        if (v < 36 || v >= 38) return 2;
        return 0;
    }
    public String name() { return "Temperatura"; }
}
