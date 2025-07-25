package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un organizzatore che gestisce uno o più eventi e invita giudici.
 * Estende la classe Utente.
 */
public class Organizzatore extends Utente {
    /** Lista degli eventi organizzati dall'organizzatore. */
    private List<Evento> eventi;

    /** Lista dei giudici invitati dall'organizzatore. */
    private List<Giudice> giudici;

    /**
     * Costruttore che inizializza l'organizzatore con liste di eventi e giudici.
     * @param login Login dell'organizzatore
     * @param password Password dell'organizzatore
     * @param eventi Lista degli eventi organizzati
     * @param giudici Lista dei giudici invitati
     */
    public Organizzatore(String login, String password, List<Evento> eventi, List<Giudice> giudici) {
        super(login, password);
        this.eventi = eventi;
        this.giudici = giudici;
    }

    /**
     * Costruttore che inizializza l'organizzatore con un singolo evento e giudice.
     * @param login Login dell'organizzatore
     * @param password Password dell'organizzatore
     * @param e Evento organizzato
     * @param g Giudice invitato
     */
    public Organizzatore (String login, String password, Evento e, Giudice g) {
        super(login, password);
        eventi = new ArrayList<>();
        eventi.add(e);
        giudici = new ArrayList<>();
        giudici.add(g);
    }

    /**
     * Aggiunge un giudice alla lista dei giudici invitati dall'organizzatore, evitando duplicati.
     * @param newG Giudice da aggiungere
     */
    public void addGiudici(Giudice newG) {
        if (!giudici.contains(newG)) {
            giudici.add(newG);
        }
    }

    /**
     * Aggiunge un evento alla lista degli eventi organizzati, evitando duplicati.
     * @param newE Evento da aggiungere
     */
    public void addEventi (Evento newE){
        if(!this.eventi.contains(newE)){
            this.eventi.add(newE);
        }
    }

    /**
     * Restituisce la lista dei giudici invitati dall'organizzatore.
     * @return lista dei giudici
     */
    public List<Giudice> getGiudici() {
        return this.giudici;
    }

    /**
     * Restituisce la lista degli eventi organizzati dall'organizzatore.
     * @return lista degli eventi
     */
    public List<Evento> getEventi() {
        return this.eventi;
    }
}
