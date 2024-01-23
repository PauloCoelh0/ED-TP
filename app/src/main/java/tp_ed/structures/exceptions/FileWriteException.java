package tp_ed.structures.exceptions;

public class FileWriteException extends Exception {
    public FileWriteException(String message) {
        super(message);
    }
    public FileWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
