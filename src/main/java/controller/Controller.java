package controller;
import dao.*;
import gui.Invito;
import implementazionePostgresDAO.*;
import model .*;
import javax.swing.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private UtenteDAO utenteDAO;
    private OrganizzatoreDAO organizzatoreDAO;
    private PartecipanteDAO partecipanteDAO;
    private EventoDAO eventoDAO;
    private TeamDAO teamDAO;
    private GiudiceDAO giudiceDAO;
    private InvitoGiudiceDAO invitoGiudiceDAO;
    private VotoDAO votoDAO;
    private DocumentoDAO documentoDAO;
    private Utente utenteCorrente = null;
    private ArrayList<Utente> utentiRegistrati;
    private ArrayList<Evento> eventiDisponibili;
    private Partecipante partecipantCorrente = null;
    private Organizzatore organizzatoreCorrente = null;
    private ArrayList<InvitoGiudice> invitiPendenti;
    private ArrayList<InvitoGiudice> invitiGiudice;

    public Controller(UtenteDAO utenteDAO, OrganizzatoreDAO organizzatoreDAO, PartecipanteDAO partecipanteDAO, GiudiceDAO giudiceDAO, EventoDAO eventoDAO, TeamDAO teamDAO, InvitoGiudiceDAO invitoGiudiceDAO, DocumentoDAO documentoDAO, VotoDAO votoDAO) {
        this.utentiRegistrati = new ArrayList<>();
        this.eventiDisponibili = new ArrayList<>();
        this.invitiPendenti = new ArrayList<>();
        this.invitiGiudice = new ArrayList<>();
        this.utenteDAO = utenteDAO;
        this.organizzatoreDAO = organizzatoreDAO;
        this.partecipanteDAO = partecipanteDAO;
        this.eventoDAO = eventoDAO;
        this.teamDAO = teamDAO;
        this.giudiceDAO = giudiceDAO;
        this.invitoGiudiceDAO = invitoGiudiceDAO;
        this.documentoDAO = documentoDAO;
        this.votoDAO = votoDAO;
        initEventi();
    }

    public void initEventi() {
        Organizzatore o =new Organizzatore("miki&sara", "sara&miki", new ArrayList<>(), new ArrayList<>());
        Organizzatore o2 = new Organizzatore("sara&miki", "miki&sara", new ArrayList<>(), new ArrayList<>());

        aggiungiUtenteSeNuovo(o);
        aggiungiUtenteSeNuovo(o2);
    }

    public void aggiungiUtenteSeNuovo(Utente utente){
        for(Utente u: utentiRegistrati){
            if(u.getLogin().equals(utente.getLogin())){
                return;
            }
        }
        utentiRegistrati.add(utente);
    }

    public void aggiungiEventoSeNuovo(Evento evento){
        for(Evento e: eventiDisponibili){
            //supponiamo che non ci siano eventi con lo stesso Titolo svolti nello stesso periodo
            if(e.getTitolo().equals(evento.getTitolo()) && e.getDataInizio().equals(evento.getDataInizio()) && e.getDataFine().equals(evento.getDataFine())){
                return;
            }
        }
        eventiDisponibili.add(evento);
    }

    public ArrayList<Evento> getEventiDisponibili() {
        return eventiDisponibili;
    }

   /* NON USATA
   public static ArrayList<Team> getTeamDisponibili(Evento evento){
        return evento.getTeams();
    }
    */
/*
    public boolean registraUtente (String email, String password) {
        for (Utente u : utentiRegistrati) {
            if (u.getLogin().equals(email)) {
                return false; //utente già presente
            }
        }
        utentiRegistrati.add(new Utente(email, password));
        return true;
    }

 */

    /*
    public boolean creaEvento (String titolo, String sede, LocalDate dataInizio, LocalDate dataFine, int nMaxIscritti, int dimMaxTeam, LocalDate inizioRegistrazioni, LocalDate fineRegistrazioni) {
        Evento nuovoEvento = new Evento (titolo, sede, dataInizio, dataFine, nMaxIscritti, dimMaxTeam, inizioRegistrazioni, fineRegistrazioni, organizzatoreCorrente, new ArrayList<>(),  new ArrayList<>());
        if(!eventiDisponibili.contains(nuovoEvento)) {
            eventiDisponibili.add(nuovoEvento);
            organizzatoreCorrente.getEventi().add(nuovoEvento);
            nuovoEvento.setDocumenti(new ArrayList<>());
            return true;
        }
        return false;
    }

     */

