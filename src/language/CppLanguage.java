package language;

// In the language package, class inheritance and polymorphism were used 
// to configure different prompts for each language when calling LLM 
// to evaluate code and receive feedback.
// All languages ​​inherit from the LanguageStrategy interface.
// Extensions used for file IO are also defined in this package.
public class CppLanguage implements LanguageStrategy {

    @Override
    public String getName() {
        return "C++";
    }

    @Override
    public String getFileExtension() {
        return ".cpp";
    }

    @Override
    public String analyzeCode(String code) {
        return 
        "Analyze the following C++ code focusing on:\n"+
        "- Correct use of STL containers\n"+
        "- Loop and recursion correctness\n"+
        "- Memory safety and reference usage\n"+
        "- Edge case handling and performance\n";
        
    }

    @Override
    public String compileErrorHint(String code) {
        return 
        "Pay special attention to common C++ issues:\n"+
        "- Missing or incorrect header includes\n"+
        "- Incorrect namespace usage (std::)\n"+
        "- Type mismatches in templates\n"+
        "- Iterator misuse or invalid access\n";
    }

    @Override
    public String complexityHint(String code) {
        return 
        "When analyzing time and space complexity:\n"+
        "- Consider complexity of STL operations (vector, map, priority_queue)\n"+
        "- Check nested loops and recursion depth\n"+
        "- Account for dynamic memory allocation\n";
    }
}
