package com.danieljoanol.forms.constants;

public class Message {

    public static final String CHECK_EMAIL = "Revisa tu bandeja de entrada del correo electrónico";
    public static final String SPARK_POST_ERROR = "Estamos teniendo problemas con el envío de emails. Por favor, intenta nuevamente más tarde";
    public static final String LOG_OUT = "Has sido desconectado";
    public static final String INVALID_CODE = "Código erroneo";
    public static final String CODE_EXPIRED = "Código caducado. Es necesario generar otro código";
    public static final String UPDATED_USERNAME = "Usuário alterado con éxito";
    public static final String UPDATED_PASSWORD = "Contraseña alterada con éxito";

    public static final String USERNAME_NOT_FOUND = "Username not found";
    public static final String ENTITY_NOT_FOUND = "Entity not found";
    public static final String DUPLICATE_USERNAME = "Username already being used";
    public static final String ID_NOT_FOUND = "Id not found";
    public static final String NOT_AUTHORIZED = "Not authorized!";

    private Message() {
    }

    public static String notBlank(String param) {
        return param + " can't be blank";
    }

    public static String notNull(String param) {
        return param + " can't be null";
    }

    public static String userBlocked(String username) {
        return "El usuario " + username + " está bloqueado. Consulte el administrador";
    }

}
