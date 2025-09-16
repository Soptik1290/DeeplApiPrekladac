package prekladac;

import com.deepl.api.*;
import java.nio.file.Files;
import java.nio.file.Paths;

class Test {
    static Translator translator;

    public static void main(String[] args) throws DeepLException, InterruptedException {
        String authKey = System.getenv("DEEPL_AUTH_KEY");
        if (authKey == null || authKey.isEmpty()) {
            try {
                // Pokus o načtení ze souboru .env
                authKey = Files.lines(Paths.get(".env"))
                        .filter(line -> line.trim().startsWith("DEEPL_AUTH_KEY="))
                        .map(line -> line.split("=", 2)[1].trim())
                        .findFirst()
                        .orElse(null);
            } catch (Exception e) {
                // Soubor .env neexistuje nebo nelze přečíst
            }
        }

        if (authKey == null || authKey.isEmpty()) {
            System.err.println("CHYBA: Nenalezen API klic DeepL. Nastavte promennou prostredi DEEPL_AUTH_KEY nebo upravte soubor .env");
            return;
        }

        translator = new Translator(authKey);
        TextResult result =
                translator.translateText("Hello, world!", null, "fr");
        System.out.println(result.getText()); // "Bonjour, le monde !"
    }
}