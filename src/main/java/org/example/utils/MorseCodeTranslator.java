package org.example.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MorseCodeTranslator {
    private static final Map<Character, String> MORSE_CODE_MAP = new HashMap<Character, String>() {{
        put('A', ".-");
        put('B', "-...");
        put('C', "-.-.");
        put('D', "-..");
        put('E', ".");
        put('F', "..-.");
        put('G', "--.");
        put('H', "....");
        put('I', "..");
        put('J', ".---");
        put('K', "-.-");
        put('L', ".-..");
        put('M', "--");
        put('N', "-.");
        put('O', "---");
        put('P', ".--.");
        put('Q', "--.-");
        put('R', ".-.");
        put('S', "...");
        put('T', "-");
        put('U', "..-");
        put('V', "...-");
        put('W', ".--");
        put('X', "-..-");
        put('Y', "-.--");
        put('Z', "--..");
    }};

    public static String textToMorseCode(String text) {
        StringBuilder morseBuilder = new StringBuilder();
        for (char c : text.toUpperCase().toCharArray()) {
            if (MORSE_CODE_MAP.containsKey(c)) {
                morseBuilder.append(MORSE_CODE_MAP.get(c)).append(" ");
            } else if (c == ' ') {
                morseBuilder.append("   ");
            }
        }
        return morseBuilder.toString().trim();
    }

    public static String morseCodeToText(String morseCode) {
        StringBuilder textBuilder = new StringBuilder();
        String[] words = morseCode.split("   ");
        for (String word : words) {
            for (String letter : word.split(" ")) {
                Optional<Character> decodedLetter = MORSE_CODE_MAP.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(letter))
                        .map(Map.Entry::getKey)
                        .findFirst();
                decodedLetter.ifPresent(textBuilder::append);
            }
            textBuilder.append(" ");
        }
        return textBuilder.toString().trim();
    }
}
