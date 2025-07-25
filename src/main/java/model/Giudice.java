package model;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un Giudice che valuta i team negli eventi.
 * Un giudice può esprimere voti e collaborare con più organizzatori.
 * Estende utente.
 */
public class Giudice extends Utente {

    /**
     * Lista degli eventi a cui il giudice partecipa.
     */
    private List<Evento> eventi;

    /**
     * Lista dei voti assegnati dal giudice.
     */
    private List<Voto> voti;

    /**
     * Lista degli organizzatori con cui il giudice collabora.
     */
    private List <Organizzatore> organizzatori;

    /**
     * Costruttore principale, che inizializza il giudice con liste di eventi, voti e organizzatori.
     * @param login login del giudice
     * @param password password del giudice
     * @param eventi lista eventi
     * @param voti lista voti
     * @param organizzatori lista organizzatori
     */
    public Giudice(String login, String password, List<Evento> eventi, List<Voto> voti, List<Organizzatore> organizzatori) {
        super(login, password);
        this.eventi = eventi;
        this.voti = voti;
        this.organizzatori = organizzatori;
    }

    /**
     * Costruttore che inizializza il giudice con un singolo evento, voto e organizzatore.
     * @param login Login del giudice
     * @param password Password del giudice
     * @param e Evento a cui partecipa
     * @param v Voto assegnato
     * @param o Organizzatore associato
     */
    public Giudice(String login, String password, Evento e, Voto v, Organizzatore o) {
        super(login, password);
        eventi = new ArrayList<>();
        eventi.add(e);
        voti = new ArrayList<>();
        voti.add(v);
        organizzatori = new ArrayList<>();
        organizzatori.add(o);
    }

    /**
     * Aggiunge un evento alla lista degli eventi del giudice, evitando duplicati.
     * @param newE Evento da aggiungere
     */
    public void addEventi(Evento newE){
        if(!this.eventi.contains(newE)){
            this.eventi.add(newE);
        }
    }

    /**
     * Aggiunge un voto alla lista dei voti del giudice, evitando duplicati.
     * @param newV Voto da aggiungere
     */
    public void addVoti(Voto newV){
        if(!this.voti.contains(newV)){
            this.voti.add(newV);
        }
    }

    /**
     * Aggiunge un organizzatore alla lista degli organizzatori, evitando duplicati.
     * @param newO Organizzatore da aggiungere
     */
    public void addOrganizzatore(Organizzatore newO){
        if(!this.organizzatori.contains(newO)){
            this.organizzatori.add(newO);
        }
    }

    /**
     * Restituisce la lista degli eventi a cui partecipa il giudice.
     * @return Lista di eventi
     */
    public List<Evento> getEventi() {
        return this.eventi;
    }

    /**
     * Restituisce la lista dei voti espressi dal giudice.
     * @return Lista di voti
     */
    public List<Voto> getVoti() {
        return this.voti;
    }

    /**
     * Restituisce la lista degli organizzatori con cui collabora il giudice.
     * @return Lista di organizzatori
     */
    public List<Organizzatore> getOrganizzatori() {
        return this.organizzatori;
    }

}
