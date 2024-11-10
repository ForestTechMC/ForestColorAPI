package cz.foresttech.api;

import cz.foresttech.api.enums.ColorizeType;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;

/**
 * A utility class for colorizing strings using various color codes and formats.
 *
 * Supports gradient, RGB, and legacy color codes, and provides methods for both standard
 * colorization and MiniMessage formatting.
 */
public class ColorAPI {
    private static final List<String> legacyColors = Arrays.asList("&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e");
    private static final List<String> specialChars = Arrays.asList("&l", "&n", "&o", "&k", "&m", "§l", "§n", "§o", "§k", "§m");
    private static final Pattern patternNormal = Pattern.compile("\\{#([0-9A-Fa-f]{6})\\}");
    private static final Pattern patternGrad = Pattern.compile("\\{#([0-9A-Fa-f]{6})>\\}(.*?)\\{#([0-9A-Fa-f]{6})<\\}");
    private static final Pattern patternOneFromTwo = Pattern.compile("\\{#([0-9A-Fa-f]{6})<>\\}");

    /**
     * Colorizes the input string based on the specified {@link ColorizeType}.
     *
     * @param type  The type of colorization to apply (e.g., GRADIENT, RGB, CLASSIC).
     * @param input The input string to colorize.
     * @return The colorized string.
     */
    public static String colorizeType(ColorizeType type, String input) {
        String var10000;
        switch (type) {
            case GRADIENT -> var10000 = colorizeGradient(input);
            case RGB -> var10000 = colorizeRGB(input);
            case CLASSIC -> var10000 = colorizeClassic(input);
            default -> throw new IncompatibleClassChangeError();
        }
        return var10000;
    }

    /**
     * Removes patterns, legacy colors, and special characters from the input string.
     *
     * @param input The input string to be cleared.
     * @return The cleaned string without patterns, legacy colors, and special characters.
     */
    public static String clear(String input) {
        input = removePatterns(input);
        input = removeLegacyColors(input);
        input = removeSpecialChars(input);
        return input;
    }

    /**
     * Removes special formatting characters (e.g., &l, &n) from the input string.
     *
     * Example: "&kForestTech ❤" -> "ForestTech ❤"
     *
     * @param input The input string.
     * @return The string without special formatting characters.
     */
    public static String removeSpecialChars(String input) {
        for (String chars : specialChars) {
            if (input.contains(chars)) {
                input = input.replaceAll(chars, "");
            }
        }
        return input;
    }

    /**
     * Removes legacy color codes (e.g., &2, &c) from the input string.
     *
     * Example: "&2ForestTech ❤" -> "ForestTech ❤"
     *
     * @param input The input string.
     * @return The string without legacy color codes.
     */
    public static String removeLegacyColors(String input) {
        for (String color : legacyColors) {
            if (input.contains(color)) {
                input = input.replaceAll(color, "");
            }
        }
        return input;
    }

    /**
     * Removes color patterns (e.g., {#RRGGBB}) from the input string.
     *
     * Example: "{#00e64e}ForestTech ❤" -> "ForestTech ❤"
     *
     * @param input The input string containing patterns.
     * @return The string without color patterns.
     */
    public static String removePatterns(String input) {
        input = input.replaceAll("\\{#([0-9A-Fa-f]{6})>\\}", "");
        input = input.replaceAll("\\{#([0-9A-Fa-f]{6})<\\}", "");
        input = input.replaceAll("\\{#([0-9A-Fa-f]{6})\\}", "");
        return input;
    }

    /**
     * Applies gradient and RGB colorization to the input string.
     *
     * @param input The input string.
     * @return The colorized string.
     */
    public static String colorize(String input) {
        input = colorizeGradient(input);
        input = colorizeRGB(input);
        return input;
    }

    /**
     * Translates legacy color codes in the input string using '&' as the color code character.
     *
     * @param input The input string containing legacy color codes.
     * @return The string with translated color codes.
     */
    public static String colorizeClassic(String input) {
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }

