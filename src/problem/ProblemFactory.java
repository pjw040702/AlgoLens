package problem;

import ui.MainFrame.Mode;

public class ProblemFactory {
//This is to get only the Mode from the main frame for some problem type and to create a Problem object with ProblemFetchWorker without an if statement.
    public static Problem create(Mode type, int problemNumber, String directText) {
        switch(type) {
            case BOJ:
                return new BOJProblem(problemNumber);
            case USER_INPUT:
                return new UserInputProblem(directText);
            default:
                throw new IllegalArgumentException("Unknown source: " + type);
        }
    }
}