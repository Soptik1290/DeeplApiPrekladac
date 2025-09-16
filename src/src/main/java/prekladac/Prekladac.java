/**
 * Prekladac je jednoduché grafické uživatelské rozhraní pro překlad pomocí DeepL API.
 * Umožňuje uživatelům vybrat zdrojový a cílový jazyk, vložit text k přeložení a provádět překladové operace.
 * Další funkcionalita zahrnuje ukládání a načítání textu ze souboru.
 */

package prekladac;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Hlavní třída, která vytváří grafické uživatelské rozhraní a zpracovává překladové operace.
 */
public class Prekladac {
    private static Translator translator;
    private static JTextArea resultTextArea;

    /**
     * Hlavní metoda, která inicializuje GUI a nastavuje posluchače událostí pro překlad a operace se soubory.
     *
     * @param args Příkazové řádky (nejsou v této aplikaci použity).
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Prekladac");

        // Seznamy jazyků pro zdrojový jazyk
        List<Jazyk> sourceJazyks = Arrays.asList(
                new Jazyk("Bulgarian", "BG"),
                new Jazyk("Czech", "CS"),
                new Jazyk("Danish", "DA"),
                new Jazyk("German", "DE"),
                new Jazyk("Greek", "EL"),
                new Jazyk("English", "EN"),
                new Jazyk("Spanish", "ES"),
                new Jazyk("Estonian", "ET"),
                new Jazyk("Finnish", "FI"),
                new Jazyk("French", "FR"),
                new Jazyk("Hungarian", "HU"),
                new Jazyk("Indonesian", "ID"),
                new Jazyk("Italian", "IT"),
                new Jazyk("Japanese", "JA"),
                new Jazyk("Korean", "KO"),
                new Jazyk("Lithuanian", "LT"),
                new Jazyk("Latvian", "LV"),
                new Jazyk("Norwegian (Bokmål)", "NB"),
                new Jazyk("Dutch", "NL"),
                new Jazyk("Polish", "PL"),
                new Jazyk("Portuguese", "PT"),
                new Jazyk("Romanian", "RO"),
                new Jazyk("Russian", "RU"),
                new Jazyk("Slovak", "SK"),
                new Jazyk("Slovenian", "SL"),
                new Jazyk("Swedish", "SV"),
                new Jazyk("Turkish", "TR"),
                new Jazyk("Ukrainian", "UK"),
                new Jazyk("Chinese", "ZH")
        );
        // Seznamy jazyků pro cílový jazyk
        List<Jazyk> targetJazyks = Arrays.asList(
                new Jazyk("Bulgarian", "BG"),
                new Jazyk("Czech", "CS"),
                new Jazyk("Danish", "DA"),
                new Jazyk("German", "DE"),
                new Jazyk("Greek", "EL"),
                new Jazyk("English - GB", "EN-GB"),
                new Jazyk("English - US", "EN-US"),
                new Jazyk("Spanish", "ES"),
                new Jazyk("Estonian", "ET"),
                new Jazyk("Finnish", "FI"),
                new Jazyk("French", "FR"),
                new Jazyk("Hungarian", "HU"),
                new Jazyk("Indonesian", "ID"),
                new Jazyk("Italian", "IT"),
                new Jazyk("Japanese", "JA"),
                new Jazyk("Korean", "KO"),
                new Jazyk("Lithuanian", "LT"),
                new Jazyk("Latvian", "LV"),
                new Jazyk("Norwegian (Bokmål)", "NB"),
                new Jazyk("Dutch", "NL"),
                new Jazyk("Polish", "PL"),
                new Jazyk("Portuguese - Portugal", "PT-PT"),
                new Jazyk("Portuguese - Brazil", "PT-BR"),
                new Jazyk("Romanian", "RO"),
                new Jazyk("Russian", "RU"),
                new Jazyk("Slovak", "SK"),
                new Jazyk("Slovenian", "SL"),
                new Jazyk("Swedish", "SV"),
                new Jazyk("Turkish", "TR"),
                new Jazyk("Ukrainian", "UK"),
                new Jazyk("Chinese", "ZH")
        );

        // Posluchače událostí a nastavení GUI
        JLabel sourceLabel = new JLabel("Zdrojový jazyk:");
        JComboBox<Jazyk> sourceLanguageComboBox = new JComboBox<>(sourceJazyks.toArray(new Jazyk[0]));
        JLabel targetLabel = new JLabel("Cílový jazyk:");
        JComboBox<Jazyk> targetLanguageComboBox = new JComboBox<>(targetJazyks.toArray(new Jazyk[0]));
        JLabel textLabel = new JLabel("Text k přeložení:");
        JTextArea textArea = new JTextArea(10, 30);
        JButton translateButton = new JButton("Přeložit");
        JButton saveButton = new JButton("Uložit do souboru");
        JButton loadButton = new JButton("Načíst ze souboru");

        resultTextArea = new JTextArea(10, 30);

        translator = new Translator("24c552bf-c8d0-0bbf-8a44-62cc8a9f7e9b:fx");

        // Layout
        JPanel panel = new JPanel();
        panel.add(sourceLabel);
        panel.add(sourceLanguageComboBox);
        panel.add(targetLabel);
        panel.add(targetLanguageComboBox);
        panel.add(textLabel);
        panel.add(new JScrollPane(textArea));
        panel.add(new JScrollPane(resultTextArea));
        panel.add(translateButton);
        panel.add(saveButton);
        panel.add(loadButton);

        frame.add(panel);

        // Akce
        translateButton.addActionListener(e -> {
            Jazyk zdrojovyJazyk = (Jazyk) sourceLanguageComboBox.getSelectedItem();
            Jazyk cilovyJazyk = (Jazyk) targetLanguageComboBox.getSelectedItem();
            String prekladanyText = textArea.getText();

            try {
                assert zdrojovyJazyk != null;
                assert cilovyJazyk != null;
                TextResult result = translator.translateText(prekladanyText, zdrojovyJazyk.getCode(), cilovyJazyk.getCode());
                resultTextArea.setText(result.getText());
            } catch (DeepLException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        UlozitDoSouboru(saveButton, frame, textArea, resultTextArea);

        NacistSoubor(loadButton, frame, textArea, resultTextArea);

        // Nastavení okna
        frame.setSize(750, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Posluchač události pro načítání textu ze souboru do textové oblasti.
     *
     * @param loadButton Tlačítko spouštějící akci.
     * @param frame      Hlavní JFrame.
     * @param textArea   JTextArea pro vstupní text.
     * @param resultTextArea JTextArea pro přeložený text.
     */
    private static void NacistSoubor(JButton loadButton, JFrame frame, JTextArea textArea, JTextArea resultTextArea) {
        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try (BufferedReader cteni = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                    StringBuilder neprelozenyText = new StringBuilder();
                    StringBuilder prelozenyText = new StringBuilder();
                    boolean isPrelozeny = false;
                    String line;

                    while ((line = cteni.readLine()) != null) {
                        if (line.equals("Nepřeložený text:")) {
                            isPrelozeny = false;
                            continue;
                        } else if (line.equals("Přeložený text:")) {
                            isPrelozeny = true;
                            continue;
                        }

                        if (isPrelozeny) {
                            prelozenyText.append(line).append("\n");
                        } else {
                            neprelozenyText.append(line).append("\n");
                        }
                    }

                    textArea.setText(neprelozenyText.toString().trim());
                    resultTextArea.setText(prelozenyText.toString().trim());

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Posluchač události pro ukládání textu z textové oblasti do souboru.
     *
     * @param saveButton Tlačítko spouštějící akci.
     * @param frame      Hlavní JFrame.
     * @param textArea   JTextArea pro vstupní text.
     * @param resultTextArea JTextArea pro přeložený text.
     */
    private static void UlozitDoSouboru(JButton saveButton, JFrame frame, JTextArea textArea, JTextArea resultTextArea) {
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fileWriter = new FileWriter(fileChooser.getSelectedFile())) {
                    // Uložení nepřeloženého textu
                    fileWriter.write("Nepřeložený text:\n");
                    fileWriter.write(textArea.getText() + "\n\n");

                    // Uložení přeloženého textu
                    fileWriter.write("Přeložený text:\n");
                    fileWriter.write(resultTextArea.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    /**
     * Představuje jazyk se jménem a kódem jazyka.
     */
    public static class Jazyk {
        private final String name;
        private final String code;


        /**
         * Konstruktor objektu Language se specifikovaným jménem a kódem.
         *
         * @param name Jméno jazyka.
         * @param code Kód jazyka.
         */
        public Jazyk(String name, String code) {
            this.name = name;
            this.code = code;
        }
        /**
         * Vrací jméno jazyka.
         *
         * @return Jméno jazyka.
         */
        @Override
        public String toString() {
            return name;
        }

        /**
         * Vrací kód jazyka.
         *
         * @return Kód jazyka.
         */
        public String getCode() {
            return code;
        }
    }
}
