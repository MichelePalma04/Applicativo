package model;

/**
 * Rappresenta un voto assegnato da un giudice a un team.
 * Ogni oggetto Voto contiene il giudice che ha espresso il voto,
 * il team che ha ricevuto il voto e il valore della votazione.
 */
public class Voto {
    /** Giudice che assegna il voto. */
    private Giudice giudice;

    /** Team valutato dal giudice. */
    private Team team;

    /** Valore numerico del voto assegnato (da 0 a 10). */
    private int votazione;

    /**
     * Costruttore.
     * @param giudice Giudice che assegna il voto
     * @param team Team valutato
     * @param votazione Valore del voto assegnato
     */
    public Voto(Giudice giudice, Team team, int votazione) {
        this.giudice = giudice;
        this.team = team;
        this.votazione = votazione;
    }

    /**
     * Restituisce il valore della votazione.
     * @return valore numerico del voto assegnato
     */
    public int getVotazione() {
        return votazione;
    }

    /**
     * Restituisce il giudice che ha assegnato il voto.
     * @return giudice votante
     */
    public Giudice getGiudice() {
        return giudice;
    }

    /**
     * Restituisce il team valutato.
     * @return team valutato dal giudice
     */
    public Team getTeam() {
        return team;
    }
}
