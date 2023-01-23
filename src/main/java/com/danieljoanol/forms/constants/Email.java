package com.danieljoanol.forms.constants;

import org.springframework.beans.factory.annotation.Value;

public class Email {

    @Value("${forms.app.code.limit}")
    private static Integer TIME_LIMIT;
    
    //TODO: change company name
    public static final String SIGNATURE = "Best regards, \nWorkshop Forms team";
    public static final String CODE_TITLE = "Nuevo código de confirmación";
    
    private Email() {}

    public static String codeMessage(String firstName, int code) {
        return "Hola " + firstName + ", \n\nEl código de confirmación és " + code + ". \n" + 
                "Inserte el código en la pantalla de confirmación para confirmar la operación." +
                "\nEl código es válido por " + TIME_LIMIT + " minutos." +
                "\n\n" + SIGNATURE;
    }
}
