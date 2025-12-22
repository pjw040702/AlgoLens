package language;

// In the language package, class inheritance and polymorphism were used 
// to configure different prompts for each language when calling LLM 
// to evaluate code and receive feedback.
// All languages ​​inherit from the LanguageStrategy interface.
// Extensions used for file IO are also defined in this package.
public class PythonLanguage implements LanguageStrategy {

    @Override
    public String getName() {
        return "Python";
    }

    @Override
    public String getFileExtension() {
        return ".py";
    }

    @Override
    public String analyzeCode(String code) {
        return "Analyze the following Python code focusing on:\n"
             + "- Algorithmic correctness\n"
             + "- Proper use of built-in data structures\n"
             + "- Readability and Pythonic style\n"
             + "- Edge case handling\n";
    }

    @Override
    public String compileErrorHint(String code) {
        return "Pay special attention to common Python issues:\n"
             + "- Indentation errors\n"
             + "- NameError due to undefined variables\n"
             + "- Incorrect loop or condition syntax\n"
             + "- Off-by-one errors in range()\n";
    }

    @Override
    public String complexityHint(String code) {
        return "When analyzing time and space complexity:\n"
             + "- Consider hidden costs of list, dict, and set operations\n"
             + "- Be aware of recursion depth limits\n"
             + "- Account for Python's dynamic typing overhead\n";
    }

}