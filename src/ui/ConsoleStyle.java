package ui;

public class ConsoleStyle {
    public static final String RESET = "\033[0m";
    // TEXT COLORS
    public static final String BLACK = "\033[30m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE        = "\033[34m";
    public static final String PURPLE      = "\033[35m";
    public static final String CYAN        = "\033[36m";
    public static final String WHITE       = "\033[37m";

    // Bright variants — higher contrast, more intense
    public static final String BRIGHT_RED    = "\033[91m";
    public static final String BRIGHT_GREEN  = "\033[92m";
    public static final String BRIGHT_YELLOW = "\033[93m";
    public static final String BRIGHT_CYAN   = "\033[96m";
    public static final String BRIGHT_WHITE  = "\033[97m";

    // ── TEXT STYLES ──────────────────────────────────────────────────────────
    // Terminal support varies
    public static final String BOLD        = "\033[1m";
    public static final String DIM         = "\033[2m";
    public static final String ITALIC      = "\033[3m";
    public static final String UNDERLINE   = "\033[4m";

    // ── BACKGROUND COLORS ───────────────────────────────────────────────────
    public static final String BG_RED      = "\033[41m";
    public static final String BG_GREEN    = "\033[42m";
    public static final String BG_YELLOW   = "\033[43m";
    public static final String BG_BLUE     = "\033[44m";
    public static final String BG_CYAN     = "\033[46m";

    // ── BOX DRAWING CHARACTERS ───────────────────────────────────────────────
    // Standard Unicode block — U+2500 through U+257F.
    // These are single characters that render as line segments.
    // Combining them correctly creates the illusion of connected borders.

    // Corners
    public static final String TL = "┌";   // Top-left
    public static final String TR = "┐";   // Top-right
    public static final String BL = "└";   // Bottom-left
    public static final String BR = "┘";   // Bottom-right

    // Junctions (T-shapes and cross)
    public static final String TJ = "┬";   // Top junction (T pointing down)
    public static final String BJ = "┴";   // Bottom junction (T pointing up)
    public static final String LJ = "├";   // Left junction (T pointing right)
    public static final String RJ = "┤";   // Right junction (T pointing left)
    public static final String CJ = "┼";   // Cross junction

    // Lines
    public static final String H  = "─";   // Horizontal line
    public static final String V  = "│";   // Vertical line

    // Double-line variants — for title bars and emphasis
    public static final String DH = "═";   // Double horizontal
    public static final String DV = "║";   // Double vertical
    public static final String DTL = "╔";  // Double top-left
    public static final String DTR = "╗";  // Double top-right
    public static final String DBL = "╚";  // Double bottom-left
    public static final String DBR = "╝";  // Double bottom-right

    // ── UTILITY ──────────────────────────────────────────────────────────────
    // Carriage return — moves cursor to start of CURRENT line without newline.
    public static final String CR  = "\r";

    // Terminal bell — emits an audio beep on most terminals.
    public static final String BELL = "\007";

    public static String repeat(String s, int times) {
        return s.repeat(times);
    }

}
