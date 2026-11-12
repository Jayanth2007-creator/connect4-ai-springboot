# Connect-4 AI Engine (Minimax + Alpha-Beta Pruning)

A full-stack **Connect-4** web application built with **Java Spring Boot** and vanilla **HTML/CSS/JavaScript**. The AI opponent uses **Adversarial Search (Minimax with Alpha-Beta Pruning)** and a custom **Heuristic Evaluation Function** to calculate optimal moves in real time.

---

## Key Features
- **Unbeatable AI Engine:** Implements Minimax with Alpha-Beta Pruning and center-first column ordering (`{3, 2, 4, 1, 5, 0, 6}`) to cut search tree branches by over 70%.
- **Adjustable Search Depth:** Switch between **Easy (Depth 2)**, **Medium (Depth 4)**, and **Hard (Depth 6)** directly from the browser.
- **Live AI Performance Metrics:** Displays the exact number of **Nodes Evaluated** and **AI Think Time (ms)** after every turn.
- **Clean MVC Architecture:** Separates data models (`model`), AI game logic (`service`), and REST endpoints (`controller`).

---

## Tech Stack
- **Backend:** Java 17+, Spring Boot (Spring Web), Maven
- **Frontend:** HTML5, CSS3, JavaScript (`fetch` REST API)
- **AI Concepts:** Game Theory, Minimax Algorithm, Alpha-Beta Pruning, Heuristic Board Scoring

---

## How to Download and Run Locally

### Prerequisites
- **Java Development Kit (JDK) 17** or higher installed on your system.
- Active internet connection on the first run so Maven can download dependencies.

### Option 1: Download as ZIP (Easiest)
1. Click the green **`<> Code`** button at the top of this repository and select **Download ZIP**.
2. Extract the downloaded ZIP file to a folder on your laptop.
3. Open **Eclipse IDE** (or IntelliJ IDEA) and go to **File → Import → Maven → Existing Maven Projects**.
4. Browse to the extracted folder containing `pom.xml` and click **Finish**.
5. Open `Connect4Application.java`, right-click, and select **Run As → Java Application**.
6. Open your browser and visit **`http://localhost:8080`**.

### Option 2: Clone via Git
```bash
git clone <your-repository-url>
cd connect4-ai-springboot
```

Run using the Maven wrapper:
- **Windows:** `mvnw.cmd spring-boot:run`
- **Mac / Linux:** `./mvnw spring-boot:run`

Then open **`http://localhost:8080`** in your browser.
