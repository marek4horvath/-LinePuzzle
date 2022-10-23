package sk.tuke.kpi.kp.linepuzzle.service;

public class ScoreException extends RuntimeException {
    public ScoreException() {
    }

    public ScoreException(String message) {
        super(message);
    }

    public ScoreException(String message, Throwable cause) {
        super(message, cause);
    }

}
