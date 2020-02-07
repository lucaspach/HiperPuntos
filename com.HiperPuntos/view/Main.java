package view;

import extra.TransactionListener;
import extra.Validation;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String [] args){
        /*
        // by profe para probar
        List<String> phoneNumbers = Arrays.asList(
            "03514767878", "011-46464646", "351-4767878", "5555-552", "258-66666", "0351-2555555"
                ,"011-525252", "02623-552001", "011-1567236669", "0351-155333425", "0351-15533342522"
        );9

        for(String phone:phoneNumbers) {
            System.out.printf("|%s|\t-->\t[%s]\n",
                    phone, Validation.customerPhoneNumber(phone) ? "SI" : "NO"
            );
        } */
        TransactionListener tL = new TransactionListener();
        Thread t1 = new Thread(tL);
        t1.setDaemon(false);
        t1.start();

        Menu m = new Menu();
        m.mainMenu();


    }

}
