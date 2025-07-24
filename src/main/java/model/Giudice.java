package model;

import java.util.ArrayList;
import java.util.List;

public class Giudice extends Utente {
    private List<Evento> eventi;
    private List<Voto> voti;
    private List <Organizzatore> organizzatori;

    public Giudice(String login, String password, List<Evento> eventi, List<Voto> voti, List<Organizzatore> organizzatori) {
        super(login, password);
        this.eventi = eventi;
        this.voti = voti;
        this.organizzatori = organizzatori;
    }

    public Giudice(String login, String password, Evento e, Voto v, Organizzatore o) {
        super(login, password);
        eventi = new ArrayList<>();
        eventi.add(e);
        voti = new ArrayList<>();
        voti.add(v);
        organizzatori = new ArrayList<>();
        organizzatori.add(o);
    }

    public void addEventi(Evento newE){
        if(!this.eventi.contains(newE)){
            this.eventi.add(newE);
        }
    }

    public void addVoti(Voto newV){
        if(!this.voti.contains(newV)){
            this.voti.add(newV);
        }
    }

    public void addOrganizzatore(Organizzatore newO){
        if(!this.organizzatori.contains(newO)){
            this.organizzatori.add(newO);
        }
    }

    public List<Evento> getEventi() {
        return this.eventi;
    }
    public List<Voto> getVoti() {
        return this.voti;
    }

    public List<Organizzatore> getOrganizzatori() {
        return this.organizzatori;
    }

}
