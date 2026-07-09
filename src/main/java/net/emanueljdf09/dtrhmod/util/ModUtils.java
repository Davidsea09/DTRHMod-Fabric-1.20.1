package net.emanueljdf09.dtrhmod.util;

import java.util.ArrayList;
import java.util.List;

public class ModUtils {

    public static List<String> splitIntoPages(String text) {
        List<String> pages = new ArrayList<>();
        String[] words = text.trim().split("\\s+");
        StringBuilder currentPage = new StringBuilder();

        for (String word : words) {
            if (currentPage.length() + word.length() + 1 > 255) {
                pages.add(currentPage.toString().trim());
                currentPage.setLength(0);
            }
            currentPage.append(word).append(" ");
        }
        if (!currentPage.isEmpty()) {
            pages.add(currentPage.toString().trim());
        }
        return pages;
    }
}
