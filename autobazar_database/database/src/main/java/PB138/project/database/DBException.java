package PB138.project.database;

/**
 * Created by Michal on 25.4.2015.
 */
public class DBException extends RuntimeException {
    public DBException(String msg) {
        super(msg);
    }

    public DBException(Throwable cause) {
        super(cause);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
