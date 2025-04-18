package model;

import java.util.ArrayList;

public class Organizzatore extends Utente {
    private ArrayList<Evento> eventi;

    public Organizzatore(String login, String password, ArrayList<Evento> eventi) {
        super(login, password);
        this.eventi = eventi;
    }

    public Organizzatore (String login, String password, Evento e){
        super(login, password);
        eventi = new ArrayList<>();
        eventi.add(e);
    }

    public void addEventi (Evento newE){
        if(!this.eventi.contains(newE)){
            this.eventi.add(newE);
        }
    }


    public ArrayList<Evento> getEventi() {
        return this.eventi;
    }

    @Override
    public String toString() {
        return getLogin()+" è l'organizzatore dell'evento: ";
    }

    public void invitoGiudici(){}
    public void aperturaRegistrazione(){}

}
