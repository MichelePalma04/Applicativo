package model;

import java.util.ArrayList;

public class Giudice extends Utente {
    private ArrayList<Evento> eventi;
    private ArrayList<Voto> voti;

    public Giudice(String login, String password, ArrayList<Evento> eventi, ArrayList<Voto> voti) {
        super(login, password);
        this.eventi = eventi;
        this.voti = voti;
    }

    public Giudice(String login, String password, Evento e, Voto v) {
        super(login, password);
        eventi = new ArrayList<>();
        eventi.add(e);
        voti = new ArrayList<>();
        voti.add(v);
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

   public ArrayList<Evento> getEventi() {
        return this.eventi;
   }

   public ArrayList<Voto> getVoti() {
        return this.voti;
   }

   @Override
   public String toString(){
        return getLogin();
   }
    public void esaminaDocumento() {}

    public void pubblicaProblema() {}
}
