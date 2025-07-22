package model;

public class InvitoGiudice {
    private Evento evento;
    private Utente utente;
    private boolean accettato;
    private boolean rifiutato;
    private int id;

    //Costruttore per nuovo invito
    public InvitoGiudice(Evento evento, Utente utente, boolean accettato, boolean rifiutato) {
        this.evento = evento;
        this.utente = utente;
        this.accettato = false;
        this.rifiutato = false;
    }

    public InvitoGiudice(int id, Evento evento, Utente utente, boolean accettato, boolean rifiutato) {
        this.evento = evento;
        this.id = id;
        this.utente = utente;
        this.accettato = accettato;
        this.rifiutato = rifiutato;
    }


    public Evento getEvento() {
        return evento;
    }

    public Utente getUtente() {
        return utente;
    }

    public boolean isAccettato() {
        return accettato;
    }

    public boolean isRifiutato() {
        return rifiutato;
    }

    public void accetta(){
        this.accettato = true;
    }

    public void setRifiutato(boolean rifiutato) {
        this.rifiutato = rifiutato;
    }

    public void setAccettato(boolean accettato) {
        this.accettato = accettato;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
