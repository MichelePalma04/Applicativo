package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Evento {
    private int id;
    private String titolo;
    private String sede;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int n_Max_Iscritti;
    private int dim_max_team;
    private LocalDate inizio_registrazioni;
    private LocalDate fine_registrazioni;
    private String problema;
    private Organizzatore organizzatore;
    private ArrayList<Giudice> giudici;
    private ArrayList<Partecipante> partecipanti;
    private ArrayList<Team> teams = new ArrayList<>();
    private Giudice giudiceDescrizione;
    private ArrayList<Documento> documenti = new ArrayList<>();


    public Evento (String titolo, String sede, LocalDate dataInizio, LocalDate dataFine, int n_Max_Iscritti, int dim_max_team, LocalDate inizio_registrazioni, LocalDate fine_registrazioni, Organizzatore organizzatore, ArrayList<Giudice> giudici, ArrayList<Partecipante> partecipanti) {
        this.titolo = titolo;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.n_Max_Iscritti = n_Max_Iscritti;
        this.dim_max_team = dim_max_team;
        this.inizio_registrazioni = inizio_registrazioni;
        this.fine_registrazioni = fine_registrazioni;
        this.organizzatore = organizzatore;
        this.giudici = giudici;
        this.partecipanti = partecipanti;
    }

    public Evento (String titolo, String sede, LocalDate dataInizio, LocalDate dataFine, int n_Max_Iscritti, int dim_max_team, LocalDate inizio_registrazioni, LocalDate fine_registrazioni, Organizzatore organizzatore, Giudice g, Partecipante p) {
        this.titolo = titolo;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.n_Max_Iscritti = n_Max_Iscritti;
        this.dim_max_team = dim_max_team;
        this.inizio_registrazioni = inizio_registrazioni;
        this.fine_registrazioni = fine_registrazioni;
        this.organizzatore = organizzatore;
        giudici = new ArrayList<>();
        giudici.add(g);
        partecipanti = new ArrayList<>();
        partecipanti.add(p);
    }

    public void addGiudici (Giudice newG){
        if(!this.giudici.contains(newG)){
            this.giudici.add(newG);
        }
    }

    public void addPartecipanti (Partecipante newP){
        if(!this.partecipanti.contains(newP)){
            this.partecipanti.add(newP);
        }
    }

    public ArrayList<Giudice> getGiudici() {
        return giudici;
    }

    public ArrayList<Partecipante> getPartecipanti() {
        return partecipanti;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getSede() {
        return sede;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public int getN_Max_Iscritti() {
        return n_Max_Iscritti;
    }

    public int getDim_max_team() {
        return dim_max_team;
    }

    public LocalDate getInizio_registrazioni() {
        return inizio_registrazioni;
    }

    public LocalDate getFine_registrazioni() {
        return fine_registrazioni;
    }

    public Organizzatore getOrganizzatore() {
        return organizzatore;
    }

    public void setOrganizzatore(Organizzatore organizzatore) {
        this.organizzatore = organizzatore;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema (String problema){
        this.problema = problema;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public Giudice getGiudiceDescrizione(){
        return giudiceDescrizione;
    }

    public void setGiudiceDescrizione(Giudice giudiceDescrizione){
        this.giudiceDescrizione = giudiceDescrizione;
    }

    public ArrayList<Documento> getDocumenti() {
        return documenti;
    }

    public void setDocumenti(ArrayList<Documento> documenti) {
        this.documenti = documenti;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}