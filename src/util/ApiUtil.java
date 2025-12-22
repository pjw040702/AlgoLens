package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiUtil {
	private static final String API_KEY = System.getProperty("LLM_API_KEY");
    private static final String MODEL_NAME = "gemini-3-flash-preview";
    
	private ApiUtil() { // prevent constructor
		
	}
	// Using the Gemini API, we take a prompt as input, convert the value to JSON format, and convert the response to a String.
	public static String get(String prompt) throws Exception {
		
		String urlString = "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL_NAME + ":generateContent?key=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoOutput(true);
        
        // Language selection is handled here as a single prompt, making it easier to support Korean responses later.
        prompt = prompt + "\n All answer should be in English.";
        
        //Convert to JSON
        JSONObject jsonInput = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject part = new JSONObject().put("text", prompt);
        JSONObject content = new JSONObject().put("parts", new JSONArray().put(part));
        contents.put(content);
        jsonInput.put("contents", contents);

        String jsonInputString = jsonInput.toString();

        //output
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) { // 200 is a success response code
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Extract only the message from the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("candidates")) {
                    JSONArray candidates = jsonResponse.getJSONArray("candidates");
                    StringBuilder message = new StringBuilder();
                    for (int i = 0; i < candidates.length(); i++) {
                        JSONObject content1 = candidates.getJSONObject(i).getJSONObject("content");
                        JSONArray parts = content1.getJSONArray("parts");
                        for (int j = 0; j < parts.length(); j++) {
                            message.append(parts.getJSONObject(j).getString("text")).append("\n");
                        }
                    }
                    return message.toString().trim();
                } else { // The response came, but it was not in a normal form.
                	throw new RuntimeException();
                }
            }
        } else { // Response code is incorrect
        	
            throw new RuntimeException();
        } 
	}

}
