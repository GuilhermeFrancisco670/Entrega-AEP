package br.edu.filazero.domain;

public class Patient extends Person {
    public Patient(long id, String name, String birthDate, String phone) { super(id, name, birthDate, phone); }
    public Patient(String name, String birthDate, String phone) { this(0, name, birthDate, phone); }
}
