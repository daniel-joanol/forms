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
}
