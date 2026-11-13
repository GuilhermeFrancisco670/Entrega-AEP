package br.edu.filazero.service;

import br.edu.filazero.dao.PatientDao;
import br.edu.filazero.domain.Patient;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public final class PatientService {
    private final PatientDao dao;
    public PatientService(PatientDao dao){this.dao=dao;}
    public Patient create(String name,String birthDate,String phone)throws SQLException {validate(name,birthDate);return dao.create(new Patient(name.trim(),birthDate,blankToNull(phone)));}
    public List<Patient> all()throws SQLException{return dao.findAll();}
    public Optional<Patient> find(long id)throws SQLException{return dao.findById(id);}
    public boolean update(long id,String name,String birthDate,String phone)throws SQLException {validate(name,birthDate);return dao.update(new Patient(id,name.trim(),birthDate,blankToNull(phone)));}
    public boolean delete(long id)throws SQLException {return dao.delete(id);}
    private void validate(String name,String date){if(name==null||name.isBlank())throw new IllegalArgumentException("Nome obrigatório.");try{if(LocalDate.parse(date).isAfter(LocalDate.now()))throw new IllegalArgumentException("Data de nascimento não pode ser futura.");}catch(java.time.format.DateTimeParseException e){throw new IllegalArgumentException("Data deve estar no formato AAAA-MM-DD.");}}
    private String blankToNull(String s){return s==null||s.isBlank()?null:s.trim();}
}
