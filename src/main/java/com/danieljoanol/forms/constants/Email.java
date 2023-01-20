package com.danieljoanol.forms.constants;

public class Email {
    
    //TODO: change name
    public static final String SIGNATURE = "Best regards, \nWorkshop Forms team";
    public static final String TITLE_CODE = "Nuevo código de confirmación";
    
    private Email() {}

    public static String messageCode(String firstName, int code) {
        return "Hola " + firstName + ", \n\nEl código de confirmación és " + code + ". \n" + 
                "Inserte el código en la pantalla de confirmación para confirmar la operación." +
                "\n\n" + SIGNATURE;
    }
}
