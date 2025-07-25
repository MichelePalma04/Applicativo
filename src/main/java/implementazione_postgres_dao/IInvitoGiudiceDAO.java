package implementazione_postgres_dao;

import dao.InvitoGiudiceDAO;
import database.ConnessioneDatabase;
import model.Evento;
import model.InvitoGiudice;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IInvitoGiudiceDAO implements InvitoGiudiceDAO {
    private Connection connection;
    private  IEventoDAO eventoDAO;
    private IUtenteDAO utenteDAO;

    private static final String EVENTOID_COLUMN = "evento_id";

    public IInvitoGiudiceDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean addInvitoGiudice(InvitoGiudice invito) {
        String sql = "INSERT INTO invito_giudice (evento_id, utente_login, accettato, rifiutato) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invito.getEvento().getId());
            ps.setString(2, invito.getUtente().getLogin());
            ps.setBoolean(3, invito.isAccettato());
            ps.setBoolean(4, invito.isRifiutato());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<InvitoGiudice> getInvitiPendentiPerUtente(String loginUtente) {
        List<InvitoGiudice> lista = new ArrayList<>();
        String sql = "SELECT id, evento_id, utente_login, accettato, rifiutato FROM invito_giudice WHERE utente_login = ? AND accettato = false AND rifiutato = false";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, loginUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Evento evento = eventoDAO.getEvento(rs.getInt(EVENTOID_COLUMN));
                Utente utente = utenteDAO.getUtentebyLogin(rs.getString("utente_login"));
                InvitoGiudice invito = new InvitoGiudice(rs.getInt(EVENTOID_COLUMN), rs.getInt("id"), evento, utente, rs.getBoolean("accettato"), rs.getBoolean("rifiutato"));
                lista.add(invito);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public InvitoGiudice getInvitoById(int idInvito) {
        String sql = "SELECT id, evento_id, utente_login, accettato, rifiutato  FROM invito_giudice WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idInvito);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Evento evento = eventoDAO.getEvento(rs.getInt(EVENTOID_COLUMN));
                Utente utente = utenteDAO.getUtentebyLogin(rs.getString("utente_login"));
                boolean accettato = rs.getBoolean("accettato");
                boolean rifiutato = rs.getBoolean("rifiutato");
                return new InvitoGiudice(rs.getInt(EVENTOID_COLUMN), rs.getInt("id"), evento, utente, accettato, rifiutato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean esisteInvitoPendentePerUtenteEvento(String login, int eventoId) {
        String sql = "SELECT 1 FROM invito_giudice WHERE utente_login = ? AND evento_id = ? AND accettato = false AND rifiutato = false LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setInt(2, eventoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true se c'è almeno una riga
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // oppure puoi rilanciare l'eccezione a seconda della tua gestione errori
        }
    }

    @Override
    public boolean updateInvitoGiudice(InvitoGiudice invito) {
        String sql = "UPDATE invito_giudice SET accettato = ?, rifiutato = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, invito.isAccettato());
            ps.setBoolean(2, invito.isRifiutato());
            ps.setInt(3, invito.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteInvitoGiudice(int idInvito) {
        String sql = "DELETE FROM invito_giudice WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idInvito);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setUtenteDAO(IUtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    @Override
    public void setEventoDAO(IEventoDAO eventoDAO){
        this.eventoDAO = eventoDAO;
    }


}
