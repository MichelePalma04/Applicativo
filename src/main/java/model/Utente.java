package model;

/**
 * The type Utente.
 */
public class Utente {
    private final String login;
    private String password;

    /**
     * Instantiates a new Utente.
     *
     * @param s
     * @param login    the login
     * @param password the password
     */
    public Utente(String s, String login, String password) {

        this.login = login;
        this.password = password;
    }



    /**
     * Gets login.
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }
    public String getPassword() {return password;}
}
