package problem;

//For problems entered directly by the user, the problem string is retrieved from the text window and fetched separately from WebProblem.
public class UserInputProblem extends Problem {

	private String directText;
	
	public String getDirectText() {
		return directText;
	}

	public UserInputProblem(String directText) {
		super();
		this.directText = directText;
	}

	@Override
	public void fetch() throws Exception {
		//We can't distinguish between title and input/output because we input the problem into the text input field and receive it as directText, 
		//but we set the string directly to pass it to LLM.
		this.problemTitle = "My own Problem!";
        this.problem = directText;
        this.inputFormat = "Please refer to the problem description for input format!";
        this.outputFormat = "Please refer to the problem description for output format!";
        this.sampleInput = new String[0];
        this.sampleOutput = new String[0];
	}

}
