package model;

public class Utente {
    private String login;
    private String password;

    public Utente(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString(){
        return login;
    }
}
