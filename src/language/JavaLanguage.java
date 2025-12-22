package language;

// In the language package, class inheritance and polymorphism were used 
// to configure different prompts for each language when calling LLM 
// to evaluate code and receive feedback.
// All languages ​​inherit from the LanguageStrategy interface.
// Extensions used for file IO are also defined in this package.
public class JavaLanguage implements LanguageStrategy {

    @Override
    public String getName() {
        return "Java";
    }

    @Override
    public String getFileExtension() {
        return ".java";
    }

    @Override
    public String analyzeCode(String code) {
        return
            "Analyze the following Java code focusing on:\n" +
            "- Correct class and method structure\n" +
            "- Proper use of loops and conditionals\n" +
            "- Object-oriented design where applicable\n" +
            "- Edge case handling and input constraints\n";
    }

    @Override
    public String compileErrorHint(String code) {
        return
            "Pay special attention to common Java issues:\n" +
            "- Public class name must match the file name\n" +
            "- Missing semicolons\n" +
            "- Incorrect method signatures (static, return type)\n" +
            "- Incorrect use of Scanner or BufferedReader\n";
    }

    @Override
    public String complexityHint(String code) {
        return
            "When analyzing time and space complexity:\n" +
            "- Focus on loop nesting depth\n" +
            "- Consider recursion depth if used\n" +
            "- Account for collections such as ArrayList, HashMap\n";
    }

}
