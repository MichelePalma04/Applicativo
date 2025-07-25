package model;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un team di partecipanti ad un evento.
 * Un team può avere più partecipanti, ricevere voti dai giudici e caricare documenti.
 */
public class Team {

    /** Nome del team. */
    private String nomeTeam;

    /** Lista dei documenti caricati dal team. */
    private List<Documento> documenti;

    /** Lista dei partecipanti che fanno parte del team. */
    private List<Partecipante> partecipanti;

    /** Lista dei voti ricevuti dal team. */
    private List<Voto> voti;

    /**
     * Costruttore principale che inizializza il team con liste di partecipanti, documenti e voti.
     * @param nomeTeam Nome del team
     * @param partecipanti Lista dei partecipanti
     * @param voti Lista dei voti ricevuti
     */
    public Team (String nomeTeam, List<Partecipante> partecipanti, List<Voto> voti) {
        this.nomeTeam = nomeTeam;
        this.partecipanti = partecipanti;
        this.documenti = new ArrayList<>();
        this.voti = voti;
    }

    /**
     * Costruttore alternativo per un team con un solo partecipante e un solo voto.
     * @param nomeTeam Nome del team
     * @param p Partecipante da aggiungere
     * @param v Voto da aggiungere
     */
    public Team (String nomeTeam, Partecipante p, Voto v) {
        this.nomeTeam = nomeTeam;
        partecipanti = new ArrayList<>();
        partecipanti.add(p);
        this.documenti = new ArrayList<>();
        voti = new ArrayList<>();
        voti.add(v);
    }

    /**
     * Aggiunge un voto alla lista dei voti del team, evitando duplicati.
     * @param newV Voto da aggiungere
     */
    public void addVoto (Voto newV) {
        if (!voti.contains(newV)) {
            voti.add(newV);
        }
    }

    /**
     * Aggiunge un partecipante alla lista del team, evitando duplicati.
     * @param newP Partecipante da aggiungere
     */
    public void addPartecipanti(Partecipante newP) {
        if (!partecipanti.contains(newP)) {
            partecipanti.add(newP);
        }
    }

    /**
     * Aggiunge un documento alla lista dei documenti del team, evitando duplicati.
     * @param newDoc Documento da aggiungere
     */
    public void addDocumento(Documento newDoc) {
        if (!this.documenti.contains(newDoc)) {
            this.documenti.add(newDoc);
        }
    }

    /**
     * Restituisce la lista dei partecipanti che fanno parte del team.
     * @return lista dei partecipanti
     */
    public List<Partecipante> getPartecipanti() {
        return partecipanti;
    }

    /**
     * Restituisce il nome del team.
     * @return nome del team
     */
    public String getNomeTeam() {
        return nomeTeam;
    }

    /**
     * Restituisce la lista dei documenti caricati dal team.
     * @return lista dei documenti
     */
    public List<Documento> getDocumenti() {
        return documenti;
    }

    /**
     * Restituisce una rappresentazione testuale del team, ovvero il suo nome.
     * @return nome del team
     */
    @Override
    public String toString() {
        return nomeTeam;
    }

    /**
     * Calcola la media dei voti ricevuti dal team.
     * @return media dei voti, 0.0 se nom è presente nessun voto.
     */
    public double mediaVoti() {
        if(voti == null || voti.isEmpty()) {
            return 0.0;
        }
        int somma = 0;
        for (Voto v: voti) {
            somma += v.getVotazione();
        }
        return (double) somma / voti.size();
    }
}
