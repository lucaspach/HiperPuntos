package extra;

public class ValidationException extends Exception {

    public ValidationException(String msj){
        super(msj);
    }
    public ValidationException(String msj, Throwable cause){
        super(msj, cause);
    }

}
