package prekladac;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PrekladacJava {
    static Translator translator;

    public static void main(String[] args) throws DeepLException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Vyberte zdrojový jazyk (např. 'en' pro angličtinu): ");
        String sourceLanguage = scanner.nextLine();

        System.out.print("Vyberte cílový jazyk (např. 'fr' pro francouzštinu): ");
        String targetLanguage = scanner.nextLine();

        System.out.print("Zadejte text k přeložení: ");
        String textToTranslate = scanner.nextLine();

        String authKey = "24c552bf-c8d0-0bbf-8a44-62cc8a9f7e9b:fx";  // Nahraďte svým klíčem
        translator = new Translator(authKey);

        TextResult result = translator.translateText(textToTranslate, sourceLanguage, targetLanguage);

        System.out.println("Přeložený text: " + result.getText());

        System.out.print("Zadejte název souboru pro uložení výsledku: ");
        String fileName = scanner.nextLine();

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(result.getText());
            System.out.println("Výsledek překladu byl uložen do souboru '" + fileName + "'.");
        } catch (IOException e) {
            System.err.println("Chyba při zápisu do souboru: " + e.getMessage());
        }

        System.out.print("Chcete přečíst výsledek z uloženého souboru? (ano/ne): ");
        String readOption = scanner.nextLine();

        if (readOption.equalsIgnoreCase("ano")) {
            try (FileReader fileReader = new FileReader(fileName)) {
                Scanner fileScanner = new Scanner(fileReader);
                while (fileScanner.hasNextLine()) {
                    System.out.println(fileScanner.nextLine());
                }
            } catch (IOException e) {
                System.err.println("Chyba při čtení souboru: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
