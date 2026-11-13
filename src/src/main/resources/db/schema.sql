-- FilaZero — PostgreSQL
-- Protótipo acadêmico. Pontuação não clínica; usar apenas dados sintéticos.
CREATE TABLE IF NOT EXISTS person (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(150) NOT NULL CHECK (length(trim(name)) > 0),
    birth_date DATE NOT NULL,
    phone VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS patient (
    person_id BIGINT PRIMARY KEY REFERENCES person(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS doctor (
    person_id BIGINT PRIMARY KEY REFERENCES person(id) ON DELETE RESTRICT,
    specialty VARCHAR(120) NOT NULL CHECK (length(trim(specialty)) > 0),
    availability TEXT NOT NULL CHECK (length(trim(availability)) > 0)
);

CREATE TABLE IF NOT EXISTS triage (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patient(person_id) ON DELETE RESTRICT,
    heart_rate INTEGER NOT NULL CHECK (heart_rate BETWEEN 1 AND 300),
    systolic_pressure INTEGER NOT NULL CHECK (systolic_pressure BETWEEN 1 AND 350),
    respiratory_rate INTEGER NOT NULL CHECK (respiratory_rate BETWEEN 1 AND 100),
    temperature_celsius NUMERIC(4,1) NOT NULL CHECK (temperature_celsius BETWEEN 20 AND 50),
    oxygen_saturation INTEGER NOT NULL CHECK (oxygen_saturation BETWEEN 0 AND 100),
    consciousness VARCHAR(20) NOT NULL CHECK (consciousness IN ('ALERTA','RESPOSTA_A_VOZ','RESPOSTA_A_DOR','SEM_RESPOSTA')),
    score INTEGER NOT NULL CHECK (score >= 0),
    priority VARCHAR(10) NOT NULL CHECK (priority IN ('BAIXA','MEDIA','ALTA')),
    status VARCHAR(20) NOT NULL DEFAULT 'WAITING' CHECK (status IN ('WAITING','IN_PROGRESS','COMPLETED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_triage_queue ON triage(status, priority, created_at);
CREATE INDEX IF NOT EXISTS idx_triage_patient ON triage(patient_id);

CREATE TABLE IF NOT EXISTS consultation (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    triage_id BIGINT NOT NULL UNIQUE REFERENCES triage(id) ON DELETE RESTRICT,
    doctor_id BIGINT NOT NULL REFERENCES doctor(person_id) ON DELETE RESTRICT,
    started_at TIMESTAMPTZ NOT NULL,
    finished_at TIMESTAMPTZ,
    CHECK (finished_at IS NULL OR finished_at >= started_at)
);
CREATE INDEX IF NOT EXISTS idx_consultation_doctor ON consultation(doctor_id);
