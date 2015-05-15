package PB138.project.database;

/**
 * Created by Michal on 15.5.2015.
 */
public class PropertyException extends RuntimeException{
    public PropertyException(String msg) {
        super(msg);
    }

    public PropertyException(Throwable cause) {
        super(cause);
    }

    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
