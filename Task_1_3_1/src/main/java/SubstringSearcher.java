import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class SubstringSearcher {
    private SubstringSearcher() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<Integer> find(String filename, String pattern) throws IOException {
        List<Integer> result = new ArrayList<>();
        if (pattern == null || pattern.length() == 0) {
            return result;
        }

        char[] pat = pattern.toCharArray();
        int m = pat.length;
        int[] lps = computeLps(pat);

        FileInputStream fis = new FileInputStream(filename);
        try (InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            char[] buffer = new char[8192];
            int read;
            // Текущая длина совпадения
            int j = 0;
            // иднекс следющего символа
            long charIndex = 0;

            while ((read = reader.read(buffer)) != -1) {
                for (int i = 0; i < read; ++i) {
                    char c = buffer[i];
                    while (j > 0 && c != pat[j]) {
                        j = lps[j - 1];
                    }
                    if (c == pat[j]) {
                        j++;
                        if (j == m) {
                            long start = charIndex - m + 1;
                            result.add((int) start);
                            j = lps[j - 1];
                        }
                    }
                    charIndex++;
                }
            }
        }

        return result;
    }

    private static int[] computeLps(char[] pat) {
        int m = pat.length;
        int[] lps = new int[m];
        lps[0] = 0;
        // Длина предыдущего наибольшего префиксного суффикса
        int len = 0;
        int i = 1;
        while (i < m) {
            if (pat[i] == pat[len]) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
}
