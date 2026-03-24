package com.example.vntravelapp.utils;

import java.text.Normalizer;
import java.util.Locale;

public final class SearchUtils {

    private SearchUtils() {
    }

    public static String normalize(String input) {
        if (input == null) {
            return "";
        }
        String text = Normalizer.normalize(input, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        text = text.replace('đ', 'd').replace('Đ', 'D');
        text = text.toLowerCase(Locale.getDefault());
        text = text.replaceAll("[^a-z0-9]+", " ");
        text = text.trim().replaceAll("\\s+", " ");
        return text;
    }

    public static int levenshteinDistance(String a, String b, int maxDistance) {
        if (a == null || b == null) {
            return Integer.MAX_VALUE;
        }
        if (a.equals(b)) {
            return 0;
        }
        int lenA = a.length();
        int lenB = b.length();
        if (Math.abs(lenA - lenB) > maxDistance) {
            return maxDistance + 1;
        }
        int[] prev = new int[lenB + 1];
        int[] curr = new int[lenB + 1];
        for (int j = 0; j <= lenB; j++) {
            prev[j] = j;
        }
        for (int i = 1; i <= lenA; i++) {
            curr[0] = i;
            int minInRow = curr[0];
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= lenB; j++) {
                int cost = (ca == b.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                if (curr[j] < minInRow) {
                    minInRow = curr[j];
                }
            }
            if (minInRow > maxDistance) {
                return maxDistance + 1;
            }
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return prev[lenB];
    }

    public static double matchScore(String normalizedQuery, String normalizedTarget) {
        if (normalizedQuery == null || normalizedTarget == null) {
            return 0.0;
        }
        if (normalizedQuery.isEmpty() || normalizedTarget.isEmpty()) {
            return 0.0;
        }
        if (normalizedTarget.contains(normalizedQuery)) {
            return 100.0;
        }

        String[] queryTokens = splitTokens(normalizedQuery);
        String[] targetTokens = splitTokens(normalizedTarget);
        if (queryTokens.length == 0 || targetTokens.length == 0) {
            return 0.0;
        }

        int matched = 0;
        double similaritySum = 0.0;
        for (String q : queryTokens) {
            double best = bestTokenSimilarity(q, targetTokens);
            if (best >= 0.78) {
                matched++;
                similaritySum += best;
            }
        }

        if (matched == 0) {
            return 0.0;
        }

        double coverage = (double) matched / queryTokens.length;
        if (coverage < 0.6) {
            return 0.0;
        }

        double average = similaritySum / matched;
        return (average * 0.7 + coverage * 0.3) * 100.0;
    }

    public static String[] splitTokens(String normalizedText) {
        if (normalizedText == null || normalizedText.isEmpty()) {
            return new String[0];
        }
        return normalizedText.split(" ");
    }

    private static double bestTokenSimilarity(String token, String[] targetTokens) {
        double best = 0.0;
        for (String t : targetTokens) {
            double sim = tokenSimilarity(token, t);
            if (sim > best) {
                best = sim;
            }
            if (best >= 0.98) {
                break;
            }
        }
        return best;
    }

    private static double tokenSimilarity(String a, String b) {
        if (a.equals(b)) {
            return 1.0;
        }
        if (a.length() <= 2 || b.length() <= 2) {
            return 0.0;
        }
        if (b.startsWith(a) || a.startsWith(b)) {
            int minLen = Math.min(a.length(), b.length());
            int maxLen = Math.max(a.length(), b.length());
            return 0.92 + (0.08 * minLen / Math.max(1, maxLen));
        }
        int maxLen = Math.max(a.length(), b.length());
        int maxDistance = Math.max(1, (int) Math.floor(maxLen * 0.34));
        int distance = levenshteinDistance(a, b, maxDistance);
        if (distance > maxDistance) {
            return 0.0;
        }
        return 1.0 - ((double) distance / maxLen);
    }
}
