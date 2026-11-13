package br.edu.filazero.dao;

import br.edu.filazero.config.Database;
import br.edu.filazero.domain.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class TriageDao {
    public long create(long patientId,VitalSigns v,int score,PriorityLevel p)throws SQLException {
        String sql="INSERT INTO triage(patient_id,heart_rate,systolic_pressure,respiratory_rate,temperature_celsius,oxygen_saturation,consciousness,score,priority,status) VALUES(?,?,?,?,?,?,?,?,?,'WAITING') RETURNING id";
        try(Connection c=Database.connect();PreparedStatement s=c.prepareStatement(sql)){
            s.setLong(1,patientId);s.setInt(2,v.heartRate());s.setInt(3,v.systolicPressure());s.setInt(4,v.respiratoryRate());s.setDouble(5,v.temperatureCelsius());s.setInt(6,v.oxygenSaturation());s.setString(7,v.consciousness().name());s.setInt(8,score);s.setString(9,p.name());try(ResultSet r=s.executeQuery()){r.next();return r.getLong(1);}
        }
    }
    public List<String> waitingQueue()throws SQLException {
        List<String> out=new ArrayList<>();String sql="SELECT t.id,p.name,t.score,t.priority,t.created_at FROM triage t JOIN patient pa ON pa.person_id=t.patient_id JOIN person p ON p.id=pa.person_id WHERE t.status='WAITING' ORDER BY CASE t.priority WHEN 'ALTA' THEN 3 WHEN 'MEDIA' THEN 2 ELSE 1 END DESC,t.created_at ASC,t.id ASC";
        try(Connection c=Database.connect();PreparedStatement s=c.prepareStatement(sql);ResultSet r=s.executeQuery()){while(r.next())out.add(String.format("Triagem %d | %s | score %d | prioridade %s | entrada %s",r.getLong("id"),r.getString("name"),r.getInt("score"),PriorityLevel.valueOf(r.getString("priority")).label(),r.getTimestamp("created_at")));}return out;
    }
    public boolean start(long triageId)throws SQLException {
        String sql="UPDATE triage SET status='IN_PROGRESS' WHERE id=? AND status='WAITING'";
        try(Connection c=Database.connect();PreparedStatement s=c.prepareStatement(sql)){s.setLong(1,triageId);return s.executeUpdate()==1;}
    }
    public void resetToWaiting(long triageId) throws SQLException {
        String sql="UPDATE triage SET status='WAITING' WHERE id=? AND status='IN_PROGRESS'";
        try(Connection c=Database.connect();PreparedStatement s=c.prepareStatement(sql)){s.setLong(1,triageId);s.executeUpdate();}
    }
    public boolean finish(long triageId)throws SQLException {
        String sql="UPDATE triage SET status='COMPLETED' WHERE id=? AND status='IN_PROGRESS'";
        try(Connection c=Database.connect();PreparedStatement s=c.prepareStatement(sql)){s.setLong(1,triageId);return s.executeUpdate()==1;}
    }
}
