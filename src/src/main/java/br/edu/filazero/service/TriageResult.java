package br.edu.filazero.service;

import br.edu.filazero.domain.PriorityLevel;

public record TriageResult(long id, int score, PriorityLevel priority) {}
