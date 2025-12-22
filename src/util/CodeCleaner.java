package util;

public final class CodeCleaner {

    private CodeCleaner() {}

    //If the LLM code response contains unnecessary characters, the code file may not compile and be usable even if saved. 
    //Therefore, I implemented a function to remove them using the clean method.
    public static String clean(String raw) {
        if (raw == null) return "";

        String result = raw.trim();

        // ```cpp ... ```
        if (result.startsWith("```")) {
            // remove first line```cpp or ```
            int firstNewline = result.indexOf("\n");
            if (firstNewline != -1) {
                result = result.substring(firstNewline + 1);
            }

            // remove last line ``` 
            int lastFence = result.lastIndexOf("```");
            if (lastFence != -1) {
                result = result.substring(0, lastFence);
            }
        }

        // '''cpp ... '''
        if (result.startsWith("'''")) {
            int firstNewline = result.indexOf("\n");
            if (firstNewline != -1) {
                result = result.substring(firstNewline + 1);
            }

            int lastFence = result.lastIndexOf("'''");
            if (lastFence != -1) {
                result = result.substring(0, lastFence);
            }
        }

        return result.trim();
    }
}