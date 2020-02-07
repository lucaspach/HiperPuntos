package extra;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static boolean customerEmail(String email){
        Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean customerPhoneNumber(String phoneNumber){
        if(phoneNumber.length()==12) { // es un número fijo
            Pattern p = Pattern.compile("^[0][1-9]{2,4}-[0-9]{6,8}$");
            Matcher m = p.matcher(phoneNumber);
            return m.matches();
        } else if(phoneNumber.length()==14) { // es un número de celular
            Pattern p = Pattern.compile("^[0][1-9]{2,4}-[15]{2}[0-9]{6,8}$");
            Matcher m = p.matcher(phoneNumber);
            return m.matches();
        } else
            return false;
    }

    public static boolean convertToPositiveDouble(String s){
        boolean result = false;
        try {
           double d = Double.parseDouble(s);
           if (d > 0) result = true;
        } catch (NumberFormatException nfe){
        }

        return result;
    }

    public static boolean convertToPositiveInteger(String s){
        boolean result = false;
        try {
            int i = Integer.parseInt(s);
            if(i > 0) result = true;
        } catch (NumberFormatException nfe){
        }
        return result;
    }
}
