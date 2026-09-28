package translation;

import javax.swing.*;
import java.awt.event.*;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator();
            CountryCodeConverter countryConverter = new CountryCodeConverter();
            LanguageCodeConverter languageConverter = new LanguageCodeConverter();

            JPanel countryPanel = new JPanel();

            DefaultListModel<String> countryModel = new DefaultListModel<>();

            for (String countryCode : translator.getCountryCodes()) {
                String countryName = countryConverter.fromCountryCode(countryCode);
                countryModel.addElement(countryName);
            }

            JList<String> countryList = new JList<>(countryModel);
            JScrollPane countryScrollPane = new JScrollPane(countryList);

            countryPanel.add(new JLabel("Country:"));
            countryPanel.add(countryScrollPane);

            JPanel languagePanel = new JPanel();

            JComboBox<String> languageBox = new JComboBox<>();

            for (String languageCode : translator.getLanguageCodes()) {
                String languageName = languageConverter.fromLanguageCode(languageCode);
                languageBox.addItem(languageName);
            }

            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageBox);

            JPanel buttonPanel = new JPanel();

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);

            JLabel resultLabel = new JLabel("");
            buttonPanel.add(resultLabel);

            // adding listener for when the user clicks the submit button
            Runnable updateTranslation = () -> {
                String countryName = countryList.getSelectedValue();
                String languageName = (String) languageBox.getSelectedItem();

                if (countryName == null || languageName == null) {
                    return;
                }

                String countryCode = countryConverter.fromCountry(countryName);
                String languageCode = languageConverter.fromLanguage(languageName);

                String result = translator.translate(countryCode, languageCode);

                if (result == null) {
                    result = "no translation found!";
                }

                resultLabel.setText(result);
            };

            countryList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    updateTranslation.run();
                }
            });

            languageBox.addActionListener(e -> updateTranslation.run());

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
