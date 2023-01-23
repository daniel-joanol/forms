package com.danieljoanol.forms.email;

import com.sparkpost.exception.SparkPostException;

public interface SparkPostService {
    
    boolean sendMesage(String recipient, String title, String message) throws SparkPostException;

}
