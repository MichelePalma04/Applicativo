package model;

public class InvitoGiudice {
    private Evento evento;
    private Utente utente;
    private boolean accettato;
    private boolean rifiutato;
    private int id;
    private int eventoId;

    //Costruttore per nuovo invito
    public InvitoGiudice(Evento evento, Utente utente, boolean accettato, boolean rifiutato) {
        this.evento = evento;
        this.utente = utente;
        this.accettato = accettato;
        this.rifiutato = rifiutato;
    }

    public InvitoGiudice(int eventoId, int id, Evento evento, Utente utente, boolean accettato, boolean rifiutato) {
        this.evento = evento;
        this.id = id;
        this.eventoId = eventoId;
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

    public int getEventoId() {
        return eventoId;
    }

    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }
}
