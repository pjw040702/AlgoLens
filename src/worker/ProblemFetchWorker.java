package worker;

import javax.swing.SwingWorker;

import problem.*;
import ui.MainFrame.Mode;

import java.util.function.Consumer;

// Set information about the current issue in the main frame and update the UI.
public class ProblemFetchWorker extends SwingWorker<Problem, Void> {

    private Mode type;
    private int problemNumber;
    private String directText;
    private Consumer<Problem> callback;
    private Consumer<String> errorCallback;

    public ProblemFetchWorker(Mode type, int problemNumber, String directText,
                              Consumer<Problem> callback, Consumer<String> errorCallback) {
        this.type = type;
        this.problemNumber = problemNumber;
        this.directText = directText;
        this.callback = callback;
        this.errorCallback = errorCallback;
    }

    @Override
    protected Problem doInBackground() throws Exception {
        Problem problem = ProblemFactory.create(type, problemNumber, directText);
        problem.fetch(); // to fetch information about the current problem, which may result in an exception due to crawling, etc.
        return problem;
    }

    @Override
    protected void done() {
        try {
            callback.accept(get()); // Update UI or get current problem info
        } catch (Exception e) {
        	e.printStackTrace();
        	errorCallback.accept("Invalid problem number!");
        }
    }
}
