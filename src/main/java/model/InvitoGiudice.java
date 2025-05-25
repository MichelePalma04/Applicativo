package model;

public class InvitoGiudice {
    private Evento evento;
    private Utente utente;
    private boolean accettato;

    public InvitoGiudice(Evento evento, Utente utente) {
        this.evento = evento;
        this.utente = utente;
        this.accettato = false;
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
    public void accetta(){
        this.accettato = true;
    }
}
