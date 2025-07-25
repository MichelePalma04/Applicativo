package model;

/**
 * Classe associativa che rappresenta un invito di un giudice a un evento.
 * Tiene traccia dello stato dell'invito (accettato/rifiutato), dell'evento e dell'utente invitato.
 */
public class InvitoGiudice {
    /** Evento a cui si riferisce l'invito. */
    private Evento evento;

    /** Utente giudice invitato all'evento. */
    private Utente utente;

    /** Flag che indica se l'invito è stato accettato. */
    private boolean accettato;

    /** Flag che indica se l'invito è stato rifiutato. */
    private boolean rifiutato;

    /** Identificativo dell'invito. */
    private int id;

    /** Identificativo dell'evento collegato all'invito. */
    private int eventoId;

    /**
     * Costruttore per nuovo invito senza identificativo evento.
     * @param evento Evento a cui si riferisce l'invito
     * @param utente Utente giudice invitato
     * @param accettato Stato di accettazione dell'invito
     * @param rifiutato Stato di rifiuto dell'invito
     */
    public InvitoGiudice(Evento evento, Utente utente, boolean accettato, boolean rifiutato) {
        this.evento = evento;
        this.utente = utente;
        this.accettato = accettato;
        this.rifiutato = rifiutato;
    }

    /**
     * Costruttore completo con identificativo dell'evento.
     * @param eventoId Identificativo dell'evento
     * @param id Identificativo dell'invito
     * @param evento Evento associato
     * @param utente Utente giudice invitato
     * @param accettato Stato di accettazione dell'invito
     * @param rifiutato Stato di rifiuto dell'invito
     */
    public InvitoGiudice(int eventoId, int id, Evento evento, Utente utente, boolean accettato, boolean rifiutato) {
        this.evento = evento;
        this.id = id;
        this.eventoId = eventoId;
        this.utente = utente;
        this.accettato = accettato;
        this.rifiutato = rifiutato;
    }

    /**
     * Restituisce l'evento associato all'invito.
     * @return evento associato
     */
    public Evento getEvento() {
        return evento;
    }

    /**
     * Restituisce l'utente giudice invitato.
     * @return utente invitato
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Indica se l'invito è stato accettato.
     * @return true se accettato, false altrimenti
     */
    public boolean isAccettato() {
        return accettato;
    }

    /**
     * Indica se l'invito è stato rifiutato.
     * @return true se rifiutato, false altrimenti
     */
    public boolean isRifiutato() {
        return rifiutato;
    }

    /**
     * Imposta lo stato di rifiuto dell'invito.
     * @param rifiutato true se rifiutato, false altrimenti
     */
    public void setRifiutato(boolean rifiutato) {
        this.rifiutato = rifiutato;
    }

    /**
     * Imposta lo stato di accettazione dell'invito.
     * @param accettato true se accettato, false altrimenti
     */
    public void setAccettato(boolean accettato) {
        this.accettato = accettato;
    }


    /**
     * Imposta l'identificativo dell'invito.
     * @param id identificativo dell'invito
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'identificativo dell'invito.
     * @return id dell'invito
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce l'identificativo dell'evento collegato all'invito.
     * @return id dell'evento
     */
    public int getEventoId() {
        return eventoId;
    }

    /**
     * Imposta l'identificativo dell'evento collegato all'invito.
     * @param eventoId id dell'evento
     */
    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }
}
