package model;
import java.util.ArrayList;

public class Partecipante extends Utente {
    private Team team;
    private ArrayList<Evento> eventi;
    //private ArrayList <Evento> eventiComeSingolo = new ArrayList<>();
    //private ArrayList <Evento> eventiComeTeam = new ArrayList<>();

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

    /*public void aggiungiEventoComeSingolo (Evento e){
        eventiComeSingolo.add(e);
    }

    public boolean haSceltoSingolo (Evento e){
        return eventiComeSingolo.contains(e);
    }

    public void setEventiComeSingolo (ArrayList <Evento> eventoComeSingolo){
        this.eventiComeSingolo = eventoComeSingolo;
    }
    public void aggiungiEventoTeam (Evento e){
        eventiComeTeam.add(e);
    }

    public boolean haSceltoTeam (Evento e){
        return eventiComeTeam.contains(e);
    }

    public void setEventiComeTeam (ArrayList <Evento> eventoComeTeam){
        this.eventiComeTeam = eventoComeTeam;
    }*/


    /*@Override
    public String toString(){
        return "Partecipanti: "+getLogin();
    }*/

    public ArrayList<Evento> getEventi(){
        return this.eventi;
    }
}
