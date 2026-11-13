package br.edu.filazero.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PriorityLevelTest {
    @Test void mapsApprovedScoreBands(){
        assertEquals(PriorityLevel.BAIXA,PriorityLevel.fromScore(2));
        assertEquals(PriorityLevel.MEDIA,PriorityLevel.fromScore(3));
        assertEquals(PriorityLevel.MEDIA,PriorityLevel.fromScore(5));
        assertEquals(PriorityLevel.ALTA,PriorityLevel.fromScore(6));
    }
}
