package problem;


// The Problem class is the parent class of all problem types, and has a subclass UserInputProblem, 
// and another subclass WebProblem, which has BOJProblem as a subclass.

public abstract class Problem {
	// Member variables that must be present in all algorithmic problems
    protected String problemTitle;
    protected String problem;
    
    protected String[] sampleInput;
    protected String[] sampleOutput;
    protected String inputFormat;
    protected String outputFormat;
	
    //getter
	public String getInputFormat() {
		return inputFormat;
	}
	public String getOutputFormat() {
		return outputFormat;
	}
    public String getProblemTitle() { return problemTitle; }
    public String getProblem() { return problem; }
    public String[] getSampleInput() { return sampleInput; }
    public String[] getSampleOutput() { return sampleOutput; }
    
    public String toString() {
    	return "Title: " + this.getProblemTitle() + "\nProblem: " + this.getProblem()+ "\nInput: " + this.getInputFormat()+ "\nOutput: " + this.getOutputFormat() + "\n";
		// Converting the problem to a string when calling LLM
    }
    
    //A method used by ProblemFetchWorker to fetch information about the current problem, which may result in an exception due to crawling, etc.
    public abstract void fetch() throws Exception;
}