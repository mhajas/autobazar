package PB138.project.database;

/**
 * Created by Michal on 25.4.2015.
 */
public class CarException extends RuntimeException{
    public CarException(String msg) {
        super(msg);
    }

    public CarException(Throwable cause) {
        super(cause);
    }

    public CarException(String message, Throwable cause) {
        super(message, cause);
    }
}
