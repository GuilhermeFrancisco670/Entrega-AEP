package br.edu.filazero.domain;

public abstract class Person {
    private final long id;
    private final String name;
    private final String birthDate;
    private final String phone;

    protected Person(long id, String name, String birthDate, String phone) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
    }
    public long getId() { return id; }
    public String getName() { return name; }
    public String getBirthDate() { return birthDate; }
    public String getPhone() { return phone; }
}
