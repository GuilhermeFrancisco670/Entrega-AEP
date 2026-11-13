package br.edu.filazero.ui;

import br.edu.filazero.dao.*;
import br.edu.filazero.domain.*;
import br.edu.filazero.service.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public final class ConsoleApp {
    private final Scanner in=new Scanner(System.in);
    private final PatientService patients=new PatientService(new PatientDao());
    private final DoctorDao doctors=new DoctorDao(); private final TriageDao triages=new TriageDao();
    private final TriageService triageService=new TriageService(new DemoScorer(),triages);
    private final ConsultationDao consultations=new ConsultationDao();

    public void run(){
        System.out.println("FilaZero — protótipo acadêmico. A pontuação é fictícia, não clínica; não use para decisões de atendimento.");
        try(var c=br.edu.filazero.config.Database.connect()){System.out.println("Conexão com PostgreSQL OK.");}catch(SQLException e){System.err.println("Não foi possível conectar ao PostgreSQL: "+e.getMessage());return;}
        while(true){menu();int op=readInt("Opção: ");try{switch(op){case 1->createPatient();case 2->listPatients();case 3->updatePatient();case 4->deletePatient();case 5->createDoctor();case 6->listDoctors();case 7->createTriage();case 8->showQueue();case 9->startConsultation();case 10->finishConsultation();case 11->report();case 0->{System.out.println("Até logo.");return;}default->System.out.println("Opção inválida.");}}catch(SQLException|IllegalArgumentException e){System.out.println("Operação não concluída: "+e.getMessage());}}
    }
    private void menu(){System.out.println("\n1 Cadastrar paciente | 2 Listar pacientes | 3 Atualizar paciente | 4 Excluir paciente\n5 Cadastrar médico | 6 Listar médicos | 7 Registrar triagem | 8 Ver fila\n9 Iniciar atendimento | 10 Encerrar atendimento | 11 Relatório de espera | 0 Sair");}
    private void createPatient()throws SQLException{Patient p=patients.create(read("Nome: "),read("Nascimento (AAAA-MM-DD): "),read("Telefone (opcional): "));System.out.println("Paciente cadastrado. ID: "+p.getId());}
    private void listPatients()throws SQLException{List<Patient> list=patients.all();if(list.isEmpty())System.out.println("Nenhum paciente cadastrado.");for(Patient p:list)System.out.printf("%d | %s | %s | %s%n",p.getId(),p.getName(),p.getBirthDate(),p.getPhone()==null?"-":p.getPhone());}
    private void updatePatient()throws SQLException{long id=readLong("ID: ");boolean ok=patients.update(id,read("Nome: "),read("Nascimento (AAAA-MM-DD): "),read("Telefone (opcional): "));System.out.println(ok?"Paciente atualizado.":"Paciente não encontrado.");}
    private void deletePatient()throws SQLException{long id=readLong("ID: ");try{System.out.println(patients.delete(id)?"Paciente excluído.":"Paciente não encontrado.");}catch(SQLException e){if("23503".equals(e.getSQLState()))System.out.println("Paciente possui triagem/atendimento relacionado e não pode ser excluído.");else throw e;}}
    private void createDoctor()throws SQLException{Doctor d=doctors.create(new Doctor(read("Nome: "),read("Nascimento (AAAA-MM-DD): "),read("Telefone (opcional): "),read("Especialidade: "),read("Disponibilidade (texto): ")));System.out.println("Médico cadastrado. ID: "+d.getId());}
    private void listDoctors()throws SQLException{for(Doctor d:doctors.findAll())System.out.printf("%d | %s | %s | %s%n",d.getId(),d.getName(),d.getSpecialty(),d.getAvailability());}
    private void createTriage()throws SQLException{long id=readLong("ID do paciente: ");VitalSigns s=new VitalSigns(readInt("Frequência cardíaca (bpm): "),readInt("Pressão sistólica (mmHg): "),readInt("Frequência respiratória (irpm): "),readDouble("Temperatura (°C): "),readInt("Saturação (%): "),readConsciousness());TriageResult result=triageService.register(id,s);System.out.println("Triagem "+result.id()+" registrada: score "+result.score()+", prioridade "+result.priority().label()+" (regra fictícia).");}
    private ConsciousnessLevel readConsciousness(){System.out.println("Consciência: 1 Alerta | 2 Resposta à voz | 3 Resposta à dor | 4 Sem resposta");return ConsciousnessLevel.fromOption(readInt("Opção: "));}
    private void showQueue()throws SQLException{List<String> q=triages.waitingQueue();if(q.isEmpty())System.out.println("Fila vazia.");q.forEach(System.out::println);}
    private void startConsultation()throws SQLException{long t=readLong("ID da triagem: ");long d=readLong("ID do médico: ");if(!triages.start(t)){System.out.println("Triagem não está aguardando.");return;}try{consultations.recordStart(t,d);System.out.println("Atendimento iniciado.");}catch(SQLException e){triages.resetToWaiting(t);throw e;}}
    private void finishConsultation()throws SQLException{long t=readLong("ID da triagem: ");consultations.recordFinish(t);triages.finish(t);System.out.println("Atendimento encerrado.");}
    private void report()throws SQLException{List<String> rows=consultations.averageWaitByPriority();if(rows.isEmpty())System.out.println("Ainda não há atendimentos concluídos.");rows.forEach(System.out::println);}
    private String read(String prompt){System.out.print(prompt);return in.nextLine().trim();}
    private int readInt(String prompt){return Integer.parseInt(read(prompt));}
    private long readLong(String prompt){return Long.parseLong(read(prompt));}
    private double readDouble(String prompt){return Double.parseDouble(read(prompt).replace(',','.'));}
}
