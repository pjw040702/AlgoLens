package ui;

import javax.swing.*;

import util.MarkdownUtil;

import java.awt.*;
@SuppressWarnings("serial")
public class ProblemExplainDialog extends JDialog {
    
    private JEditorPane editorPane;

    public ProblemExplainDialog(JFrame parent) {
        super(parent, "Problem Explanation", true);
        setSize(700, 500);
        setLocationRelativeTo(parent);
        
        Image icon = Toolkit.getDefaultToolkit()
		        .getImage(getClass().getResource("/icon.png"));

		this.setIconImage(icon);

		// Explain the problem clearly - Explain in HTML format in the order of time complexity - space complexity
        editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);
    }

    public void setContent(
            String description,
            String timeComplexity,
            String spaceComplexity
    ) {
        editorPane.setText(buildHtml(
                description,
                timeComplexity,
                spaceComplexity
        ));
        editorPane.setCaretPosition(0);
        // Once ProblemFetchWorker is finished, it is displayed in the GUI as a callback.
    }
    // Design
    private String buildHtml(String description, String time, String space) {
        return "<html>\n"
             + "<head>\n"
             + "    <style>\n"
             + "        body {\n"
             + "            font-family: \"Segoe UI\", Arial, sans-serif;\n"
             + "            margin: 16px;\n"
             + "            background-color: #ffffff;\n"
             + "        }\n\n"
             + "        h1 {\n"
             + "            font-size: 20px;\n"
             + "            margin-bottom: 12px;\n"
             + "            border-bottom: 2px solid #444;\n"
             + "            padding-bottom: 6px;\n"
             + "        }\n\n"
             + "        h2 {\n"
             + "            font-size: 16px;\n"
             + "            margin-top: 20px;\n"
             + "            margin-bottom: 8px;\n"
             + "            color: #333;\n"
             + "        }\n\n"
             + "        .box {\n"
             + "            background-color: #f7f7f7;\n"
             + "            border: 1px solid #ddd;\n"
             + "            padding: 10px;\n"
             + "            border-radius: 6px;\n"
             + "            line-height: 1.5;\n"
             + "        }\n\n"
             + "        .complexity {\n"
             + "            font-family: monospace;\n"
             + "            background-color: #eee;\n"
             + "            padding: 6px 8px;\n"
             + "            border-radius: 4px;\n"
             + "            display: inline-block;\n"
             + "        }\n"
             + "    </style>\n"
             + "</head>\n\n"
             + "<body>\n"
             + "    <h1>Problem Explanation</h1>\n\n"
             + "    <h2>Description</h2>\n"
             + "    <div class=\"box\">\n"
             +          MarkdownUtil.markdownToHtml(description) + "\n"
             + "    </div>\n\n"
             + "    <h2>Time Complexity</h2>\n"
             + "    <div class=\"complexity\">\n"
             +          MarkdownUtil.markdownToHtml(time) + "\n"
             + "    </div>\n\n"
             + "    <h2>Space Complexity</h2>\n"
             + "    <div class=\"complexity\">\n"
             +          MarkdownUtil.markdownToHtml(space) + "\n"
             + "    </div>\n"
             + "</body>\n"
             + "</html>\n";
        // LLM returns a response in Markdown format, which is then converted to HTML format suitable for the GUI to avoid breaking the syntax.
    }

}