package lenart.piotr.blokus.engine.exceptions;

public class WrongActionException extends Exception{
    private final String textToDisplay;

    public WrongActionException(String textToDisplay){
        super();
        this.textToDisplay = textToDisplay;
    }

    public String getTextToDisplay(){
        return textToDisplay;
    }
}
