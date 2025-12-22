package util;

public class MarkdownUtil {
	private MarkdownUtil() {
        // to prevent instantiation
    }
	// LLM returns a response in Markdown format, which is then converted to HTML format suitable for the GUI to avoid breaking the syntax.
	public static String markdownToHtml(String md) {
        if (md == null || md.trim().isEmpty()) return "";

        String html = md;

        // HTML escape
        html = html.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");

        // Markdown headings
        html = html.replaceAll("(?m)^### (.+)$", "<h4>$1</h4>");
        html = html.replaceAll("(?m)^## (.+)$", "<h3>$1</h3>");
        html = html.replaceAll("(?m)^# (.+)$", "<h2>$1</h2>");

        // Bold
        html = html.replaceAll("\\*\\*(.+?)\\*\\*", "<b>$1</b>");

        // LaTeX-ish cleanup
        html = html.replaceAll("\\$(.+?)\\$", "$1"); // $...$ 제거
        html = html.replace("\\(", "").replace("\\)", "");

        // Line breaks
        html = html.replace("\n", "<br>");

        return html;
    }
}