    /**
     * Applies gradient colorization to the input string using gradient patterns.
     *
     * Gradient patterns are of the form: {#RRGGBB>}{text}{#RRGGBB<}
     *
     * @param input The input string containing gradient patterns.
     * @return The string with gradient colors applied.
     */
    public static String colorizeGradient(String input) {
        Matcher matcher = patternOneFromTwo.matcher(input);
        StringBuilder output = new StringBuilder();

        while (matcher.find()) {
            String text = matcher.group(1);
            matcher.appendReplacement(output, "{#" + text + "<}{#" + text + ">}");
        }
        matcher.appendTail(output);
        input = output.toString();

        for (matcher = patternGrad.matcher(input); matcher.find(); input = input.replace(matcher.group(), color(matcher.group(2), new Color(Integer.parseInt(matcher.group(1), 16)), new Color(Integer.parseInt(matcher.group(3), 16))))) {
        }
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /**
     * Applies RGB colorization to the input string using RGB patterns.
     *
     * RGB patterns are of the form: {#RRGGBB}
     *
     * @param input The input string containing RGB patterns.
     * @return The string with RGB colors applied.
     */
    public static String colorizeRGB(String input) {
        Matcher matcher = patternNormal.matcher(input);
        StringBuilder result = new StringBuilder(input.length());

        int lastEnd;
        for (lastEnd = 0; matcher.find(); lastEnd = matcher.end()) {
            String color = matcher.group(1);
            result.append(input, lastEnd, matcher.start());
            if (color != null) {
                result.append(getColor(color));
            }
        }
        result.append(input.substring(lastEnd));
        return result.toString();
    }

    /**
     * Applies a gradient between two colors to the input string.
     *
     * @param input  The text to which the gradient will be applied.
     * @param first  The starting color of the gradient.
     * @param second The ending color of the gradient.
     * @return The string with gradient colors applied.
     */
    public static String color(String input, Color first, Color second) {
        ChatColor[] colors = createGradient(first, second, removeSpecialChars(input).length());
        return apply(input, colors);
    }

    /**
     * Applies the given array of colors to the input string, considering special formatting characters.
     *
     * @param input  The input string.
     * @param colors An array of ChatColor to apply to the input string.
     * @return The colorized string.
     */
    private static String apply(String input, ChatColor[] colors) {
        StringBuilder specialColors = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        String[] characters = input.split("");
        int outIndex = 0;

        for (int i = 0; i < characters.length; ++i) {
            if (!characters[i].equals("&") && !characters[i].equals("§")) {
                stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
            } else if (i + 1 >= characters.length) {
                stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
            } else {
                if (characters[i + 1].equals("r")) {
                    specialColors.setLength(0);
                } else {
                    specialColors.append(characters[i]);
                    specialColors.append(characters[i + 1]);
                }
                ++i;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Creates an array of ChatColor representing a gradient between two colors.
     *
     * @param first  The starting color of the gradient.
     * @param second The ending color of the gradient.
     * @param amount The number of colors to generate in the gradient.
     * @return An array of ChatColor representing the gradient.
     */
    private static ChatColor[] createGradient(Color first, Color second, int amount) {
        ChatColor[] colors = new ChatColor[amount];
        int amountR = Math.abs(first.getRed() - second.getRed()) / (amount - 1);
        int amountG = Math.abs(first.getGreen() - second.getGreen()) / (amount - 1);
        int amountB = Math.abs(first.getBlue() - second.getBlue()) / (amount - 1);
        int[] colorDir = new int[]{first.getRed() < second.getRed() ? 1 : -1, first.getGreen() < second.getGreen() ? 1 : -1, first.getBlue() < second.getBlue() ? 1 : -1};

        for (int i = 0; i < amount; ++i) {
            Color color = new Color(first.getRed() + amountR * i * colorDir[0], first.getGreen() + amountG * i * colorDir[1], first.getBlue() + amountB * i * colorDir[2]);
            colors[i] = ChatColor.of(color);
        }
        return colors;
    }

    /**
     * Converts a hex color string to a ChatColor.
     *
     * @param matcher The hex color string (e.g., "FF0000").
     * @return The corresponding ChatColor.
     */
    public static ChatColor getColor(String matcher) {
        return ChatColor.of(new Color(Integer.parseInt(matcher, 16)));
    }

    // New methods using MiniMessage

    /**
     * Colorizes the input string using MiniMessage format, applying gradient, RGB, and classic colorizations.
     *
     * @param input The input string.
     * @return A Component with colorizations applied.
     */
    public static Component colorizeMM(String input) {
        input = colorizeGradientMM(input);
        input = colorizeRGBMM(input);
        input = colorizeClassicMM(input);
        return MiniMessage.miniMessage().deserialize(input);
    }

    /**
     * Applies gradient colorization to the input string using MiniMessage format.
     *
     * Replaces gradient patterns with MiniMessage gradient tags.
     *
     * @param input The input string containing gradient patterns.
     * @return The string with MiniMessage gradient tags applied.
     */
    public static String colorizeGradientMM(String input) {
        Matcher matcher = patternOneFromTwo.matcher(input);
        StringBuilder output = new StringBuilder();

        while (matcher.find()) {
            String color = matcher.group(1);
            matcher.appendReplacement(output, "{#" + color + "<}{#" + color + ">}");
        }
        matcher.appendTail(output);
        input = output.toString();

        matcher = patternGrad.matcher(input);
        output.setLength(0);
        int lastEnd = 0;
        while (matcher.find()) {
            String startColor = matcher.group(1);
            String text = matcher.group(2);
            String endColor = matcher.group(3);

            output.append(input, lastEnd, matcher.start());

            output.append("<gradient:#").append(startColor).append(":#").append(endColor).append(">");
            output.append(text);
            output.append("</gradient>");

            lastEnd = matcher.end();
        }
        output.append(input.substring(lastEnd));
        input = output.toString();

        return input;
    }

    /**
     * Applies RGB colorization to the input string using MiniMessage format.
     *
     * Replaces RGB patterns with MiniMessage color tags.
     *
     * @param input The input string containing RGB patterns.
     * @return The string with MiniMessage color tags applied.
     */
    public static String colorizeRGBMM(String input) {
        Matcher matcher = patternNormal.matcher(input);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String color = matcher.group(1);
            // Replace {#RRGGBB} with <color:#RRGGBB>
            matcher.appendReplacement(result, "<color:#" + color + ">");
        }
        matcher.appendTail(result);
        input = result.toString();

        return input;
    }

    /**
     * Translates legacy color codes in the input string to MiniMessage format.
     *
     * @param input The input string containing legacy color codes.
     * @return The string with MiniMessage color and format tags applied.
     */
    public static String colorizeClassicMM(String input) {
        Map<Character, String> colorCodeMap = Map.ofEntries(
            Map.entry('0', "black"),
            Map.entry('1', "dark_blue"),
            Map.entry('2', "dark_green"),
            Map.entry('3', "dark_aqua"),
            Map.entry('4', "dark_red"),
            Map.entry('5', "dark_purple"),
            Map.entry('6', "gold"),
            Map.entry('7', "gray"),
            Map.entry('8', "dark_gray"),
            Map.entry('9', "blue"),
            Map.entry('a', "green"),
            Map.entry('b', "aqua"),
            Map.entry('c', "red"),
            Map.entry('d', "light_purple"),
            Map.entry('e', "yellow"),
            Map.entry('f', "white")
        );

        Map<Character, String> formatCodeMap = Map.ofEntries(
            Map.entry('k', "obfuscated"),
            Map.entry('l', "bold"),
            Map.entry('m', "strikethrough"),
            Map.entry('n', "underlined"),
            Map.entry('o', "italic"),
            Map.entry('r', "reset")
        );

        StringBuilder output = new StringBuilder();
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&' || chars[i] == '§') {
                if (i + 1 < chars.length) {
                    char code = chars[i + 1];
                    if (colorCodeMap.containsKey(code)) {
                        output.append("<color:").append(colorCodeMap.get(code)).append(">");
                        i++;
                    } else if (formatCodeMap.containsKey(code)) {
                        output.append("<").append(formatCodeMap.get(code)).append(">");
                        i++;
                    } else {
                        output.append(chars[i]);
                    }
                } else {
                    output.append(chars[i]);
                }
            } else {
                output.append(chars[i]);
            }
        }
        return output.toString();
    }

    /**
     * Deserializes a MiniMessage-formatted string into a Component.
     *
     * @param miniMessageString The MiniMessage-formatted string.
     * @return A Component representing the deserialized string.
     */
    public static Component mm(String miniMessageString) {
        return MiniMessage.miniMessage().deserialize(miniMessageString);
    }
}
