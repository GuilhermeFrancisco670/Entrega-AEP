package br.edu.filazero.domain;

public record VitalSigns(int heartRate, int systolicPressure, int respiratoryRate,
                         double temperatureCelsius, int oxygenSaturation,
                         ConsciousnessLevel consciousness) {
    public VitalSigns {
        if (heartRate < 1 || heartRate > 300) throw new IllegalArgumentException("Frequência cardíaca fora do intervalo de entrada (1–300).");
        if (systolicPressure < 1 || systolicPressure > 350) throw new IllegalArgumentException("Pressão sistólica fora do intervalo de entrada (1–350).");
        if (respiratoryRate < 1 || respiratoryRate > 100) throw new IllegalArgumentException("Frequência respiratória fora do intervalo de entrada (1–100).");
        if (!Double.isFinite(temperatureCelsius) || temperatureCelsius < 20 || temperatureCelsius > 50) throw new IllegalArgumentException("Temperatura fora do intervalo de entrada (20–50 °C).");
        if (oxygenSaturation < 0 || oxygenSaturation > 100) throw new IllegalArgumentException("Saturação deve estar entre 0 e 100%.");
        if (consciousness == null) throw new IllegalArgumentException("Nível de consciência obrigatório.");
    }
}
