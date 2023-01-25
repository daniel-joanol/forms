package com.danieljoanol.forms.util;

import java.util.Random;

public class CodeGeneration {

    public static int newCode() {
        Random random = new Random();
        return 100000 + random.nextInt(899999);
    }
    
}
