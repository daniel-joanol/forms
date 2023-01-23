package com.danieljoanol.forms.constants;

public class Email {
    
    //TODO: change company name
    public static final String SIGNATURE = "Best regards, \nWorkshop Forms team";
    public static final String CODE_TITLE = "Nuevo código de confirmación";
    
    private Email() {}

    public static String codeMessage(String firstName, int code, int timeLimit) {
        return "Hola " + firstName + ", \n\nEl código de confirmación és " + code + ". \n" + 
                "Inserte el código en la pantalla de confirmación para confirmar la operación." +
                "\nEl código es válido por " + timeLimit+ " minutos." +
                "\n\n" + SIGNATURE;
    }
}
