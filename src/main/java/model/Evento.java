package model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un evento hackathon.
 * Un Evento è organizzato da un Organizzatore, può avere più Giudici, Partecipanti, Team e Documenti associati.
 * Tiene traccia delle date rilevanti, dei limiti di iscrizione, del problema proposto e di chi ne è responsabile.
 */
public class Evento {

    /** Identificativo univoco dell'evento. */
    private int id;

    /** Titolo dell'evento. */
    private String titolo;

    /** Sede di svolgimento dell'evento. */
    private String sede;

    /** Data di inizio dell'evento. */
    private LocalDate dataInizio;

    /** Data di fine dell'evento. */
    private LocalDate dataFine;

    /** Numero massimo di iscritti all'evento. */
    private int maxIscritti;

    /** Dimensione massima dei team partecipanti. */
    private int dimMaxTeam;

    /** Data di apertura delle registrazioni. */
    private LocalDate inizioRegistrazioni;

    /** Data di chiusura delle registrazioni. */
    private LocalDate fineRegistrazioni;

    /** Problema da risolvere proposto durante l'evento. */
    private String problema;

    /** Organizzatore responsabile dell'evento. */
    private Organizzatore organizzatore;

    /** Lista dei giudici invitati all'evento. */
    private List<Giudice> giudici;

    /** Lista dei partecipanti iscritti all'evento. */
    private List<Partecipante> partecipanti;

    /** Lista dei team partecipanti. */
    private List<Team> teams = new ArrayList<>();

    /** Giudice responsabile della descrizione del problema. */
    private Giudice giudiceDescrizione;

    /** Lista dei documenti caricati durante l'evento. */
    private List<Documento> documenti = new ArrayList<>();

    /**
     * Costruttore per creare un evento senza id.
     * @param titolo titolo dell'evento
     * @param sede sede dell'evento
     * @param dataInizio data di inizio
     * @param dataFine data di fine
     * @param maxIscritti massimo numero di iscritti
     * @param dimMaxTeam dimensione massima dei team
     * @param inizioRegistrazioni data di apertura registrazioni
     * @param fineRegistrazioni data di chiusura registrazioni
     * @param organizzatore organizzatore responsabile
     * @param giudici lista dei giudici
     * @param partecipanti lista dei partecipanti
     */
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

    /**
     * Costruttore per creare un evento con id.
     * @param id identificativo dell'evento
     * @param titolo titolo dell'evento
     * @param sede sede dell'evento
     * @param dataInizio data di inizio
     * @param dataFine data di fine
     * @param maxIscritti massimo numero di iscritti
     * @param dimMaxTeam dimensione massima dei team
     * @param inizioRegistrazioni data di apertura registrazioni
     * @param fineRegistrazioni data di chiusura registrazioni
     * @param organizzatore organizzatore responsabile
     * @param giudici lista dei giudici
     * @param partecipanti lista dei partecipanti
     */
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

    /**
     * Costruttore alternativo per evento con un solo giudice e partecipante.
     * @param titolo titolo dell'evento
     * @param sede sede dell'evento
     * @param dataInizio data di inizio
     * @param dataFine data di fine
     * @param maxIscritti massimo numero di iscritti
     * @param dimMaxTeam dimensione massima dei team
     * @param inizioRegistrazioni data di apertura registrazioni
     * @param fineRegistrazioni data di chiusura registrazioni
     * @param organizzatore organizzatore responsabile
     * @param g giudice
     * @param p partecipante
     */
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

    /**
     * Aggiunge un giudice alla lista dei giudici dell'evento.
     * @param newG giudice da aggiungere
     */
    public void addGiudici (Giudice newG){
        if(!this.giudici.contains(newG)){
            this.giudici.add(newG);
        }
    }

    /**
     * Aggiunge un partecipante alla lista dei partecipanti dell'evento.
     * @param newP partecipante da aggiungere
     */
    public void addPartecipanti (Partecipante newP){
        if(!this.partecipanti.contains(newP)){
            this.partecipanti.add(newP);
        }
    }

    /**
     * Restituisce la lista dei giudici dell'evento.
     * @return lista dei giudici
     */
    public List<Giudice> getGiudici() {
        return giudici;
    }

    /**
     * Restituisce la lista dei partecipanti dell'evento.
     * @return lista dei partecipanti
     */
    public List<Partecipante> getPartecipanti() {
        return partecipanti;
    }

