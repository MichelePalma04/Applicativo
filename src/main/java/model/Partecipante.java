package model;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un partecipante a uno o più eventi.
 * Un partecipante può appartenere ad un team e iscriversi a vari eventi.
 * Estende la classe Utente.
 */
public class Partecipante extends Utente {
    /** Team di appartenenza del partecipante. */
    private Team team;

    /** Nome del team di cui fa parte. */
    private String teamNome;

    /** Lista degli eventi a cui il partecipante è iscritto. */
    private List<Evento> eventi;

    /**
     * Costruttore principale che inizializza il partecipante con liste di team ed eventi.
     * @param login Login del partecipante
     * @param password Password del partecipante
     * @param team Team di appartenenza
     * @param eventi Lista degli eventi a cui partecipa
     */
    public Partecipante(String login, String password, Team team, List<Evento> eventi) {
        super(login, password);
        this.team = team;
        this.eventi = eventi;
    }

    /**
     * Costruttore con singolo evento e singolo team.
     * @param login Login del partecipante
     * @param password Password del partecipante
     * @param team Team di appartenenza
     * @param e Evento a cui partecipa
     */
    public Partecipante(String login, String password, Team team, Evento e) {
        super(login, password);
        this.team = team;
        eventi = new ArrayList<>();
        eventi.add(e);
    }

    /**
     * Costruttore alternativo per DAO, solo login, password e nome team.
     * @param login Login del partecipante
     * @param password Password del partecipante
     * @param teamNome Nome del team di appartenenza
     */
    public Partecipante(String login, String password, String teamNome) {
        super(login, password);
        this.teamNome = teamNome;
        this.team = null;
        this.eventi = new ArrayList<>();
    }

    /**
     * Aggiunge un evento alla lista degli eventi a cui partecipa, evitando duplicati.
     * @param newE Evento da aggiungere
     */
    public void addEventi (Evento newE){
        if(!this.eventi.contains(newE)){
            this.eventi.add(newE);
        }
    }

    /**
     * Restituisce il nome del team di appartenenza.
     * @return nome del team
     */
    public String getTeamNome() {
        return teamNome;
    }

    /**
     * Restituisce il team di appartenenza del partecipante.
     * @return team di appartenenza
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Restituisce la lista degli eventi a cui partecipa il partecipante.
     * @return lista di eventi
     */
    public List<Evento> getEventi(){
        return this.eventi;
    }
}
