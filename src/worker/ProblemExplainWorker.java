package worker;

import java.util.function.Consumer;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import problem.Problem;
import ui.LoadingDialog;
import util.ApiUtil;

// Call LLM to obtain the problem description, time complexity, and space complexity. Once complete, open the GUI using a callback.
public class ProblemExplainWorker extends SwingWorker<Void, Void> {
	// To input
	private Problem problem;
	private Consumer<ProblemExplainWorker> callback;
    private Consumer<String> errorCallback;
    private LoadingDialog loading;
    
    // To get
    private String timeComplexity;
    private String spaceComplexity;
    private String describe;
    
    
	public ProblemExplainWorker(Problem problem, Consumer<ProblemExplainWorker> callback, Consumer<String> errorCallback, LoadingDialog loading) {
		super();
		
		this.problem = problem;
		this.callback = callback;
		this.errorCallback = errorCallback;
		this.loading = loading;
	}
    // getter and setter
	public String getTimeComplexity() {
		return timeComplexity;
	}
	public void setTimeComplexity(String timeComplexity) {
		this.timeComplexity = timeComplexity;
	}
	public String getSpaceComplexity() {
		return spaceComplexity;
	}
	public void setSpaceComplexity(String spaceComplexity) {
		this.spaceComplexity = spaceComplexity;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		// Building API prompt
		
		this.timeComplexity = ApiUtil.get("Please find the time complexity for the following algorithm problem. \n" +
		"Please express it in just the form of O(N)." + "\n" + problem);
		this.spaceComplexity = ApiUtil.get("Please find the space complexity for the following algorithm problem. \n" +
				"Please express it in just the form of O(N)." + "\n" + problem);
		this.describe = ApiUtil.get("Please explain your solution to the following algorithmic problem using the following format.\r\n"
				+ "\r\n"
				+ "Output Format:\r\n"
				+ "1. Summarize the core requirements of the problem\r\n"
				+ "2. Meaning of the input and output\r\n"
				+ "3. Key idea for solving this problem\r\n"
				+ "4. Algorithm for solving this problem and why it should be used\r\n"
				+ "5. Describe the step-by-step solution strategy in detail\r\n"
				+ "\r\n"
				+ "Restrictions:\r\n"
				+ "1. Describe without code.\r\n"
				+ "2. Omit unnecessary introduction." + "\r\n" + problem);
		return null;
	}
	@Override
    protected void done() {
        try {
        	SwingUtilities.invokeLater(() -> loading.dispose()); // Close loading dialog
            get();
            callback.accept(this); // open GUI
        } catch (Exception e) {
        	SwingUtilities.invokeLater(() -> loading.dispose()); // Close loading dialog
            errorCallback.accept("An error occurred while calling the API.");
        } 
    }
    
	
}
