package model;

import java.util.ArrayList;

public class Giudice extends Utente {
    private ArrayList<Evento> eventi;
    private ArrayList<Voto> voti;
    private ArrayList <Organizzatore> organizzatori;

    public Giudice(String login, String password, ArrayList<Evento> eventi, ArrayList<Voto> voti, ArrayList<Organizzatore> organizzatori) {
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

    public ArrayList<Evento> getEventi() {
        return this.eventi;
    }
    public ArrayList<Voto> getVoti() {
        return this.voti;
    }

    public ArrayList<Organizzatore> getOrganizzatori() {
        return this.organizzatori;
    }

    @Override
    public String toString(){
        return getLogin();
    }

    public void esaminaDocumento() {}

    public void pubblicaProblema() {}
}
