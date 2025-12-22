package problem;

public abstract class WebProblem extends Problem {
	//WebProblem additionally takes a URL and a problem number, 
	//allowing you to access the URL via the problem number and retrieve the problem through crawling. 
	//This abstract class facilitates future support not only for BOJ but also for other PS sites.
	protected int problemNumber;
	protected String url;
    
	//getter
	public int getProblemNumber() {
		return problemNumber;
	}
	public String getUrl() {
		return url;
	}
	
	public WebProblem(int problemNumber) {
		super();
		this.problemNumber = problemNumber;
	}
}
