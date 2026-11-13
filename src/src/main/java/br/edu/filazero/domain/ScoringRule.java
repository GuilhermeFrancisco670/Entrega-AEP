package br.edu.filazero.domain;

/** Regra de pontuação didática, não clínica. */
public interface ScoringRule {
    int pointsFor(VitalSigns signs);
    String name();
}
