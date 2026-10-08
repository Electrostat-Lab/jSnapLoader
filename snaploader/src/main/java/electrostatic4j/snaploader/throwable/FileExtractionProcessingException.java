package electrostatic4j.snaploader.throwable;

public class FileExtractionProcessingException extends RuntimeException {
    public FileExtractionProcessingException(String message) {
        super(message);
    }

    public FileExtractionProcessingException() {
    }

    public FileExtractionProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileExtractionProcessingException(Throwable cause) {
        super(cause);
    }

    public FileExtractionProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
