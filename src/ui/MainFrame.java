package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JRadioButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;

import language.LanguageFactory;
import language.LanguageStrategy;
import problem.Problem;
import util.IntegerFilter;
import util.MarkdownUtil;
import worker.ProblemExplainWorker;
import worker.ProblemFeedbackWorker;
import worker.ProblemFetchWorker;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	// UI
	private JFrame frmAlgolens;
	private JMenuBar menuBar;
	private JMenu helpMenu;
	private JMenuItem helpMenuItem;
	private JSplitPane initialSplitPane;
	private JPanel problemPanel;
	private JPanel codePanel;
	private JRadioButton userInputRadioButton;
	private JTextField problemNumberField;
	private JPanel problemSelectPanel;
	private JPanel problemExplainPanel;
	private JPanel problemIOPanel;
	private JSplitPane problemIOSplitPane;
	private JPanel problemInputPanel;
	private JPanel problemOutputPanel;
	private JLabel problemNumberLabel;
	private JPanel inputModePanel;
	private JPanel problemNumberPanel;
	private JScrollPane problemExplainScroll;
	private JEditorPane problemExplainPane;
	private JPanel sampleInputTextPanel;
	private JLabel sampleInputText;
	private JPanel sampleOutputTextPanel;
	private JLabel sampleOutputText;
	private JPanel problemInputTextPanel;
	private JScrollPane problemInputTextScroll;
	private JTextArea problemInputTextArea;
	private JPanel problemOutputTextPanel;
	private JScrollPane problemOutputTextScroll;
	private JTextArea problemOutputTextArea;
	private JMenu optionMenu;
	private JPanel problemBottomContainer;
	private JButton problemButton;
	private JPanel languageSelectPanel;
	private JLabel languageSelectText;
	private JComboBox languageSelectComboBox;
	private JPanel codeFileInputPanel;
	private JButton codeFileInputButton;
	private JPanel codeVisualizePanel;
	private JScrollPane codeVisualizeScroll;
	private JTextArea codeVisualizeArea;
	private JPanel codeButtonPanel;
	private JButton codeButton;
	private JMenuItem languageMenuItem;
	private JLabel codeFileNameLabel;
	private JButton problemNumberEnterButton;
	private JRadioButton enterProblemNumberRadioButton;
	private JPanel problemLRPanel;
	private JPanel problemButtonPanel;
	private JButton problemLeftButton;
	private JButton problemRightButton;
	
	// Setting information about sample input and output
	private int currentSampleIndex = 0;
	private String[] sampleInputs;
	private String[] sampleOutputs;
	
	//Variables for current mode and problem
	private Problem currentProblem;
	private Mode currentMode;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frmAlgolens.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Currently, two modes are supported:
	public enum Mode {
	    BOJ,
	    USER_INPUT
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAlgolens = new JFrame();
		frmAlgolens.setTitle("AlgoLens");
		frmAlgolens.setBounds(100, 100, 1000, 700);
		frmAlgolens.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menuBar = new JMenuBar();
		frmAlgolens.setJMenuBar(menuBar);

		helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);

		helpMenuItem = new JMenuItem("Help");
		helpMenu.add(helpMenuItem);

		initialSplitPane = new JSplitPane();
		frmAlgolens.getContentPane().add(initialSplitPane, BorderLayout.CENTER);
		initialSplitPane.setResizeWeight(0.5); 

		// left: problem panel
		problemPanel = new JPanel();
		initialSplitPane.setLeftComponent(problemPanel);
		problemPanel.setLayout(new BorderLayout(0, 0));

		JPanel problemTopContainer = new JPanel();
		problemTopContainer.setLayout(new BoxLayout(problemTopContainer, BoxLayout.Y_AXIS));
		problemPanel.add(problemTopContainer, BorderLayout.NORTH);

		// Separate from the problem number input field
		JSplitPane problemCenterSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		problemCenterSplit.setResizeWeight(0.5);
		problemPanel.add(problemCenterSplit, BorderLayout.CENTER);

		// Input problem
		problemSelectPanel = new JPanel();
		problemTopContainer.add(problemSelectPanel);
		problemSelectPanel.setLayout(new BoxLayout(problemSelectPanel, BoxLayout.Y_AXIS));

		inputModePanel = new JPanel();
		problemSelectPanel.add(inputModePanel);
		inputModePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		userInputRadioButton = new JRadioButton("User input");
		enterProblemNumberRadioButton = new JRadioButton("BOJ");
		inputModePanel.add(enterProblemNumberRadioButton);
		inputModePanel.add(userInputRadioButton);

		// Select current mode
		ButtonGroup group = new ButtonGroup();
		group.add(userInputRadioButton);
		group.add(enterProblemNumberRadioButton);

		problemNumberPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) problemNumberPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		problemSelectPanel.add(problemNumberPanel);

		problemNumberLabel = new JLabel("Enter your BOJ problem number: ");
		problemNumberPanel.add(problemNumberLabel);

		problemNumberField = new JTextField();
		problemNumberPanel.add(problemNumberField);
		problemNumberField.setColumns(10);
		((AbstractDocument) problemNumberField.getDocument()).setDocumentFilter(new IntegerFilter()); // Only receive unsigned integer

		problemNumberEnterButton = new JButton("Enter");
		problemNumberPanel.add(problemNumberEnterButton);

		// problem explain
		problemExplainPanel = new JPanel();
		problemCenterSplit.setTopComponent(problemExplainPanel);
		problemExplainPanel.setLayout(new BorderLayout(0, 0));

		problemExplainPane = new JEditorPane();
		problemExplainPane.setContentType("text/html");
		problemExplainPane.setEditable(false);

		problemExplainScroll = new JScrollPane(problemExplainPane);
		problemExplainScroll.getVerticalScrollBar().setUnitIncrement(16);

		problemExplainPanel.add(problemExplainScroll, BorderLayout.CENTER);


		// View sample IO
		problemIOPanel = new JPanel();
		problemCenterSplit.setBottomComponent(problemIOPanel);
		problemIOPanel.setLayout(new BorderLayout(0, 0));

		problemIOSplitPane = new JSplitPane();
		problemIOPanel.add(problemIOSplitPane);
		problemIOSplitPane.setDividerLocation(0.5); 
		problemIOSplitPane.setResizeWeight(0.5);    

		problemInputPanel = new JPanel();
		problemIOSplitPane.setLeftComponent(problemInputPanel);
		problemInputPanel.setLayout(new BorderLayout(0, 0));

		problemInputTextPanel = new JPanel();
		problemInputPanel.add(problemInputTextPanel, BorderLayout.CENTER);
		problemInputTextPanel.setLayout(new BorderLayout(0, 0));

		problemInputTextScroll = new JScrollPane();
		problemInputTextPanel.add(problemInputTextScroll);

		problemInputTextArea = new JTextArea();
		problemInputTextArea.setColumns(10);
		problemInputTextArea.setLineWrap(true);
		problemInputTextArea.setWrapStyleWord(true);
		problemInputTextScroll.setViewportView(problemInputTextArea);

		sampleInputTextPanel = new JPanel();
		problemInputPanel.add(sampleInputTextPanel, BorderLayout.NORTH);

		sampleInputText = new JLabel("Sample Input");
		sampleInputTextPanel.add(sampleInputText);

		problemOutputPanel = new JPanel();
		problemIOSplitPane.setRightComponent(problemOutputPanel);
		problemOutputPanel.setLayout(new BorderLayout(0, 0));

		problemOutputTextPanel = new JPanel();
		problemOutputPanel.add(problemOutputTextPanel, BorderLayout.CENTER);
		problemOutputTextPanel.setLayout(new BorderLayout(0, 0));

		problemOutputTextScroll = new JScrollPane();
		problemOutputTextPanel.add(problemOutputTextScroll);

		problemOutputTextArea = new JTextArea();
		problemOutputTextArea.setColumns(10);
		problemOutputTextArea.setLineWrap(true);
		problemOutputTextArea.setWrapStyleWord(true);
		problemOutputTextScroll.setViewportView(problemOutputTextArea);

		sampleOutputTextPanel = new JPanel();
		problemOutputPanel.add(sampleOutputTextPanel, BorderLayout.NORTH);

		sampleOutputText = new JLabel("Sample Output");
		sampleOutputTextPanel.add(sampleOutputText);

		// button
		problemBottomContainer = new JPanel();
		problemPanel.add(problemBottomContainer, BorderLayout.SOUTH); // SOUTH에 배치하여 고정
		problemBottomContainer.setLayout(new BoxLayout(problemBottomContainer, BoxLayout.Y_AXIS));
		
		problemLRPanel = new JPanel();
		problemBottomContainer.add(problemLRPanel);
		
		problemLeftButton = new JButton("<"); // move left sample input 
		problemLRPanel.add(problemLeftButton);
		
		problemRightButton = new JButton(">"); // move right sample input
		problemLRPanel.add(problemRightButton);
		
		problemButtonPanel = new JPanel();
		problemBottomContainer.add(problemButtonPanel);

		problemButton = new JButton("Get an explanation of the problem!");
		problemButtonPanel.add(problemButton);

		// right: code panel
		codePanel = new JPanel();
		initialSplitPane.setRightComponent(codePanel);
		codePanel.setLayout(new BorderLayout(0, 0));

		// select language
		JPanel codeTopContainer = new JPanel();
		codeTopContainer.setLayout(new BoxLayout(codeTopContainer, BoxLayout.Y_AXIS));
		codePanel.add(codeTopContainer, BorderLayout.NORTH);

		languageSelectPanel = new JPanel();
		FlowLayout fl_languageSelectPanel = (FlowLayout) languageSelectPanel.getLayout();
		codeTopContainer.add(languageSelectPanel); // codeTopContainer

		languageSelectText = new JLabel("Select your programming language !");
		languageSelectPanel.add(languageSelectText);
		String[] languages = { "Java", "Python", "C", "C++" }; // Support language
		languageSelectComboBox = new JComboBox(languages);
		languageSelectPanel.add(languageSelectComboBox);

		codeFileInputPanel = new JPanel();
		codeTopContainer.add(codeFileInputPanel); // codeTopContainer

		codeFileInputButton = new JButton("Enter code file");
		codeFileInputPanel.add(codeFileInputButton);

		codeFileNameLabel = new JLabel("");
		codeFileInputPanel.add(codeFileNameLabel);
		codeFileNameLabel.setVisible(false);

		// Code editor
		codeVisualizePanel = new JPanel();
		codePanel.add(codeVisualizePanel, BorderLayout.CENTER);
		codeVisualizePanel.setLayout(new BorderLayout(0, 0));

		codeVisualizeScroll = new JScrollPane();
		codeVisualizePanel.add(codeVisualizeScroll);

		codeVisualizeArea = new JTextArea();
		codeVisualizeArea.setColumns(10);
		codeVisualizeArea.setLineWrap(true);
		codeVisualizeArea.setWrapStyleWord(true);
		codeVisualizeScroll.setViewportView(codeVisualizeArea);
		
		//Code Editor Design looks like IDE
		Font codeFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);
		codeVisualizeArea.setFont(codeFont);
		codeVisualizeArea.setBackground(new Color(30, 30, 30));
		codeVisualizeArea.setForeground(new Color(220, 220, 220));
		codeVisualizeArea.setCaretColor(Color.WHITE);
		codeVisualizeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		codeVisualizeArea.setLineWrap(false);
		codeVisualizeArea.setWrapStyleWord(false);
		codeVisualizeScroll.getVerticalScrollBar().setUnitIncrement(16);
		JTextArea lines = new JTextArea("1");
		lines.setBackground(new Color(45, 45, 45));
		lines.setForeground(Color.GRAY);
		lines.setEditable(false);
		lines.setFont(codeVisualizeArea.getFont());

		codeVisualizeArea.getDocument().addDocumentListener(new DocumentListener() {
		    public void insertUpdate(DocumentEvent e) { updateLines(); }
		    public void removeUpdate(DocumentEvent e) { updateLines(); }
		    public void changedUpdate(DocumentEvent e) { updateLines(); }

		    private void updateLines() {
		        int lineCount = codeVisualizeArea.getLineCount();
		        StringBuilder sb = new StringBuilder();
		        for (int i = 1; i <= lineCount; i++) {
		            sb.append(i).append("\n");
		        }
		        lines.setText(sb.toString());
		    }
		});

		codeVisualizeScroll.setRowHeaderView(lines);

		// Button to get code feedback
		codeButtonPanel = new JPanel();
		codePanel.add(codeButtonPanel, BorderLayout.SOUTH);

		codeButton = new JButton("Get a feedback of the code!");
		codeButtonPanel.add(codeButton);

		// Finalize, icon and visible
		Image icon = Toolkit.getDefaultToolkit()
		        .getImage(getClass().getResource("/icon.png"));

		frmAlgolens.setIconImage(icon);
		frmAlgolens.setVisible(true);

		// Divider
		initialSplitPane.setDividerLocation(0.5); 
		problemIOSplitPane.setDividerLocation(0.5);
		problemCenterSplit.setDividerLocation(0.5);
		
		// initial setting
		
		enterProblemNumberRadioButton.setSelected(true);
		problemNumberLabel.setEnabled(true);
		problemNumberField.setEnabled(true);
		problemNumberEnterButton.setEnabled(true);
		
		problemExplainPane.setEditable(false);
		problemInputTextArea.setEditable(false);
		problemOutputTextArea.setEditable(false);
		
		currentMode = Mode.BOJ; // initial mode
		
		// Change UI by selected mode
		userInputRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				problemNumberLabel.setEnabled(false);
				problemNumberField.setEnabled(false);
				problemNumberEnterButton.setEnabled(false); // cannot input problem number
				
				problemExplainPane.setContentType("text"); // NOT HTML
				problemExplainPane.setEditable(true);
			
				problemLeftButton.setEnabled(false);
				problemRightButton.setEnabled(false);
				
				// to prevent error
				currentProblem = null;
				problemExplainPane.setText("");
				problemInputTextArea.setText("");
				problemOutputTextArea.setText("");
				sampleInputs = null;
				sampleOutputs = null;
				
				currentMode = Mode.USER_INPUT;
			}
		});
		
		enterProblemNumberRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				problemNumberLabel.setEnabled(true);
				problemNumberField.setEnabled(true);
				problemNumberEnterButton.setEnabled(true);
				problemExplainPane.setContentType("text/html");
				
				problemExplainPane.setEditable(false);
				
				problemLeftButton.setEnabled(true);
				problemRightButton.setEnabled(true);
				
				// to prevent error
				currentProblem = null;
				problemExplainPane.setText("");
				problemInputTextArea.setText("");
				problemOutputTextArea.setText("");
				sampleInputs = null;
				sampleOutputs = null;
				
				currentMode = Mode.BOJ;
			}
		});
		
		// when mode is not USER_INPUT, we can add problem number.
		// this button will fetch problem and update TextArea by call ProblemFetchWorker
		problemNumberEnterButton.addActionListener(e -> {
			
			String directText = null; // not use
			if(problemNumberField.getText().length() <= 0) {
				showWarning("Invalid problem number!");
				return;
			}
			int number = Integer.parseInt(problemNumberField.getText());
			
			
			ProblemFetchWorker worker = new ProblemFetchWorker(currentMode, number,directText,this::updateUI, this::showWarning);

			worker.execute();
		    
		});
		
		//To show sample input and output
		problemLeftButton.addActionListener(e -> {
		    if (sampleInputs != null && sampleInputs.length > 0) {
		        currentSampleIndex--;
		        if (currentSampleIndex < 0) {
		            currentSampleIndex = sampleInputs.length - 1;  // go end
		        }
		        updateSampleIO();
		    }
		});
		problemRightButton.addActionListener(e -> {
		    if (sampleInputs != null && sampleInputs.length > 0) {
		        currentSampleIndex++;
		        if (currentSampleIndex >= sampleInputs.length) {
		            currentSampleIndex = 0;  // go init
		        }
		        updateSampleIO();
		    }
		});
		//this button will call ProblemExplainWorker, and show UI
		problemButton.addActionListener(e -> {
			
		    // If mode is USER_INPUT, problem is not fetched, so we must fetch here
			if(currentMode == Mode.USER_INPUT) {
				String directText = problemExplainPane.getText().trim();
				if (directText.isEmpty()) { // Nothing entered
			        showWarning("Please enter a problem description!");
			        return;
			    }
				int number = 0; // callback is only set current problem
				ProblemFetchWorker worker = new ProblemFetchWorker(currentMode, number, directText,  problem -> {
																		                    currentProblem = problem;
																		                    runExplainWorker();
																		                }, this::showWarning);
				worker.execute();
				
				return;
			}
			
		    if (currentProblem == null) {
		        showWarning("Please load or enter a problem first.");
		        return;
		    }
		    // not USER_INPUT mode
		    runExplainWorker();
				
		});
		// Select code file
		codeFileInputButton.addActionListener(e -> {
			
			// get language from combo box
			String selectedLanguage =
		            (String) languageSelectComboBox.getSelectedItem();

		    LanguageStrategy language;
		    try {
		        language = LanguageFactory.from(selectedLanguage);
		    } catch (Exception ex) {
		        showWarning("Unsupported language selected.");
		        return;
		    }
		    //get appropriate extension by LanguageStrategy class
		    String extension = language.getFileExtension();
		    
		    //choose file
		    JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
		        @Override
		        public boolean accept(File f) {
		            return f.isDirectory() || f.getName().toLowerCase().endsWith(extension);
		        }

		        @Override
		        public String getDescription() {
		            return extension + " files (*" + extension + ")";
		        }
		    });

		    
		    int result = fileChooser.showOpenDialog(frmAlgolens);

		    if (result == JFileChooser.APPROVE_OPTION) {
		        File file = fileChooser.getSelectedFile();

		        try {
		            String content = readFile(file);
		            codeVisualizeArea.setText(content);
		            codeVisualizeArea.setCaretPosition(0);
		        } catch (IOException ex) {
		            showWarning("Failed to read the file.");
		        }
		    }
		});
		//get code feedback by calling ProblemFeedbackWorker and update UI
		codeButton.addActionListener(e -> {
		
			// If mode is USER_INPUT, problem is not fetched, so we must fetch here
			if(currentMode == Mode.USER_INPUT) {
				String directText = problemExplainPane.getText().trim();
				if (directText.isEmpty()) { // Nothing entered
			        showWarning("Please enter a problem description!");
			        return;
			    }
				int number = 0; // callback is only set current problem
				ProblemFetchWorker worker = new ProblemFetchWorker(currentMode, number, directText,  problem -> {
																		                    currentProblem = problem;
																		                    runFeedbackWorker();
																		                }, this::showWarning);
				worker.execute();
				
				return;
			}			
						
		    // no fetched problem
		    if (currentProblem == null) {
		        showWarning("Please load or enter a problem first.");
		        return;
		    }
		    runFeedbackWorker();

		});
		// Help
		helpMenuItem.addActionListener(e -> {
		    JOptionPane.showMessageDialog(
		        frmAlgolens,
		        "📘 AlgoLens – User Guide\n\n"
		      + "1. Problem Input\n"
		      + "- BOJ Mode:\n"
		      + "  Enter a problem number and click Enter.\n"
		      + "- User Input Mode:\n"
		      + "  Write the problem description directly in the text area.\n\n"
		      + "2. Problem Explanation\n"
		      + "- Click \"Get an explanation of the problem!\"\n"
		      + "- The system will analyze the problem and show:\n"
		      + "  • Problem description\n"
		      + "  • Time complexity\n"
		      + "  • Space complexity\n\n"
		      + "3. Code Feedback\n"
		      + "- Select the programming language.\n"
		      + "- Load a source code file.\n"
		      + "- Click \"Get a feedback of the code!\"\n"
		      + "- You will receive:\n"
		      + "  • Code analysis\n"
		      + "  • Suggestions\n"
		      + "  • An improved version of the code\n\n"
		      + "Notes:\n"
		      + "- API requests may take several seconds.\n"
		      + "- Please make sure the selected language matches your code.\n",
		        "Help",
		        JOptionPane.INFORMATION_MESSAGE
		    );
		});


	}
	// Showing problem design
	private String buildProblemHtml(String title, String problem, String input, String output) {
	    return String.format(
	        "<html>\n"
	      + "<head>\n"
	      + "    <style>\n"
	      + "        body {\n"
	      + "            font-family: \"Segoe UI\", Arial, sans-serif;\n"
	      + "            margin: 14px;\n"
	      + "            background-color: #ffffff;\n"
	      + "            line-height: 1.5;\n"
	      + "        }\n"
	      + "        h1 {\n"
	      + "            font-size: 18px;\n"
	      + "            border-bottom: 2px solid #444;\n"
	      + "            padding-bottom: 4px;\n"
	      + "        }\n"
	      + "        h2 {\n"
	      + "            font-size: 15px;\n"
	      + "            margin-top: 18px;\n"
	      + "        }\n"
	      + "        .box {\n"
	      + "            background-color: #f7f7f7;\n"
	      + "            border: 1px solid #ddd;\n"
	      + "            padding: 10px;\n"
	      + "            border-radius: 6px;\n"
	      + "            white-space: pre-wrap;\n"
	      + "        }\n"
	      + "    </style>\n"
	      + "</head>\n"
	      + "<body>\n"
	      + "    <h1>%s</h1>\n\n"
	      + "    <h2>Problem</h2>\n"
	      + "    <div class=\"box\">%s</div>\n\n"
	      + "    <h2>Input</h2>\n"
	      + "    <div class=\"box\">%s</div>\n\n"
	      + "    <h2>Output</h2>\n"
	      + "    <div class=\"box\">%s</div>\n"
	      + "</body>\n"
	      + "</html>\n",
	        MarkdownUtil.markdownToHtml(title),
	        MarkdownUtil.markdownToHtml(problem),
	        MarkdownUtil.markdownToHtml(input),
	        MarkdownUtil.markdownToHtml(output)
	    );
	
	}
	// Separate methods for branching functionality when USER_INPUT is present and when it is not.
	private void runExplainWorker() {
		ProblemExplainDialog dialog =
	            new ProblemExplainDialog(this);
		
		LoadingDialog loading =
	            new LoadingDialog(frmAlgolens, "Analyzing problem...");
		loading.setVisible(true);
		
		ProblemExplainWorker worker; // When receiving an API response, set an appropriate value in the dialog and display it as a callback.
		worker = new ProblemExplainWorker(
			    currentProblem,
			    w -> {
	                dialog.setContent(
	                    w.getDescribe(),
	                    w.getTimeComplexity(),
	                    w.getSpaceComplexity()
	                );
	                dialog.setVisible(true);
	            },
			    this::showWarning, loading // loading dialog should be closed in SwingWorker done()
			);

        worker.execute();
        return;
	}
	// Separate methods for branching functionality when USER_INPUT is present and when it is not.
	private void runFeedbackWorker() {
		// no entered code
	    String code = codeVisualizeArea.getText();
	    if (code == null || code.trim().isEmpty()) {
	        showWarning("Please provide source code.");
	        return;
	    }

	    // get language from combo box
	    String selectedLanguage =
	            (String) languageSelectComboBox.getSelectedItem();

	    LanguageStrategy language;
	    try {
	        language = LanguageFactory.from(selectedLanguage);
	    } catch (Exception ex) {
	        showWarning("Unsupported language selected.");
	        return;
	    }

	    // feedback dialog
	    LanguageStrategy lang =
	            LanguageFactory.from(selectedLanguage);

	    ProblemFeedbackDialog feedbackDialog =
	            new ProblemFeedbackDialog(
	                frmAlgolens,
	                lang.getFileExtension()
	            );
	    LoadingDialog loading =
		        new LoadingDialog(frmAlgolens, "Analyzing code...");
		loading.setVisible(true);
	    
	    // When receiving an API response, set an appropriate value in the dialog and display it as a callback.
	    ProblemFeedbackWorker worker =
	            new ProblemFeedbackWorker(
	                currentProblem,
	                language,
	                code,
	                w -> {
	                    // success
	                    feedbackDialog.setAnalysisText(
	                            w.getAnalysisText()
	                    );
	                    feedbackDialog.setImprovedCode(
	                            w.getImprovedCode()
	                    );
	                    feedbackDialog.setVisible(true);
	                },
	                this::showWarning, loading // loading dialog should be closed in SwingWorker done()
	            );
	          
	    
	    worker.execute();
	    return;
	}
	// Warning Dialog
	private void showWarning(String message) {
	    JOptionPane.showMessageDialog(
	            frmAlgolens,
	            message,
	            "Warning",
	            JOptionPane.WARNING_MESSAGE
	    );
	}
	// Update UI after loading issue
	private void updateUI(Problem problem) {

	    this.sampleInputs = problem.getSampleInput();
	    this.sampleOutputs = problem.getSampleOutput();
	    this.currentSampleIndex = 0;
	    this.currentProblem = problem;
	    
	    problemExplainPane.setText(
	    	    buildProblemHtml(
	    	        currentProblem.getProblemTitle(),
	    	        currentProblem.getProblem(),
	    	        currentProblem.getInputFormat(),
	    	        currentProblem.getOutputFormat()
	    	    )
	    	);
	    problemExplainPane.setCaretPosition(0);

	    updateSampleIO(); // Sample IO
	}
	// The sample IO is just for displaying in the UI without calling LLM.
	private void updateSampleIO() {
	    if (sampleInputs == null || sampleInputs.length == 0) {
	        problemInputTextArea.setText("");
	    } else {
	        problemInputTextArea.setText(sampleInputs[currentSampleIndex]);
	    }

	    if (sampleOutputs == null || sampleOutputs.length == 0) {
	        problemOutputTextArea.setText("");
	    } else {
	        problemOutputTextArea.setText(sampleOutputs[currentSampleIndex]);
	    }
	}
	
	private String readFile(File file) throws IOException {
	    StringBuilder sb = new StringBuilder();

	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            sb.append(line).append("\n");
	        }
	    }

	    return sb.toString();
	}

}