/*
    public Utente loginUtente (String email, String password) {
        for (Utente u : utentiRegistrati) {
            if (u.getLogin().equals(email) && u.getPassword().equals(password)) {
                if(u instanceof Organizzatore){
                    organizzatoreCorrente = (Organizzatore) u;
                    utenteCorrente = u;
                    return u;
                }

                for(Evento e: eventiDisponibili) {
                    for(Partecipante p: e.getPartecipanti()) {
                        if(p.getLogin().equals(u.getLogin())) {
                            utenteCorrente = p;
                            partecipantCorrente = p;
                            return p;
                        }
                    }
                }
                utenteCorrente = u;
                return u;
            }
        }
        return null;
    }
 */

    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    /*
    public ArrayList<Utente> getUtentiRegistrati() {
        return utentiRegistrati;
    }
     */

    public List<Utente> getUtentiRegistrati() {
        return utenteDAO.getAllUtenti();
    }

    public boolean eliminaUtente(String login) {
        return utenteDAO.deleteUtente(login);
    }

    public boolean aggiornaUtente(Utente utente) {
        return utenteDAO.updateUtente(utente);
    }

    public void setUtenteCorrente(Utente u) {
        this.utenteCorrente = u;
    }

    public Partecipante getPartecipantCorrente() {
        return partecipantCorrente;
    }

    public void setPartecipantCorrente(Partecipante p) {
        partecipantCorrente = p;
    }

    public Giudice getGiudiceCorrente(Evento evento) {
        for (Giudice giudice: evento.getGiudici()) {
            if(giudice.getLogin().equals(utenteCorrente.getLogin())) {
                return giudice;
            }
        }
        return null;
    }

    //Funzione che serve a controllare se vengono aggiunti effettivamente i partecipanti a quell evento
    public void stampaPartecipantiEvento(Evento e) {
        System.out.println("Partecipanti evento: " + e.getTitolo());
        for(Partecipante p : e.getPartecipanti()) {
            System.out.println("-"+p.getLogin());
        }
    }

    /*
    public boolean invitaGiudicePendente(Evento evento, Utente utente) {
        if((utente instanceof Partecipante)) {
            return false;


        for(Giudice giudice: evento.getGiudici()) {
            if(giudice.getLogin().equals(utente.getLogin())) {
                return false;
            }
        }
        for(InvitoGiudice invito: invitiPendenti){
            if(invito.getEvento().equals(evento)&&invito.getUtente().getLogin().equals(utente.getLogin()) && !invito.isAccettato() && !invito.isRifiutato()) {
                return false;
            }
        }
        invitiPendenti.add(new InvitoGiudice(evento, utente));
        return true;
    }
    */

    /*
    public boolean accettaInvitoGiudice(Evento evento, Utente utente) {
        InvitoGiudice invitoTrovato = null;
        for (InvitoGiudice invito: invitiPendenti) {
            if(invito.getEvento().equals(evento)&&invito.getUtente().getLogin().equals(utente.getLogin()) && !invito.isAccettato()) {
                invitoTrovato = invito;
                break;
            }
        }
        if(invitoTrovato != null) {
            for(Partecipante p : evento.getPartecipanti()){
                if(p.getLogin().equals(utente.getLogin())) {
                    JOptionPane.showMessageDialog(null, "Non puoi accettare l'invito come giudice: sei già partecipante di questo evento", "Errore", JOptionPane.ERROR_MESSAGE);
                    invitiPendenti.remove(invitoTrovato);
                    return false;
                }
            }
            invitoTrovato.accetta();

            Giudice giudiceEsistente = null;

            for (Utente u : utentiRegistrati) {
                if (u instanceof Giudice && u.getLogin().equals(utente.getLogin())) {
                    giudiceEsistente = (Giudice) u;
                    break;
                }
            }

            if (giudiceEsistente != null) {
                if (!giudiceEsistente.getEventi().contains(evento)) {
                    giudiceEsistente.getEventi().add(evento);
                }
                evento.getGiudici().add(giudiceEsistente);
                if (utenteCorrente != null && utenteCorrente.getLogin().equals(utente.getLogin())) {
                    utenteCorrente = giudiceEsistente;
                }
                setUtenteCorrente(giudiceEsistente);
            }else{
                Giudice nuovoGiudice = new Giudice(utente.getLogin(), utente.getPassword(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                nuovoGiudice.getEventi().add(evento);
                utentiRegistrati.add(nuovoGiudice);
                evento.getGiudici().add(nuovoGiudice);
                if (utenteCorrente != null && utenteCorrente.getLogin().equals(utente.getLogin())) {
                    utenteCorrente = nuovoGiudice;
                }
                setUtenteCorrente(nuovoGiudice);
            }

            invitiPendenti.remove(invitoTrovato);
            System.out.println("Giudici dell'evento "+ evento.getTitolo() + " dopo l'accettazione.");
            for (Giudice g : evento.getGiudici()) {
                System.out.println("- " + g.getLogin());
            }
            stampaUtentiRegistrati();
            return true;
        }
        return false;
    }

    public boolean rifiutaInvitoGiudice (Evento evento, Utente utente) {
        InvitoGiudice invitoDaRimuovere = null;

        //uso di new ArrayList <> così da evitare problemi nella rimozioni di elementi durante il for
        for (InvitoGiudice invito : new ArrayList <> (invitiPendenti)) {
            if(invito.getEvento().equals(evento) && invito.getUtente().getLogin().equals(utente.getLogin()) && !invito.isAccettato()){
                invito.setRifiutato();
                invitiPendenti.remove(invito);
                return true;
            }
        }
        return false;
    }
    */

    public boolean isUtenteGiudice(Evento evento, Utente utente) {
        for (Giudice g : evento.getGiudici()) {
            if (g.getLogin().equals(utente.getLogin())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList <Utente> getUtentiInvitabili(int eventoId){
        for (Giudice g :giudiceDAO.getGiudiciEvento(eventoId)) {
            System.out.println("Giudice presente: " + g.getLogin());
        }
        ArrayList <Utente> invitabili = new ArrayList <>();
        for (Utente u : utenteDAO.getAllUtenti()) {
            if (organizzatoreDAO.isOrganizzatore(u.getLogin())) {
                continue;
            }
            boolean giaGiudiceInQuestoEvento = false;
            for (Giudice g: giudiceDAO.getGiudiciEvento(eventoId)) {
                if(g.getLogin().equals(u.getLogin())) {
                    giaGiudiceInQuestoEvento = true;
                    break;
                }
            }
            if(giaGiudiceInQuestoEvento){
                continue;
            }

            boolean giaPartecipanteInQuestoEvento = false;
            for (Partecipante p: partecipanteDAO.getPartecipantiEvento(eventoId)) {
                if(p.getLogin().equals(u.getLogin())) {
                    giaPartecipanteInQuestoEvento = true;
                    break;
                }
            }
            if(giaPartecipanteInQuestoEvento){
                continue;
            }

            if(invitoGiudiceDAO.esisteInvitoPendentePerUtenteEvento(u.getLogin(),eventoId)) {
                continue;
            }

            invitabili.add(u);
        }
        return invitabili;
    }

    /*
    public void aggiungiInvitoGiudice (InvitoGiudice invito){
        invitiGiudice.add(invito);
    }
     */


/*
    public void caricaDocumento (Evento e, Documento documento) {
        e.getDocumenti().add(documento);
    }
 */

    public ArrayList <Documento> getDocumentoTeam(Evento evento, Team team) {
        ArrayList <Documento> documento = new ArrayList <>();
        for(Documento doc: evento.getDocumenti()){
            if(doc.getTeam().equals(team)){
                documento.add(doc);
            }
        }
        return documento;
    }




    public Utente loginUtente (String login, String password) {
        Utente u = utenteDAO.getUtentebyLoginAndPassword(login, password);

        if(u==null){
            return null;
        }

        if(organizzatoreDAO.isOrganizzatore(u.getLogin())){
            organizzatoreCorrente = organizzatoreDAO.getOrganizzatore(u.getLogin());
            utenteCorrente = organizzatoreCorrente;
            return organizzatoreCorrente;
        }

        List<Evento> eventi = eventoDAO.getTuttiEventi();
        for(Evento evento: eventi) {
            Partecipante p = partecipanteDAO.getPartecipante(u.getLogin(), evento.getId());
            if (p != null) {
                partecipantCorrente = p;
                utenteCorrente = p;
                return p;
            }
        }
        utenteCorrente = u;
        return u;
    }

    public boolean registraUtente (String email, String password) {
       Utente attuale = utenteDAO.getUtentebyLogin(email);
       if(attuale != null){
           return false;
       }
       Utente nuovoUtente = new Utente(email, password);
       boolean inserito = utenteDAO.addUtente(nuovoUtente);
       if(inserito){
           utentiRegistrati.add(nuovoUtente);
           return true;
       }else {
           return false;
       }
    }

    public boolean iscriviPartecipante (String login, int eventoId) {
        return partecipanteDAO.addPartecipante(login, eventoId);
    }

    public Partecipante getPartecipanteDaDB(String login, int eventoId) {
        return partecipanteDAO.getPartecipante(login, eventoId);
    }

    public Evento creaEvento (String titolo, String sede, LocalDate dataInizio, LocalDate dataFine, int nMaxIscritti, int dimMaxTeam, LocalDate inizioRegistrazioni, LocalDate fineRegistrazioni) {
        Evento nuovoEvento = new Evento (titolo, sede, dataInizio, dataFine, nMaxIscritti, dimMaxTeam, inizioRegistrazioni, fineRegistrazioni, organizzatoreCorrente, new ArrayList<>(),  new ArrayList<>());
        // Salva nel DB e ottieni l'evento con id assegnato
        Evento eventoConId = eventoDAO.aggiungiEvento(nuovoEvento);
        if (eventoConId != null) {
            // Puoi opzionalmente aggiornare la lista locale se vuoi una cache
            // eventiDisponibili.add(eventoConId);
            organizzatoreCorrente.getEventi().add(eventoConId);
            organizzatoreDAO.aggiungiOrganizzatore(organizzatoreCorrente, eventoConId.getId());
            eventoConId.setDocumenti(new ArrayList<>());
            return eventoConId;
        }
        return null;
    }

    public List<Evento> getEventiOrganizzatore(String loginOrganizzatore) {
        return eventoDAO.getEventiPerOrganizzatore(loginOrganizzatore);
    }

    public List<Evento> getTuttiEventi() {
        return eventoDAO.getTuttiEventi();
    }

    /*
    public List <Partecipante> getPartecipantiEvento(int eventoId) {
        return partecipanteDAO.getPartecipantiEvento(eventoId);
    }

     */

    public List<InvitoGiudice> getInvitiPendentiUtente(String login) {
        return invitoGiudiceDAO.getInvitiPendentiPerUtente(login);
    }

    /*
    public boolean aggiungiInvitoGiudice(InvitoGiudice invito) {
        return invitoGiudiceDAO.addInvitoGiudice(invito);
    }

     */

    public boolean accettaInvitoGiudice(int idInvito, String login) {
        // Imposta lo stato
        InvitoGiudice invito = invitoGiudiceDAO.getInvitoById(idInvito);
        int eventoId = invito.getEventoId();
        Partecipante partecipante = partecipanteDAO.getPartecipante(login, eventoId);
        if(partecipante != null){
            JOptionPane.showMessageDialog(null, "Non puoi accettare l'invito come giudice: sei già partecipante di questo evento", "error", JOptionPane.ERROR_MESSAGE);
            invito.setAccettato(false);
            invito.setRifiutato(true);
            invitoGiudiceDAO.updateInvitoGiudice(invito);
            return false;
        }
        invito.setAccettato(true);
        invito.setRifiutato(false);
        // Aggiorna nel database tramite DAO
        boolean invitoOK = invitoGiudiceDAO.updateInvitoGiudice(invito);
        if(invitoOK){
            System.out.println("DEBUG: Sto inserendo giudice per eventoId = " + eventoId);
            return giudiceDAO.aggiungiGiudice(login, eventoId);
        }
        return false;
    }

    public boolean rifiutaInvitoGiudice(int invitoId, String login) {
        InvitoGiudice invito = invitoGiudiceDAO.getInvitoById(invitoId);
        invito.setAccettato(false);
        invito.setRifiutato(true);
        return invitoGiudiceDAO.updateInvitoGiudice(invito);
    }

    public boolean invitaGiudicePendente(int eventoId, String login) {
        if (giudiceDAO.getGiudice(login, eventoId) != null) {
            return false;
        }
        if(invitoGiudiceDAO.esisteInvitoPendentePerUtenteEvento(login, eventoId)){
            return false;
        }
        Evento evento = eventoDAO.getEvento(eventoId);
        Utente utente = utenteDAO.getUtentebyLogin(login);
        InvitoGiudice nuovoInvito = new InvitoGiudice(evento, utente, false, false);
        return invitoGiudiceDAO.addInvitoGiudice(nuovoInvito);
    }

    public Giudice getGiudiceEvento(String login, int eventoId) {
        return giudiceDAO.getGiudice(login, eventoId);
    }

    public List <Giudice> getGiudiciEvento(int eventoId) {
        return giudiceDAO.getGiudiciEvento(eventoId);
    }
    public Utente getUtenteDaDB(String login) {
        return utenteDAO.getUtentebyLogin(login);
    }

    public Evento geteventoById(int id) {
        return eventoDAO.getEvento(id);
    }

    public List<Team> getTeamsEvento (int eventoId) {
        return teamDAO.getTeamEvento(eventoId);
    }

    // Crea un nuovo team e lo aggiunge al DB
    public Team creaTeam(String nomeTeam, String loginPartecipante, int eventoId) {
        Team team = new Team(nomeTeam, new ArrayList<>(), new ArrayList<>());
        teamDAO.aggiungiTeam(team, eventoId);
        // Unisci subito il partecipante al team appena creato
        partecipanteDAO.joinTeam(loginPartecipante, nomeTeam, eventoId);
        return teamDAO.getTeam(nomeTeam, eventoId);
    }

    // Unisci il partecipante a un team
    /*
    public void unisciPartecipanteATeam(String loginPartecipante, String nomeTeam, int eventoId) {
        partecipanteDAO.joinTeam(loginPartecipante, nomeTeam, eventoId);
    }

     */

    // Verifica se un partecipante è già in un team
    public boolean isPartecipanteInTeam(String loginPartecipante, String nomeTeam, int eventoId) {
        return teamDAO.isPartecipanteInTeam(loginPartecipante, nomeTeam, eventoId);
    }

    // Dimensione attuale del team
    public int getDimTeam(String nomeTeam, int eventoId) {
        return teamDAO.getDimTeam(nomeTeam, eventoId);
    }
    // Carica un documento nel DB
    public void caricaDocumento(Documento documento, String nomeTeam, int eventoId) {
        documentoDAO.save(documento, nomeTeam, eventoId);
    }

    public String getProblemaEvento(int eventoId) {
        return eventoDAO.getProblemaEvento(eventoId);
    }

    public void setProblemaEvento(int eventoId, String descrizione) {
        eventoDAO.setProblemaEvento(eventoId, descrizione);
    }

    public Giudice getGiudiceDescrizione(int eventoId) {
        String login = eventoDAO.getLoginGiudiceDescrizione(eventoId); // recupera il login dal DB
        if (login != null) {
            return giudiceDAO.getGiudice(login, eventoId); // recupera l'oggetto Giudice dal DB
        }
        return null;
    }

    public boolean teamHaDocumenti(String nomeTeam, int eventoId) {
        return documentoDAO.teamHaDocumenti(nomeTeam, eventoId);
    }

    public List<Documento> getDocumentiEvento(int eventoId) {
        return documentoDAO.getDocumentiEvento(eventoId);
    }

    public List<Documento> getDocumentiTeamEvento(String nomeTeam, int eventoId) {
        return documentoDAO.getDocumentiTeamEvento(eventoId, nomeTeam);
    }

    public boolean giudiceHaVotatoTeam(String loginGiudice, String nomeTeam, int eventoId) {
        return votoDAO.giudiceHaVotatoTeam(loginGiudice, nomeTeam, eventoId);
    }

    public int getVotoDiGiudiceTeam(String loginGiudice, String nomeTeam, int eventoId) {
        return votoDAO.getVotoDiGiudiceTeam(loginGiudice, nomeTeam, eventoId);
    }

    public void votaTeam(String loginGiudice, String nomeTeam, int eventoId, int voto) {
        votoDAO.votaTeam(loginGiudice, nomeTeam, eventoId, voto);
    }

    public boolean assegnaGiudiceDescrizione (int eventoId, String loginGiudice) {
        if (giudiceDAO.getGiudice(loginGiudice, eventoId) != null) {
            return eventoDAO.setGiudiceDescrizione(eventoId, loginGiudice);
        }
        return false;
    }

    public void unisciPartecipanteATeam (String loginPartecipante, String nomeTeam, int eventoId) {
        teamDAO.unisciPartecipanteATeam(loginPartecipante, nomeTeam, eventoId);
    }
}

