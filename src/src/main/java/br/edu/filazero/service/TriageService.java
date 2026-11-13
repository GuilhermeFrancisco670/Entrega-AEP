package br.edu.filazero.service;

import br.edu.filazero.dao.TriageDao;
import br.edu.filazero.domain.*;
import java.sql.SQLException;

public final class TriageService {
    private final DemoScorer scorer; private final TriageDao dao;
    public TriageService(DemoScorer scorer,TriageDao dao){this.scorer=scorer;this.dao=dao;}
    public TriageResult register(long patientId,VitalSigns signs)throws SQLException {
        if(patientId<=0)throw new IllegalArgumentException("Paciente inválido.");
        int score=scorer.score(signs);PriorityLevel priority=PriorityLevel.fromScore(score);long id=dao.create(patientId,signs,score,priority);return new TriageResult(id,score,priority);
    }
}
