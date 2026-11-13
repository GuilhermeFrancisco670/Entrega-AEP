package br.edu.filazero.dao;

import br.edu.filazero.config.Database;
import br.edu.filazero.domain.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DoctorDao {
    public Doctor create(Doctor d)throws SQLException {
        try(Connection c=Database.connect()) {
            c.setAutoCommit(false);
            try {
                long id;
                try(PreparedStatement s=c.prepareStatement("INSERT INTO person(name,birth_date,phone) VALUES(?,?,?) RETURNING id")){
                    s.setString(1,d.getName());s.setDate(2,Date.valueOf(d.getBirthDate()));s.setString(3,d.getPhone());try(ResultSet r=s.executeQuery()){r.next();id=r.getLong(1);}}
                try(PreparedStatement s=c.prepareStatement("INSERT INTO doctor(person_id,specialty,availability) VALUES(?,?,?)")){s.setLong(1,id);s.setString(2,d.getSpecialty());s.setString(3,d.getAvailability());s.executeUpdate();}
                c.commit();return new Doctor(id,d.getName(),d.getBirthDate(),d.getPhone(),d.getSpecialty(),d.getAvailability());
            } catch(SQLException e){c.rollback();throw e;}
        }
    }
    public List<Doctor> findAll()throws SQLException {
        List<Doctor> out=new ArrayList<>();String sql="SELECT p.id,p.name,p.birth_date,p.phone,d.specialty,d.availability FROM person p JOIN doctor d ON d.person_id=p.id ORDER BY p.id";
        try(Connection c=Database.connect();PreparedStatement s=c.prepareStatement(sql);ResultSet r=s.executeQuery()){while(r.next())out.add(new Doctor(r.getLong("id"),r.getString("name"),r.getDate("birth_date").toString(),r.getString("phone"),r.getString("specialty"),r.getString("availability")));}return out;
    }
}
