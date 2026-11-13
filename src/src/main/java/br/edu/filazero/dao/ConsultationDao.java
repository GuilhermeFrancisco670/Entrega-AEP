package br.edu.filazero.dao;

import br.edu.filazero.config.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ConsultationDao {
    public void recordStart(long triageId,long doctorId)throws SQLException {
        String sql="INSERT INTO consultation(triage_id,doctor_id,started_at) VALUES(?,?,CURRENT_TIMESTAMP)";
        try(Connection c=Database.connect();PreparedStatement s=c.prepareStatement(sql)){s.setLong(1,triageId);s.setLong(2,doctorId);s.executeUpdate();}
    }
    public void recordFinish(long triageId)throws SQLException {
        String sql="UPDATE consultation SET finished_at=CURRENT_TIMESTAMP WHERE triage_id=? AND finished_at IS NULL";
        try(Connection c=Database.connect();PreparedStatement s=c.prepareStatement(sql)){s.setLong(1,triageId);if(s.executeUpdate()!=1)throw new SQLException("Atendimento aberto não encontrado.");}
    }
    public List<String> averageWaitByPriority()throws SQLException {
        List<String> out=new ArrayList<>();String sql="SELECT t.priority,ROUND(AVG(EXTRACT(EPOCH FROM (c.started_at-t.created_at))/60.0)::numeric,2) AS avg_minutes,COUNT(*) AS total FROM consultation c JOIN triage t ON t.id=c.triage_id WHERE c.finished_at IS NOT NULL GROUP BY t.priority ORDER BY CASE t.priority WHEN 'ALTA' THEN 3 WHEN 'MEDIA' THEN 2 ELSE 1 END DESC";
        try(Connection c=Database.connect();PreparedStatement s=c.prepareStatement(sql);ResultSet r=s.executeQuery()){while(r.next())out.add(r.getString("priority")+": média de "+r.getBigDecimal("avg_minutes")+" minuto(s), atendimentos concluídos: "+r.getLong("total"));}return out;
    }
}
