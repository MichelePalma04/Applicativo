import model.*;

import java.util.ArrayList;
import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Utente u1 = new Utente("mike@gmail.com", "mikemike");

        System.out.println(u1.getLogin());
        System.out.println(u1.getPassword());

        LocalDate dataInizio = LocalDate.of(2025, 6, 9);
        LocalDate dataFine = LocalDate.of(2025, 6, 11);
        LocalDate inizioReg = LocalDate.of(2025, 6, 1);
        LocalDate fineReg = LocalDate.of(2025, 6, 7);

        Team t1 = new Team ("Sport",new ArrayList<>(), new ArrayList<>());
        Organizzatore o = new Organizzatore("mike@mail", "0906", new ArrayList<>());
        Partecipante p1 = new Partecipante("carlacasa", "pallina", t1, new ArrayList<>());
        t1.addPartecipanti(p1);
        Giudice g1 = new Giudice("saretta", "0611", new ArrayList<>(), new ArrayList<>());
        Voto v1 = new Voto (g1, t1, 9);
        g1.addVoti(v1);
        t1.addVoto(v1);
        Evento e1 = new Evento("hackathon", "Milano", dataInizio, dataFine, 20, 5, inizioReg, fineReg, o, g1, p1 );
        Evento e2 = new Evento ("sport", "roma", dataInizio, dataFine, 20, 5, inizioReg, fineReg, o, g1, p1 );
        p1.addEventi(e1);
        g1.addEventi(e2);
        o.addEventi(e1);
        o.addEventi(e2);
        /*System.out.println("Eventi gestiti da " + o.getLogin()+ ":");
        o.getEventi().forEach(e -> System.out.println("- "+ e.getTitolo() + " a " + e.getSede() + " dal "+e.getDataInizio() + " a " + e.getDataFine()+ " con " +e.getN_Max_Iscritti()+ " con " +e.getOrganizzatore()));
        */
    }
}