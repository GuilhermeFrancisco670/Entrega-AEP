package br.edu.filazero.dao;

import br.edu.filazero.config.Database;
import br.edu.filazero.domain.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PatientDao {
    public Patient create(Patient p) throws SQLException {
        String personSql = "INSERT INTO person(name,birth_date,phone) VALUES(?,?,?) RETURNING id";
        try (Connection c = Database.connect()) {
            c.setAutoCommit(false);
            try {
                long id;
                try (PreparedStatement s = c.prepareStatement(personSql)) {
                    s.setString(1, p.getName());
                    s.setDate(2, Date.valueOf(p.getBirthDate()));
                    s.setString(3, p.getPhone());
                    try (ResultSet r = s.executeQuery()) { r.next(); id = r.getLong(1); }
                }
                try (PreparedStatement s = c.prepareStatement("INSERT INTO patient(person_id) VALUES(?)")) {
                    s.setLong(1, id);
                    s.executeUpdate();
                }
                c.commit();
                return new Patient(id, p.getName(), p.getBirthDate(), p.getPhone());
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }
    public Optional<Patient> findById(long id) throws SQLException {
        String sql="SELECT p.id,p.name,p.birth_date,p.phone FROM person p JOIN patient x ON x.person_id=p.id WHERE p.id=?";
        try(Connection c=Database.connect(); PreparedStatement s=c.prepareStatement(sql)){s.setLong(1,id);try(ResultSet r=s.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}
    }
    public List<Patient> findAll() throws SQLException {
        List<Patient> out=new ArrayList<>(); String sql="SELECT p.id,p.name,p.birth_date,p.phone FROM person p JOIN patient x ON x.person_id=p.id ORDER BY p.id";
        try(Connection c=Database.connect(); PreparedStatement s=c.prepareStatement(sql);ResultSet r=s.executeQuery()){while(r.next())out.add(map(r));} return out;
    }
    public boolean update(Patient p) throws SQLException {
        String sql="UPDATE person SET name=?,birth_date=?,phone=? WHERE id=? AND EXISTS(SELECT 1 FROM patient WHERE person_id=?)";
        try(Connection c=Database.connect();PreparedStatement s=c.prepareStatement(sql)){s.setString(1,p.getName());s.setDate(2,Date.valueOf(p.getBirthDate()));s.setString(3,p.getPhone());s.setLong(4,p.getId());s.setLong(5,p.getId());return s.executeUpdate()==1;}
    }
    public boolean delete(long id) throws SQLException {
        try (Connection c = Database.connect()) {
            c.setAutoCommit(false);
            try {
                int patientRows;
                try (PreparedStatement s = c.prepareStatement("DELETE FROM patient WHERE person_id=?")) {
                    s.setLong(1, id);
                    patientRows = s.executeUpdate();
                }
                if (patientRows == 0) { c.rollback(); return false; }
                try (PreparedStatement s = c.prepareStatement("DELETE FROM person WHERE id=?")) {
                    s.setLong(1, id);
                    s.executeUpdate();
                }
                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }
    private Patient map(ResultSet r)throws SQLException{return new Patient(r.getLong("id"),r.getString("name"),r.getDate("birth_date").toString(),r.getString("phone"));}
}
