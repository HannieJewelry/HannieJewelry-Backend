package hanniejewelry.vn.shared.utils;

import java.text.Normalizer;
import java.util.function.Predicate;

public final class SlugUtils {
    private SlugUtils() {}

    public static String toSlug(String input) {
        if (input == null) return "";
        String slug = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        return slug;
    }


    public static String toSlugUnique(String title, Predicate<String> exists) {
        String base = toSlug(title);
        String slug = base;
        int count = 1;
        while (exists.test(slug)) {
            slug = base + "-" + count++;
        }
        return slug;
    }
}
