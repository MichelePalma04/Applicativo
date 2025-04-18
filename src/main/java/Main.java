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

        /*Giudice g1 = new Giudice("micheleoalma@gmail.com", "mike");
        Team team1 = new Team("Sport");
        Voto v1 = new Voto(g1, team1, 9);
        */
        


        Organizzatore o = new Organizzatore("mike@mail", "0906", new ArrayList<>());
        Evento e1 = new Evento("hackathon", "Milano", dataInizio, dataFine, 20, 5, inizioReg, fineReg, o );
        Evento e2 = new Evento ("sport", "roma", dataInizio, dataFine, 20, 5, inizioReg, fineReg, o );
        o.addEventi(e1);
        o.addEventi(e2);
        System.out.println("Eventi gestiti da " + o.getLogin()+ ":");
        o.getEventi().forEach(e -> System.out.println("- "+ e.getTitolo() + " a " + e.getSede() + " dal "+e.getDataInizio() + " a " + e.getDataFine()+ " con " +e.getN_Max_Iscritti()+ " con " +e.getOrganizzatore()));
    }
}