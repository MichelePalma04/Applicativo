package model;

public class Giudice extends Utente {
    /**
     * Instantiates a new Utente.
     *
     * @param login    the login
     * @param password the password
     */
    public Giudice(String login, String password) {
        super(login, password);
    }
    public void esaminaDocumento() {}

    public void pubblicaProblema() {}
}
