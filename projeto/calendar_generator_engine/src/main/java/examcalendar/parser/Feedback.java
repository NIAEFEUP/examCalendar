package examcalendar.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duarte on 29/06/2016.
 */
public class Feedback {
    boolean generated = false;
    String file;
    List<String> warnings = new ArrayList<String>();
    List<String> errors =  new ArrayList<String>();

    public Feedback(String file){
        this.file = file;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public void addWarning(String message, String row, String column){
        String s = "WARNING Line:"+row +" Column:"+column +" -"+message;
        warnings.add(s);
    }

    public void addError(String message, String row, String column){
        String s = "ERROR Line:"+row +" Column:"+column +" -"+message;
        errors.add(s);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("FILE: "+file+"\n");
        stringBuilder.append("RESULT: " + generated +"\n");

        for(String message : getErrors()){
            stringBuilder.append(message);
        }

        for(String message : getWarnings()){
            stringBuilder.append(message);
        }
        return stringBuilder.toString();
    }
}
