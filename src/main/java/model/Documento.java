
package model;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un documento caricato dai partecipanti di un team durante un evento.
 * Un documento è associato a una data di invio, a un file, al team che lo ha caricato
 * e al login del partecipante che lo ha inviato. Può contenere una lista di commenti dei giudici.
 */
public class Documento {
    /** Data di invio del documento. */
    private LocalDate data;

    /** File allegato al documento. */
    private File file;

    /** Lista dei commenti dei giudici associati al documento. */
    private List<CommentoGiudice> commentiGiudici = new ArrayList<>();

    /** Team che ha inviato il documento. */
    private Team team;

    /** Identificativo univoco del documento. */
    private int id;

    /** Login del partecipante che ha caricato il documento. */
    private String loginPartecipante;

    /**
     * Costruttore della classe Documento con login del partecipante.
     * @param data Data di invio del documento
     * @param file File allegato
     * @param team Team che ha inviato il documento
     * @param loginPartecipante Login del partecipante che ha caricato il documento
     */
    public Documento (LocalDate data, File file, Team team, String loginPartecipante) {
        this.data = data;
        this.team = team;
        this.file = file;
        this.loginPartecipante = loginPartecipante;
    }

    /**
     * Costruttore principale della classe, senza login del partecipante.
     * @param data Data di invio del documento
     * @param file File allegato
     * @param team Team che ha inviato il documento
     */
    public Documento (LocalDate data, File file, Team team) {
        this.data = data;
        this.team = team;
        this.file = file;
    }

    /**
     * Restituisce la data di invio del documento.
     * @return Data di invio
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Restituisce il file associato al documento.
     * @return File allegato
     */
    public File getFile() {
        return file;
    }

    /**
     * Restituisce il team che ha caricato il documento.
     * @return Team autore del documento
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Restituisce l'identificativo del documento.
     * @return ID del documento
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'identificativo del documento.
     * @param id Identificativo da assegnare
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce la lista dei commenti dei giudici associati al documento.
     * @return Lista dei commenti dei giudici
     */
    public List<CommentoGiudice> getCommentiGiudici() {
        return commentiGiudici;
    }

    /**
     * Imposta la lista dei commenti dei giudici.
     * @param commentiGiudici Lista dei commenti da assegnare
     */
    public void setCommentiGiudici(List<CommentoGiudice> commentiGiudici) {
        this.commentiGiudici = commentiGiudici;
    }

    /**
     * Aggiunge un commento di un giudice al documento.
     * @param commento Commento da aggiungere
     */
    public void aggiungiCommentoGiudice(CommentoGiudice commento) {
        this.commentiGiudici.add(commento);
    }

    /**
     * Restituisce il login del partecipante che ha caricato il documento.
     * @return Login del partecipante
     */
    public String getLoginPartecipante() {
        return loginPartecipante;
    }

    /**
     * Imposta il login del partecipante.
     * @param loginPartecipante Login da assegnare
     */
    public void setLoginPartecipante(String loginPartecipante) {
        this.loginPartecipante = loginPartecipante;
    }
}

