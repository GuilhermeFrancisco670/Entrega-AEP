package br.edu.filazero.domain;

public class Doctor extends Person {
    private final String specialty;
    private final String availability;
    public Doctor(long id, String name, String birthDate, String phone, String specialty, String availability) {
        super(id, name, birthDate, phone);
        this.specialty = specialty;
        this.availability = availability;
    }
    public Doctor(String name, String birthDate, String phone, String specialty, String availability) {
        this(0, name, birthDate, phone, specialty, availability);
    }
    public String getSpecialty() { return specialty; }
    public String getAvailability() { return availability; }
}
