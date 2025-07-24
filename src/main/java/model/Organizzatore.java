package model;

import java.util.ArrayList;
import java.util.List;

public class Organizzatore extends Utente {
    private List<Evento> eventi;
    private List<Giudice> giudici;

    public Organizzatore(String login, String password, List<Evento> eventi, List<Giudice> giudici) {
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

    public void addEventi (Evento newE){
        if(!this.eventi.contains(newE)){
            this.eventi.add(newE);
        }
    }

    public List<Giudice> getGiudici() {
        return this.giudici;
    }

    public List<Evento> getEventi() {
        return this.eventi;
    }


}
