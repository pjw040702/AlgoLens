package worker;

import java.util.function.Consumer;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import language.LanguageStrategy;
import problem.Problem;
import ui.LoadingDialog;
import util.ApiUtil;
import util.CodeCleaner;
// Call LLM to get the code bid back and the modified code. When finished, open the GUI with the callback.
public class ProblemFeedbackWorker extends SwingWorker<Void, Void> {
	//To input
	private Problem problem;
	private final LanguageStrategy language;
    private final String code;
	private Consumer<ProblemFeedbackWorker> callback;
    private Consumer<String> errorCallback;
    private LoadingDialog loading;
    //To get
    private String analysisText;
    private String improvedCode;
    
    
    public ProblemFeedbackWorker(
            Problem problem,
            LanguageStrategy language,
            String code,
            Consumer<ProblemFeedbackWorker> callback,
            Consumer<String> errorCallback, LoadingDialog loading) {

        this.problem = problem;
        this.language = language;
        this.code = code;
        this.callback = callback;
        this.errorCallback = errorCallback;
        this.loading = loading;
    }
    //getter and setter
    public String getAnalysisText() {
		return analysisText;
	}

	public void setAnalysisText(String analysisText) {
		this.analysisText = analysisText;
	}

	public String getImprovedCode() {
		return improvedCode;
	}

	public void setImprovedCode(String improvedCode) {
		this.improvedCode = improvedCode;
	}

	@Override
    protected Void doInBackground() throws Exception {

        // Building prompt
		
        StringBuilder prompt = new StringBuilder();

        prompt.append("Problem Description:\n")
              .append(problem)
              .append("\n\n");

        prompt.append("Language: ")
              .append(language.getName())
              .append("\n\n");

        prompt.append(language.analyzeCode(code))
              .append("\n");

        prompt.append(language.compileErrorHint(code))
              .append("\n");

        prompt.append(language.complexityHint(code))
              .append("\n\n");

        prompt.append("User Code:\n")
              .append(code);
        
        prompt.append(
        	    "Analyze the code and return the result strictly in the following format.\n\n"
        	  + "[ANALYSIS]\n"
        	  + "(Please analyze the code in the above format, but do not write the code.)\n\n"
        	  + "[IMPROVED_CODE]\n"
        	  + "(Improved version of the code using the same programming language. "
        	  + "Please make minimal changes to variable names, function/method names, and logic.)\n\n"
        	  + "Do not write anything outside this format.\n"
        	);

        
        String response = ApiUtil.get(prompt.toString());
        // Here, we call and parse LLM only once to strengthen the matching of feedback and modified code.
        String analysisTag = "[ANALYSIS]";
        String codeTag = "[IMPROVED_CODE]";

        int analysisIndex = response.indexOf(analysisTag);
        int codeIndex = response.indexOf(codeTag);

        if (analysisIndex == -1 || codeIndex == -1) {
            throw new IllegalStateException("Invalid API response format");
        }

        // Feedback
        analysisText = response.substring(
                analysisIndex + analysisTag.length(),
                codeIndex
        ).trim();

        // Improved Code
        improvedCode = response.substring(
                codeIndex + codeTag.length()
        ).trim();
        improvedCode = CodeCleaner.clean(improvedCode);
        //If the LLM code response contains unnecessary characters, the code file may not compile and be usable even if saved. 
        //Therefore, I implemented a function to remove them using the clean method.
        

        return null;
    }

    @Override
    protected void done() {
        try {
        	SwingUtilities.invokeLater(() -> loading.dispose()); // close loading dialog
        	get(); // open GUI
            callback.accept(this);
        } catch (Exception e) {
        	SwingUtilities.invokeLater(() -> loading.dispose()); // close loading dialog
            errorCallback.accept("An error occurred while calling the API.");
        }
    }

}
