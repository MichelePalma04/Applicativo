import model.*;

import java.util.ArrayList;
import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

//prove generali stampa
 /*     LocalDate dataInizio = LocalDate.of(2025, 6, 9);
        LocalDate dataFine = LocalDate.of(2025, 6, 11);
        LocalDate inizioReg = LocalDate.of(2025, 6, 1);
        LocalDate fineReg = LocalDate.of(2025, 6, 7);

        Team t1 = new Team ("Sport",new ArrayList<>(), new ArrayList<>());
        Team t2= new Team ("Acqua", new ArrayList<>(), new ArrayList<>());
        Organizzatore o1 = new Organizzatore("mike@mail", "0906", new ArrayList<>(), new ArrayList<>());
        Organizzatore o2 = new Organizzatore("fra@mail", "cane", new ArrayList<>(), new ArrayList<>());
        Partecipante p1 = new Partecipante("carlacasa", "pallina", t1, new ArrayList<>());
        Partecipante p2 = new Partecipante ("paolo63", "bombetta", t1, new ArrayList<>());
        Partecipante p3 = new Partecipante ("cat45", "capriola", t2, new ArrayList<>());
        t1.addPartecipanti(p1);
        t1.addPartecipanti(p3);
        t2.addPartecipanti(p1);
        Giudice g1 = new Giudice("saretta", "0611", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Giudice g2 = new Giudice ("mikele", "pallottola", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        o1.addGiudici(g1);
        o2.addGiudici(g2);
        g1.addOrganizzatore(o1);
        g2.addOrganizzatore(o1);
        Voto v1 = new Voto (g1, t1, 9);
        Voto v2 = new Voto (g2, t1, 8);
        g2.addVoti(v2);
        g1.addVoti(v1);
        t1.addVoto(v1);
        t2.addVoto(v1);
        Evento e1 = new Evento("hackathon", "Milano", dataInizio, dataFine, 20, 5, inizioReg, fineReg, o1, g1, p1 );
        Evento e2 = new Evento ("sport", "roma", dataInizio, dataFine, 20, 5, inizioReg, fineReg, o2, g2, p1 );
        p1.addEventi(e1);
        g1.addEventi(e2);
        g2.addEventi(e2);
        g2.addEventi(e1);
        p2.addEventi(e1);
        p3.addEventi(e2);
        //g1.addEventi(e2);

        g1.addEventi(e1);
        o1.addEventi(e1);
        o1.addEventi(e2);

        System.out.println("L'organizzatore del/degli evento/i: "+o1.getEventi() + " è " + e1.getOrganizzatore());
        System.out.println("L'evento "+ g1.getEventi() +" ha come giudici "+ e1.getGiudici());
        System.out.println("L'evento "+ g2.getEventi() +" ha come giudici "+ e2.getGiudici());
        System.out.println(e2.getOrganizzatore() + " è l'organizzatore dell'evento " + o2.getEventi()+ " e ha scelto come giudici " +e2.getGiudici());
        System.out.println(t1 + " "+t1.getPartecipanti());
        System.out.println(t2 + " "+t2.getPartecipanti());
        System.out.println(g1.getVoti());
        System.out.println(o1.getEventi());
        System.out.println(o2.getEventi());
        /*System.out.println("Eventi gestiti da " + o.getLogin()+ ":");
        o.getEventi().forEach(e -> System.out.println("- "+ e.getTitolo() + " a " + e.getSede() + " dal "+e.getDataInizio() + " a " + e.getDataFine()+ " con " +e.getN_Max_Iscritti()+ " con " +e.getOrganizzatore()));
        */
    }
}