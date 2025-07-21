package dao;

import model.Evento;
import java.util.List;

public interface EventoDAO {
    List<Evento> getEventiPerOrganizzatore(String login);
    Evento getEventoAttivo();
    Evento getEvento(int eventoId);
    List<Evento> getTuttiEventi();
    Evento aggiungiEvento(Evento evento);
    boolean aggiornaEvento(Evento evento);
    boolean eliminaEvento(int eventoId);
}
