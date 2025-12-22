package language;

// In the language package, class inheritance and polymorphism were used 
// to configure different prompts for each language when calling LLM 
// to evaluate code and receive feedback.
// All languages ​​inherit from the LanguageStrategy interface.
// Extensions used for file IO are also defined in this package.
public class CLanguage implements LanguageStrategy {

    @Override
    public String getName() {
        return "C";
    }

    @Override
    public String getFileExtension() {
        return ".c";
    }

    @Override
    public String analyzeCode(String code) {
        return 
        "Analyze the following C code focusing on:\n"+
        "- Correct use of pointers and memory\n"+
        "- Array bounds safety\n"+
        "- Loop correctness\n"+
        "- Edge cases related to input size\n";
    }

    @Override
    public String compileErrorHint(String code) {
        return
        "Pay special attention to common C issues:\n"+
        "- Missing semicolons\n"+
        "- Incorrect pointer dereferencing (*, &)\n"+
        "- Mismatched format specifiers in scanf/printf\n"+
        "- Missing header files (stdio.h, stdlib.h)\n";
    }

    @Override
    public String complexityHint(String code) {
        return 
        "When analyzing time and space complexity:\n"+
        "- Focus on loop nesting depth\n"+
        "- Consider manual memory usage (arrays, malloc)\n"+
        "- Assume no built-in high-level data structures\n";
    }
}