package model;

import java.util.ArrayList;

public class Organizzatore extends Utente {
    private ArrayList<Evento> eventi;
    private ArrayList<Giudice> giudici;

    public Organizzatore(String login, String password, ArrayList<Evento> eventi, ArrayList<Giudice> giudici) {
        super(login, password);
        this.eventi = eventi;
        this.giudici = giudici;
    }

    public Organizzatore (String login, String password, Evento e, Giudice g) {
        super(login, password);
        eventi = new ArrayList<>();
        eventi.add(e);
        giudici = new ArrayList<>();
        giudici.add(g);
    }

    public void addGiudici(Giudice newG) {
        if (!giudici.contains(newG)) {
            giudici.add(newG);
        }
    }

    public ArrayList<Giudice> getGiudici() {
        return this.giudici;
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
