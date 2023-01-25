package com.danieljoanol.forms.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SparkPost implements SparkPostService {

    @Value("${spark.api.key}")
    private String API_KEY;

    // TODO: update email
    @Value("${spark.api.email}")
    private String API_EMAIL;

    Client client = new Client(API_KEY);

    @Override
    public boolean sendMesage(String recipient, String title, String message) throws SparkPostException {

        try {
            client.sendMessage(
                    API_EMAIL,
                    recipient,
                    title,
                    message,
                    message);
        } catch (SparkPostException ex) {
            log.error(ex.getMessage(), ex);
            return false;
        }

        return true;
    }
}
