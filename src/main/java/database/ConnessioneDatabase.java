package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton per la gestione della connessione al database PostgreSQL.
 * Fornisce un'unica istanza condivisa per tutta l'applicazione.
 */
public class ConnessioneDatabase {

    /** Istanza singleton della classe ConnessioneDatabase. */
    private static ConnessioneDatabase instance;

    /** Oggetto Connection per la connessione al database. */
    public Connection connection = null;

    /** Nome utente per la connessione al database. */
    private String nome = "postgres";

    /** Password per la connessione al database. */
    private String password = "altalena0789";

    /** URL della connessione al database. */
    private String url = "jdbc:postgresql://localhost:5432/Hackaton";

    /** Driver JDBC PostgreSQL. */
    private String driver = "org.postgresql.Driver";

    /**
     * Costruttore privato. Inizializza la connessione al database utilizzando i parametri specificati.
     * @throws SQLException se si verifica un errore durante la connessione
     */
    private ConnessioneDatabase() throws SQLException {
        try{
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Restituisce l'istanza singleton di ConnessioneDatabase.
     * Se l'istanza non esiste o la connessione è chiusa, ne crea una nuova.
     * @return istanza di ConnessioneDatabase
     * @throws SQLException se si verifica un errore durante la connessione
     */
    public static ConnessioneDatabase getInstance() throws SQLException {
        if(instance == null){
            instance = new ConnessioneDatabase();
        }else if (instance.connection.isClosed()){
            instance = new ConnessioneDatabase();
        }
        return instance;
    }
}
