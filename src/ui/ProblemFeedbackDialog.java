package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import util.MarkdownUtil;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("serial")
public class ProblemFeedbackDialog extends JDialog {

	private JEditorPane analysisPane;
    private JTextArea improvedCodeArea;
    private JButton saveButton;
    private String fileExtension;

    public ProblemFeedbackDialog(JFrame parent, String fileExtension) {
        super(parent, "Code Feedback", true);
        
        this.fileExtension = fileExtension;

        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        Image icon = Toolkit.getDefaultToolkit()
		        .getImage(getClass().getResource("/icon.png"));

		this.setIconImage(icon);
        
		// Shows the area to show feedback and the modified code to reflect it in a splitPane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        SwingUtilities.invokeLater(() -> {
        	add(splitPane, BorderLayout.CENTER);
            splitPane.setResizeWeight(0.5);
            
        });
        

        // On the left is the code feedback area, which neatly displays LLM's response in HTML format.
        analysisPane = new JEditorPane();
        analysisPane.setContentType("text/html");
        analysisPane.setEditable(false);

        JScrollPane analysisScroll = new JScrollPane(analysisPane);
        analysisScroll.setBorder(
            BorderFactory.createTitledBorder("Analysis & Advice")
        );
        analysisScroll.getVerticalScrollBar().setUnitIncrement(16);

        splitPane.setLeftComponent(analysisScroll);

        // On the right is the improved code. I designed it to look like a code IDE.
        improvedCodeArea = new JTextArea();
        improvedCodeArea.setLineWrap(false);
        

        JScrollPane codeScroll = new JScrollPane(improvedCodeArea);
        codeScroll.setBorder(
                BorderFactory.createTitledBorder("Improved Code")
        );
        //Code Editor Design
        Font codeFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);
  		improvedCodeArea.setFont(codeFont);
  		improvedCodeArea.setBackground(new Color(30, 30, 30));
  		improvedCodeArea.setForeground(new Color(220, 220, 220));
  		improvedCodeArea.setCaretColor(Color.WHITE);
  		improvedCodeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  		improvedCodeArea.setLineWrap(false);
  		improvedCodeArea.setWrapStyleWord(false);
  		codeScroll.getVerticalScrollBar().setUnitIncrement(16);
  		JTextArea lines = new JTextArea("1");
  		lines.setBackground(new Color(45, 45, 45));
  		lines.setForeground(Color.GRAY);
  		lines.setEditable(false);
  		lines.setFont(improvedCodeArea.getFont());

  		improvedCodeArea.getDocument().addDocumentListener(new DocumentListener() {
  		    public void insertUpdate(DocumentEvent e) { updateLines(); }
  		    public void removeUpdate(DocumentEvent e) { updateLines(); }
  		    public void changedUpdate(DocumentEvent e) { updateLines(); }

  		    private void updateLines() {
  		        int lineCount = improvedCodeArea.getLineCount();
  		        StringBuilder sb = new StringBuilder();
  		        for (int i = 1; i <= lineCount; i++) {
  		            sb.append(i).append("\n");
  		        }
  		        lines.setText(sb.toString());
  		    }
  		});

  		codeScroll.setRowHeaderView(lines);	

        splitPane.setRightComponent(codeScroll);

        // A button to save your improved code.
        saveButton = new JButton("Save Improved Code");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(saveButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // ActionListener
        initSaveButton();
    }

    private void initSaveButton() {
        saveButton.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Improved Code");
            
            String ext = fileExtension.substring(1); // ".java" → "java"
            chooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                    ext.toUpperCase() + " files (*." + ext + ")",
                    ext
                )
            );
            // default file name
            chooser.setSelectedFile(new File("improved_code."+ ext));

            int result = chooser.showSaveDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) return;

            File file = chooser.getSelectedFile();

            // To save same extension format
            if (!file.getName().toLowerCase().endsWith(fileExtension)) {
                file = new File(file.getAbsolutePath() + fileExtension);
            }

            try (BufferedWriter bw =
                         new BufferedWriter(new FileWriter(file))) {

                bw.write(improvedCodeArea.getText());

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to save the file.",
                        "Warning",
        	            JOptionPane.WARNING_MESSAGE
                );
            }
        });
    }
    
    //Design problem feedback area as html
    private String buildHtml(String content) {
        return "<html>\n"
             + "<head>\n"
             + "    <style>\n"
             + "        body {\n"
             + "            font-family: \"Segoe UI\", Arial, sans-serif;\n"
             + "            margin: 12px;\n"
             + "            background-color: #ffffff;\n"
             + "        }\n"
             + "        h2, h3, h4 {\n"
             + "            margin-top: 14px;\n"
             + "        }\n"
             + "        b {\n"
             + "            color: #222;\n"
             + "        }\n"
             + "    </style>\n"
             + "</head>\n"
             + "<body>\n"
             +      MarkdownUtil.markdownToHtml(content) + "\n"
             + "</body>\n"
             + "</html>\n";
    }
    // LLM returns a response in Markdown format, which is then converted to HTML format suitable for the GUI to avoid breaking the syntax.

    // Once ProblemFetchWorker is finished, it is displayed in the GUI as a callback.
    public void setAnalysisText(String text) {
    	analysisPane.setText(buildHtml(text));
        analysisPane.setCaretPosition(0);
    }

    public void setImprovedCode(String code) {
        improvedCodeArea.setText(code);
        improvedCodeArea.setCaretPosition(0);
    }
}
