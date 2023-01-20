package com.danieljoanol.forms.constants;

public class Message {

    public static final String MESSAGE_CHECK_EMAIL = "Revisa tu bandeja de entrada del correo electrónico";
    public static final String LOG_OUT = "Has sido desconectado";

    private Message() {
    }

    public static String notBlank(String param) {
        return param + " can't be blank";
    }

    public static String notNull(String param) {
        return param + " can't be null";
    }

    public static String usernameNotFound() {
        return "Username not found";
    }

    public static String entityNotFound() {
        return "Entity not found";
    }

    public static String idNotFound(Long id) {
        return "Id " + id + " not found";
    }

    public static String duplicateUsername() {
        return "Username already being used";
    }

    public static String userBlocked(String username) {
        return  "El usuario " + username + " está bloqueado. Consulte el administrador";
    }

    public static String notAuthorized() {
        return "Not authorized!";
    }
}
