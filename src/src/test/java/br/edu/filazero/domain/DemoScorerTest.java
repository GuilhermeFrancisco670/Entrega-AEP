package br.edu.filazero.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DemoScorerTest {
    private final DemoScorer scorer=new DemoScorer();
    @Test void normalDemoMeasurementsProduceZeroAndLowPriority(){
        VitalSigns s=new VitalSigns(80,120,16,37.0,98,ConsciousnessLevel.ALERTA);
        assertEquals(0,scorer.score(s));assertEquals(PriorityLevel.BAIXA,scorer.priority(s));
    }
    @Test void multipleSignalsAreAddedAndCanProduceHighPriority(){
        VitalSigns s=new VitalSigns(130,185,35,40.0,85,ConsciousnessLevel.RESPOSTA_A_DOR);
        assertEquals(18,scorer.score(s));assertEquals(PriorityLevel.ALTA,scorer.priority(s));
    }
    @Test void consciousResponseIsIncludedPolymorphically(){
        VitalSigns s=new VitalSigns(80,120,16,37.0,98,ConsciousnessLevel.SEM_RESPOSTA);
        assertEquals(4,scorer.score(s));assertEquals(PriorityLevel.MEDIA,scorer.priority(s));
    }
    @Test void rejectsInvalidSaturation(){
        assertThrows(IllegalArgumentException.class,()->new VitalSigns(80,120,16,37,101,ConsciousnessLevel.ALERTA));
    }
}
