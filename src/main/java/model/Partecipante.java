package model;
import java.util.ArrayList;

public class Partecipante extends Utente {
    private Team team;
    private String teamNome;
    private ArrayList<Evento> eventi;

    public Partecipante(String login, String password, Team team, ArrayList<Evento> eventi) {
        super(login, password);
        this.team = team;
        this.eventi = eventi;
    }

    public Partecipante(String login, String password, Team team, Evento e) {
        super(login, password);
        this.team = team;
        eventi = new ArrayList<>();
        eventi.add(e);
    }

    // costruttore light per DAO!
    public Partecipante(String login, String password, String teamNome) {
        super(login, password);
        this.teamNome = teamNome;
        this.team = null;
        this.eventi = new ArrayList<>();
    }

    public void addEventi (Evento newE){
        if(!this.eventi.contains(newE)){
            this.eventi.add(newE);
        }
    }

    public String getTeamNome() {
        return teamNome;
    }

    public Team getTeam() {
        return team;
    }

    public ArrayList<Evento> getEventi(){
        return this.eventi;
    }
}
