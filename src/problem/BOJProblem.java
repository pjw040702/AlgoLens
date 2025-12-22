package problem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BOJProblem extends WebProblem {
	// BOJ Problem crawls and retrieves information about problems from the BOJ site.
	public BOJProblem(int problemNumber) {
		super(problemNumber);
		this.url = "https://www.acmicpc.net/problem/" + problemNumber;
	}
	
	@Override
    public void fetch() throws Exception {	
		//Send headers for normal crawling
		Document document = Jsoup.connect(this.url)
			        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
			                  + "AppleWebKit/537.36 (KHTML, like Gecko) "
			                  + "Chrome/120.0.0.0 Safari/537.36")
			        .referrer("https://www.google.com/")
			        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
			        .header("Accept-Language", "en-US,en;q=0.5")
			        .timeout(8000)
			        .get();
			
		
		// Store details of issues that cannot be defined in the superclass here.
		// Crawling with CSS collector using JSoup.
		this.problemTitle = this.problemNumber + ". " + document.select("#problem_title").text();
		this.problem = document.select("#description").text().substring(2).trim();
		this.inputFormat = document.select("#problem_input").text();
		this.outputFormat = document.select("#problem_output").text();
			
		Elements input = document.select("pre[id^=sample-input-]");
		Elements output = document.select("pre[id^=sample-output-]");
			
		this.sampleInput = input.stream().map(Element::text).toArray(String[]::new);
		this.sampleOutput = output.stream().map(Element::text).toArray(String[]::new);
			
		
	}
	
}
