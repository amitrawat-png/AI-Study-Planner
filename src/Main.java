import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    static DateTimeFormatter dmyFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FileManager fileManager = new FileManager();
        Planner planner = new Planner();
        System.out.println("========================================");
        System.out.println("          AI STUDY PLANNER");
        System.out.println("========================================");
        ArrayList<Subject> subjects = fileManager.loadSubjects();
        if (subjects.isEmpty()) {
            System.out.println("\nNo existing study data found.");
            System.out.print("Would you like to add your first subject now? (y/n): ");
            String answer = sc.nextLine().trim();
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                addNewSubject(sc, subjects);
                fileManager.saveSubjects(subjects);
            }
        }
        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("               MAIN MENU");
            System.out.println("========================================");
            System.out.println("1. View All Subjects & Study Progress");
            System.out.println("2. Generate Today's Study Plan");
            System.out.println("3. Add a New Subject");
            System.out.println("4. Add a Topic to a Subject");
            System.out.println("5. Record Study Time for a Topic");
            System.out.println("6. View Subject Priorities (AI Ranking)");
            System.out.println("7. Save Data");
            System.out.println("8. Exit");
            System.out.println("========================================");
            int choice = readInt(sc, "Enter your choice (1-8): ", 1, 8);
            switch (choice) {
                case 1:
                    planner.showProgress(subjects);
                    break;
                case 2:
                    if (subjects.isEmpty()) {
                        System.out.println("\nNo subjects available! Please add a subject first.");
                    } else {
                        double availableHours = readDouble(sc, "\nEnter available study hours today: ", 0.1);
                        planner.generateStudyPlan(subjects, availableHours);
                        fileManager.saveSubjects(subjects);
                    }
                    break;
                case 3:
                    addNewSubject(sc, subjects);
                    fileManager.saveSubjects(subjects);
                    break;
                case 4:
                    addTopicToSubject(sc, subjects);
                    fileManager.saveSubjects(subjects);
                    break;
                case 5:
                    recordStudyTime(sc, subjects);
                    fileManager.saveSubjects(subjects);
                    break;
                case 6:
                    planner.showPriority(subjects);
                    break;
                case 7:
                    fileManager.saveSubjects(subjects);
                    break;
                case 8:
                    fileManager.saveSubjects(subjects);
                    System.out.println("\nThank you for using AI Study Planner. Goodbye!");
                    running = false;
                    break;
            }
        }
        sc.close();
    }
    static void addNewSubject(Scanner sc, ArrayList<Subject> subjects) {
        System.out.println("\n===== ADD NEW SUBJECT =====");
        String name = readString(sc, "Enter subject name: ");
        int difficulty = readInt(sc, "Enter difficulty (1 = Easy, 2 = Medium, 3 = Hard): ", 1, 3);
        LocalDate examDate = readDate(sc, "Enter exam date (dd-MM-yyyy): ");
        Subject subject = new Subject(name, difficulty, examDate);
        int numberOfTopics = readInt(sc, "Enter number of topics: ", 0, 100);
        for (int j = 1; j <= numberOfTopics; j++) {
            System.out.println("\nTopic " + j);
            String topicName = readString(sc, "Enter topic name: ");
            double hours = readDouble(sc, "Enter required study hours: ", 0.1);
            double completedHours = readDouble(sc, "Enter already completed hours (0 to " + hours + "): ", 0.0);
            while (completedHours > hours) {
                System.out.println("Completed hours cannot exceed required hours (" + hours + ")!");
                completedHours = readDouble(sc, "Enter already completed hours: ", 0.0);
            }
            Topic topic = new Topic(topicName, hours);
            topic.study(completedHours);
            subject.addTopic(topic);
        }
        subjects.add(subject);
        System.out.println("\nSubject '" + name + "' added successfully!");
    }
    static void addTopicToSubject(Scanner sc, ArrayList<Subject> subjects) {
        if (subjects.isEmpty()) {
            System.out.println("\nNo subjects available! Please add a subject first.");
            return;
        }
        System.out.println("\n===== SELECT SUBJECT =====");
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println((i + 1) + ". " + subjects.get(i).name);
        }
        int subIndex = readInt(sc, "Choose subject (1-" + subjects.size() + "): ", 1, subjects.size()) - 1;
        Subject selectedSubject = subjects.get(subIndex);
        System.out.println("\nAdding topic to: " + selectedSubject.name);
        String topicName = readString(sc, "Enter topic name: ");
        double hours = readDouble(sc, "Enter required study hours: ", 0.1);
        double completedHours = readDouble(sc, "Enter already completed hours (0 to " + hours + "): ", 0.0);
        while (completedHours > hours) {
            System.out.println("Completed hours cannot exceed required hours (" + hours + ")!");
            completedHours = readDouble(sc, "Enter already completed hours: ", 0.0);
        }
        Topic topic = new Topic(topicName, hours);
        topic.study(completedHours);
        selectedSubject.addTopic(topic);
        System.out.println("\nTopic '" + topicName + "' added to " + selectedSubject.name + "!");
    }
    static void recordStudyTime(Scanner sc, ArrayList<Subject> subjects) {
        if (subjects.isEmpty()) {
            System.out.println("\nNo subjects available!");
            return;
        }
        System.out.println("\n===== SELECT SUBJECT =====");
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println((i + 1) + ". " + subjects.get(i).name);
        }
        int subIndex = readInt(sc, "Choose subject (1-" + subjects.size() + "): ", 1, subjects.size()) - 1;
        Subject selectedSubject = subjects.get(subIndex);
        if (selectedSubject.topics.isEmpty()) {
            System.out.println("\nNo topics found for " + selectedSubject.name + ".");
            return;
        }
        System.out.println("\n===== SELECT TOPIC IN " + selectedSubject.name + " =====");
        for (int i = 0; i < selectedSubject.topics.size(); i++) {
            Topic t = selectedSubject.topics.get(i);
            System.out.println((i + 1) + ". " + t.name + " (Remaining: " + String.format("%.2f", t.getRemainingHours()) + " hrs)");
        }
        int topicIndex = readInt(sc, "Choose topic (1-" + selectedSubject.topics.size() + "): ", 1, selectedSubject.topics.size()) - 1;
        Topic selectedTopic = selectedSubject.topics.get(topicIndex);
        if (selectedTopic.completed) {
            System.out.println("\nThis topic is already completed!");
            return;
        }
        double hoursStudied = readDouble(sc, "Enter hours studied: ", 0.1);
        selectedTopic.study(hoursStudied);
        System.out.println("\nProgress recorded! " + selectedTopic.name + " is now at " +
                String.format("%.2f", selectedTopic.completedHrs) + "/" + selectedTopic.requiredHrs + " hours.");
        if (selectedTopic.completed) {
            System.out.println("Congratulations! You completed this topic!");
        }
    }
    static String readString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }
    static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid whole number.");
            }
        }
    }
    static double readDouble(Scanner sc, String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= min) {
                    return value;
                }
                System.out.println("Value must be at least " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }
    static LocalDate readDate(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return LocalDate.parse(input, dmyFormatter);
            } catch (DateTimeParseException e1) {
                try {
                    return LocalDate.parse(input);
                } catch (DateTimeParseException e2) {
                    System.out.println("Invalid date format! Please enter date in dd-MM-yyyy format (e.g. 25-12-2026).");
                }
            }
        }
    }
}