    /**
     * Restituisce il titolo dell'evento.
     * @return titolo dell'evento
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Restituisce la sede dell'evento.
     * @return sede dell'evento
     */
    public String getSede() {
        return sede;
    }

    /**
     * Restituisce la data di inizio dell'evento.
     * @return data di inizio
     */
    public LocalDate getDataInizio() {
        return dataInizio;
    }

    /**
     * Restituisce la data di fine dell'evento.
     * @return data di fine
     */
    public LocalDate getDataFine() {
        return dataFine;
    }

    /**
     * Restituisce il numero massimo di iscritti.
     * @return massimo numero di iscritti
     */
    public int getMaxIscritti() {
        return maxIscritti;
    }

    /**
     * Restituisce la dimensione massima dei team.
     * @return dimensione massima dei team
     */
    public int getDMaxTeam() {
        return dimMaxTeam;
    }

    /**
     * Restituisce la data di apertura delle registrazioni.
     * @return data di apertura delle registrazioni
     */
    public LocalDate getInizioReg() {
        return inizioRegistrazioni;
    }

    /**
     * Restituisce la data di chiusura delle registrazioni.
     * @return data di chiusura delle registrazioni
     */
    public LocalDate getFineReg() {
        return fineRegistrazioni;
    }

    /**
     * Restituisce l'organizzatore dell'evento.
     * @return organizzatore
     */
    public Organizzatore getOrganizzatore() {
        return organizzatore;
    }

    /**
     * Imposta l'organizzatore dell'evento.
     * @param organizzatore organizzatore da impostare
     */
    public void setOrganizzatore(Organizzatore organizzatore) {
        this.organizzatore = organizzatore;
    }

    /**
     * Restituisce il problema proposto dell'evento.
     * @return problema dell'evento
     */
    public String getProblema() {
        return problema;
    }

    /**
     * Imposta il problema da risolvere dell'evento.
     * @param problema problema da impostare
     */
    public void setProblema (String problema){
        this.problema = problema;
    }

    /**
     * Restituisce la lista dei team partecipanti.
     * @return lista dei team
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Imposta la lista dei team partecipanti.
     * @param teams lista dei team
     */
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    /**
     * Restituisce il giudice responsabile della descrizione del problema.
     * @return giudice responsabile della descrizione
     */
    public Giudice getGiudiceDescrizione(){
        return giudiceDescrizione;
    }

    /**
     * Imposta il giudice responsabile della descrizione del problema.
     * @param giudiceDescrizione giudice da impostare
     */
    public void setGiudiceDescrizione(Giudice giudiceDescrizione){
        this.giudiceDescrizione = giudiceDescrizione;
    }

    /**
     * Restituisce la lista dei documenti associati all'evento.
     * @return lista dei documenti
     */
    public List<Documento> getDocumenti() {
        return documenti;
    }

    /**
     * Imposta la lista dei documenti associati all'evento.
     * @param documenti lista dei documenti
     */
    public void setDocumenti(List<Documento> documenti) {
        this.documenti = documenti;
    }

    /**
     * Restituisce l'identificativo dell'evento.
     * @return identificativo dell'evento
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'identificativo dell'evento.
     * @param id identificativo da impostare
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Valida la coerenza delle date dell'evento secondo vincoli logici e temporali.
     * <ul>
     *   <li>La durata deve essere esattamente di 2 giorni.</li>
     *   <li>L'ordine delle date deve essere rispettato: registrazioni prima dell'inizio, inizio prima della fine.</li>
     *   <li>Nessuna data deve essere nel passato.</li>
     *   <li>La fine registrazione deve precedere l'inizio dell'evento.</li>
     *   <li>(Opzionale) La durata non può superare una settimana.</li>
     * </ul>
     * @throws IllegalArgumentException se le date non rispettano i vincoli
     */
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
        } // 3. Nessuna data nel passato
        if (inizioRegistrazioni.isBefore(oggi) || fineRegistrazioni.isBefore(oggi) ||
                dataInizio.isBefore(oggi) || dataFine.isBefore(oggi)) {
            throw new IllegalArgumentException("Le date non possono essere nel passato.");
        }
        // 4. La fine registrazione deve essere prima dell'inizio evento
        if (!fineRegistrazioni.isBefore(dataInizio)) {
            throw new IllegalArgumentException("La fine registrazione deve essere prima dell'inizio evento.");
        }
    }
}