package com.danieljoanol.forms.constants;

public class Email {
    
    //TODO: change company name
    public static final String SIGNATURE = "Un saludo cordial,<br>Equipo de Workshop Forms";
    public static final String CODE_TITLE = "Nuevo código de confirmación";
    
    private Email() {}

    public static String codeMessage(String firstName, int code, int timeLimit) {

        StringBuilder sb = new StringBuilder();
        sb.append("Hola " + firstName + ",<br><br>");
        sb.append("El código de confirmación és " + code + ".<br>");
        sb.append("Inserta el código en la pantalla de confirmación para confirmar la operación.<br>");
        sb.append("Acuerdate que el código es válido por " + timeLimit+ " minutos.<br><br>");
        sb.append(SIGNATURE);
        
        return sb.toString();
    }
}
