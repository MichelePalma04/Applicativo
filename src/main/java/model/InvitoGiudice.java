package model;

public class InvitoGiudice {
    private Evento evento;
    private Utente utente;
    private boolean accettato;
    private boolean rifiutato;

    public InvitoGiudice(Evento evento, Utente utente) {
        this.evento = evento;
        this.utente = utente;
        this.accettato = false;
        this.rifiutato = false;
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

    public void setRifiutato() {
        this.rifiutato = true;
    }
}
