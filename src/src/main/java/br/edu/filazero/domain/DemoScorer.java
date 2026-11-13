package br.edu.filazero.domain;

import java.util.List;

/** Pontuação fictícia aprovada para demonstração acadêmica. Não é protocolo clínico. */
public final class DemoScorer {
    private final List<ScoringRule> rules = List.of(new HeartRateRule(), new SystolicPressureRule(),
            new RespiratoryRateRule(), new TemperatureRule(), new OxygenSaturationRule(), new ConsciousnessRule());
    public int score(VitalSigns signs) {
        return rules.stream().mapToInt(rule -> rule.pointsFor(signs)).sum();
    }
    public PriorityLevel priority(VitalSigns signs) { return PriorityLevel.fromScore(score(signs)); }
}
