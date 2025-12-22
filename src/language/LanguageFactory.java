package language;

// LanguageFactory is responsible for returning a language class 
// using the information received from the main frame when a language is input.
public class LanguageFactory {

    public static LanguageStrategy from(String language) {
        switch (language) {
            case "Java":
                return new JavaLanguage();
            case "C":
                return new CLanguage();
            case "C++":
                return new CppLanguage();
            case "Python":
            	return new PythonLanguage();
            default:
                throw new IllegalArgumentException("Unsupported language");
        }
    }
}
