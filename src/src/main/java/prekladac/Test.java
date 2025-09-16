package prekladac;

import com.deepl.api.*;

class Test {
    static Translator translator;


    public static void main(String[] args) throws DeepLException, InterruptedException {
        String authKey = "24c552bf-c8d0-0bbf-8a44-62cc8a9f7e9b:fx";  // Replace with your key
        translator = new Translator(authKey);
        TextResult result =
                translator.translateText("Hello, world!", null, "fr");
        System.out.println(result.getText()); // "Bonjour, le monde !"
    }
}