# AlgoLens (Java Swing PS Assistant)

**AlgoLens** is a Java Swing–based desktop application designed to improve the algorithm problem-solving (PS) learning experience by addressing two common issues: **fragmented explanations** and **lack of structural reasoning** in typical solutions.

The program loads problems either by crawling BOJ (Baekjoon Online Judge) or by accepting direct user input. It then leverages an LLM to provide **structured explanations** (solution strategy, time/space complexity) and **code-level feedback**, enabling users to compare and refine their own reasoning and implementations.

---

## Project Purpose

When studying algorithmic problem solving independently, most explanations focus only on *how to solve a specific problem*, without clarifying:

- **Why a particular algorithm was chosen**
- **When to use 1D vs. 2D DP**
- **How the reasoning generalizes to similar problems**

Even with generative AI, high-quality explanations usually require very detailed prompts; otherwise, the output often degrades into a simple “correct solution” without insight.

This project aims to reduce that friction by:

- Explaining problems from a **decision-making perspective** (“why this approach”)
- Explicitly presenting **time and space complexity**
- Providing **code improvement feedback** while preserving the user’s original variable names and logic as much as possible

The goal is to help users **compare their own thought process with a structured reference**, rather than passively reading a finalized solution.

---

## Key Features

### 1) Problem Loading

- **BOJ Mode**
  - Enter a problem number → fetch problem data via **Jsoup crawling**
  - Separately stores:
    - Title
    - Problem description
    - Input format
    - Output format
  - Sample inputs/outputs are stored as `String[]`, allowing navigation across multiple samples via UI controls

- **User Input Mode**
  - Users directly paste problem descriptions
  - Uses **lazy loading**
    - No fetch on `Enter`
    - `UserInputProblem` is created and fetched only when **Explain** or **Feedback** is clicked

---

### 2) Problem Explanation (LLM)

- Generates:
  - Structured solution approach
  - Time complexity
  - Space complexity
- Responses are returned in **Markdown**, converted to **HTML**, and rendered in a dialog for readability

---

### 3) Code Feedback (LLM)

- Analyzes user code based on the selected language:
  - Java / C / C++ / Python
- Produces:
  - Explanatory feedback
  - Improved version of the code
- Improved code can be saved, with file extensions automatically determined by language

---

### 4) Asynchronous Processing with SwingWorker

- Crawling and API calls are executed using **SwingWorker**
- A **LoadingDialog** is displayed during background processing
- UI updates are handled via callbacks on the EDT after task completion

---

## Program Logic Flow (Summary)

All major operations follow a consistent flow:

**User Action → Background Worker → EDT Callback → UI Update**

- **Enter Problem**
  - BOJ Mode: `ProblemFetchWorker` → crawl → store `Problem` → update UI
  - User Input Mode: no fetch (input only)
- **Explain**
  - BOJ Mode: run `ProblemExplainWorker` using `currentProblem`
  - User Input Mode: create + fetch `UserInputProblem` → run `ExplainWorker`
- **Feedback**
  - Same flow as Explain, followed by `ProblemFeedbackWorker`

---

## Architecture Overview

### Package Structure

#### `ui`
- `MainFrame`  
  Main UI controller, event handling, worker execution, and state management
- `ProblemExplainDialog`  
  Displays problem explanations (HTML rendering)
- `ProblemFeedbackDialog`  
  Displays code feedback and supports saving improved code
- `LoadingDialog`  
  Indicates background processing

#### `problem`
- `Problem` (abstract)  
  Common problem model (title, description, input/output, samples) + `fetch()`
- `WebProblem` (abstract)  
  Base class for web-based problems (future extensibility)
- `BOJProblem`  
  Implements `fetch()` via Jsoup crawling
- `UserInputProblem`  
  Implements `fetch()` based on user-provided input (lazy loading)
- `ProblemFactory`  
  Creates appropriate `Problem` instances based on mode

#### `language`
- `LanguageStrategy` (interface)  
  Abstracts language-specific rules (extensions, prompt policies)
- `JavaLanguage`, `CppLanguage`, `CLanguage`, `PythonLanguage`  
  Language-specific implementations
- `LanguageFactory`  
  Maps user-selected language to the corresponding strategy

#### `worker`
- `ProblemFetchWorker`  
  Loads problems asynchronously
- `ProblemExplainWorker`  
  Generates explanations and complexity analysis
- `ProblemFeedbackWorker`  
  Analyzes and improves user code

#### `util` (or similar)
- `CallAPI`  
  Handles JSON-based API requests/responses (`org.json`)
- `MarkdownUtil`  
  Converts Markdown to HTML
- `CodeCleaner`  
  Post-processing (e.g., removing code fences like ```cpp)
- `IntegerFilter`  
  Restricts problem number input fields to integers

---

### Design Rationale

- Adding new platforms (e.g., LeetCode, Programmers) only requires extending the `Problem` hierarchy
- Supporting new languages only requires implementing a new `LanguageStrategy` and updating the factory
- SwingWorker cleanly separates network/crawling tasks from the UI, preventing freezes

---

## Tech Stack

- **Java Swing** – GUI
- **Jsoup** – BOJ HTML crawling
- **org.json** – JSON request/response handling
- **SwingWorker** – background task execution
- **File I/O** – loading and saving code

---

## How to Run

1. Import the project into **Eclipse** or **IntelliJ**.
2. Add external libraries:
   - `jsoup`
   - `org.json`
3. Run `MainFrame` (or the main entry class).

> API key and endpoint configuration depend on the `CallAPI` implementation.  
> If environment variables are used, configure them in your IDE’s Run Configuration.

---

## Usage

1. Select **Mode**
   - BOJ: enter problem number → `Enter`
   - User Input: paste problem description directly
2. Click **Explain** to receive a structured explanation and complexity analysis
3. Load or paste code and click **Feedback**
4. Save the improved code if needed

---

## Future Improvements

- Support for LeetCode / Programmers via new `WebProblem` subclasses
- More robust Markdown rendering (code blocks, lists, math)
- Local compile/test execution integration
- API response caching to reduce repeated calls

---

## License

For educational use only.
