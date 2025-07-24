package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Evento {
    private int id;
    private String titolo;
    private String sede;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private int maxIscritti;
    private int dimMaxTeam;
    private LocalDate inizioRegistrazioni;
    private LocalDate fineRegistrazioni;
    private String problema;
    private Organizzatore organizzatore;
    private List<Giudice> giudici;
    private List<Partecipante> partecipanti;
    private List<Team> teams = new ArrayList<>();
    private Giudice giudiceDescrizione;
    private List<Documento> documenti = new ArrayList<>();


    public Evento (String titolo, String sede, LocalDate dataInizio, LocalDate dataFine,int maxIscritti, int dimMaxTeam, LocalDate inizioRegistrazioni, LocalDate fineRegistrazioni, Organizzatore organizzatore, List<Giudice> giudici, List<Partecipante> partecipanti) {
        this.titolo = titolo;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.maxIscritti = maxIscritti;
        this.dimMaxTeam = dimMaxTeam;
        this.inizioRegistrazioni = inizioRegistrazioni;
        this.fineRegistrazioni = fineRegistrazioni;
        this.organizzatore = organizzatore;
        this.giudici = giudici;
        this.partecipanti = partecipanti;
    }
    public Evento (int id, String titolo, String sede, LocalDate dataInizio, LocalDate dataFine,int maxIscritti, int dimMaxTeam, LocalDate inizioRegistrazioni, LocalDate fineRegistrazioni, Organizzatore organizzatore, List<Giudice> giudici, List<Partecipante> partecipanti) {
        this.titolo = titolo;
        this.id = id;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.maxIscritti = maxIscritti;
        this.dimMaxTeam = dimMaxTeam;
        this.inizioRegistrazioni = inizioRegistrazioni;
        this.fineRegistrazioni = fineRegistrazioni;
        this.organizzatore = organizzatore;
        this.giudici = giudici;
        this.partecipanti = partecipanti;
    }

    public Evento (String titolo, String sede, LocalDate dataInizio, LocalDate dataFine, int maxIscritti, int dimMaxTeam, LocalDate inizioRegistrazioni, LocalDate fineRegistrazioni, Organizzatore organizzatore, Giudice g, Partecipante p) {
        this.titolo = titolo;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.maxIscritti = maxIscritti;
        this.dimMaxTeam = dimMaxTeam;
        this.inizioRegistrazioni = inizioRegistrazioni;
        this.fineRegistrazioni = fineRegistrazioni;
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

    public List<Giudice> getGiudici() {
        return giudici;
    }

    public List<Partecipante> getPartecipanti() {
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

    public int getMaxIscritti() {
        return maxIscritti;
    }

    public int getDMaxTeam() {
        return dimMaxTeam;
    }

    public LocalDate getInizioReg() {
        return inizioRegistrazioni;
    }

    public LocalDate getFineReg() {
        return fineRegistrazioni;
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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Giudice getGiudiceDescrizione(){
        return giudiceDescrizione;
    }

    public void setGiudiceDescrizione(Giudice giudiceDescrizione){
        this.giudiceDescrizione = giudiceDescrizione;
    }

    public List<Documento> getDocumenti() {
        return documenti;
    }

    public void setDocumenti(List<Documento> documenti) {
        this.documenti = documenti;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void validaDate() {
        LocalDate oggi = LocalDate.now();
        // 1. Durata evento = 2 giorni
        if (!dataInizio.plusDays(2).isEqual(dataFine)) {
            throw new IllegalArgumentException("La durata dell'evento deve essere di esattamente 2 giorni.");
        }
        // 2. Ordine delle date
        if (!(inizioRegistrazioni.isBefore(fineRegistrazioni)
                && fineRegistrazioni.isBefore(dataInizio)
                && dataInizio.isBefore(dataFine))) {
            throw new IllegalArgumentException("Le date non rispettano l'ordine logico richiesto.");
        }
        // 3. Nessuna data nel passato
        if (inizioRegistrazioni.isBefore(oggi) || fineRegistrazioni.isBefore(oggi) ||
                dataInizio.isBefore(oggi) || dataFine.isBefore(oggi)) {
            throw new IllegalArgumentException("Le date non possono essere nel passato.");
        }
        // 4. La fine registrazione deve essere prima dell'inizio evento
        if (!fineRegistrazioni.isBefore(dataInizio)) {
            throw new IllegalArgumentException("La fine registrazione deve essere prima dell'inizio evento.");
        }
        // 5. La durata dell'evento non può essere superiore a una settimana (opzionale)
        if (dataInizio.plusWeeks(1).isBefore(dataFine)) {
            throw new IllegalArgumentException("La durata dell'evento non può essere superiore a una settimana.");
        }
    }
}