package br.edu.filazero.domain;

public final class ConsciousnessRule implements ScoringRule {
    public int pointsFor(VitalSigns s) { return s.consciousness().points(); }
    public String name() { return "Nível de consciência"; }
}
