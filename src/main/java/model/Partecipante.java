package model;

public class Partecipante extends Utente {
    private Team team;
    /**
     * Instantiates a new Utente.
     *
     * @param login    the login
     * @param password the password
     */
    public Partecipante(String login, String password, Team team) {
        super(login, password);
        this.team = team;
    }
}
