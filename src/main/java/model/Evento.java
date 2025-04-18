package model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Evento {
    private String titolo;
    private String sede;
    private Date dataInizio;
    private Date dataFine;
    private int n_Max_Iscritti;
    private int dim_max_team;
    private Date inizio_registrazioni;
    private Date fine_registrazioni;
    private String problema;
    private Organizzatore organizzatore;
    //private List<Giudice> giudiceList;
    //private List<Partecipante> partecipanteList;

    public Evento (String titolo, String sede, Date dataInizio, Date dataFine, int n_Max_Iscritti, int dim_max_team, Date inizio_registrazioni, Date fine_registrazioni, Organizzatore organizzatore) {
        this.titolo = titolo;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.n_Max_Iscritti = n_Max_Iscritti;
        this.dim_max_team = dim_max_team;
        this.inizio_registrazioni = inizio_registrazioni;
        this.fine_registrazioni = fine_registrazioni;
        this.organizzatore = organizzatore;

    }

    public String getTitolo() {return titolo;}
    public String getSede() {return sede;}
    public Date getDataInizio() {return dataInizio;}
    public Date getDataFine() {return dataFine;}
    public int getN_Max_Iscritti() {return n_Max_Iscritti;}
    public int getDim_max_team() {return dim_max_team;}
    public Date getInizio_registrazioni() { return inizio_registrazioni;}
    public Date getFine_registrazioni() { return fine_registrazioni;}
    public Organizzatore getOrganizzatore() { return organizzatore;}

    public void setProblema (String problema){
        this.problema = problema;
    }
}