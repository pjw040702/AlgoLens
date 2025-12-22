package language;

// In the language package, class inheritance and polymorphism were used 
// to configure different prompts for each language when calling LLM 
// to evaluate code and receive feedback.
// All languages ​​inherit from the LanguageStrategy interface.
// Extensions used for file IO are also defined in this package.
public interface LanguageStrategy {

    String getName();

    String getFileExtension();

    String analyzeCode(String code);

    String compileErrorHint(String code);

    String complexityHint(String code);
}