package model;
import java.util.ArrayList;

public class Partecipante extends Utente {
    private Team team;
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

    public void addEventi (Evento newE){
        if(!this.eventi.contains(newE)){
            this.eventi.add(newE);
        }
    }

    @Override
    public String toString(){

        return "Partecipanti: "+getLogin();
    }
    public ArrayList<Evento> getEventi(){
        return this.eventi;
    }
}
