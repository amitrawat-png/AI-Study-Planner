import java.util.ArrayList;
import java.util.Comparator;
public class Planner {
    double priority(Subject subject) {
        double urgent =
                100.0 / subject.getRemainingDays();
        int remainingTopics = 0;
        for (Topic topic : subject.topics) {
            if (!topic.completed) {
                remainingTopics++;
            }
        }
        double result =
                urgent * 0.5
                + subject.difficulty * 10
                + remainingTopics * 5;
        return result;
    }
    void showPriority(ArrayList<Subject> subjects) {
        if (subjects.isEmpty()) {
            System.out.println("\nNo subjects available.");
            return;
        }
        sortSubjects(subjects);
        System.out.println("\n===== SUBJECT PRIORITIES (AI RANKED) =====");
        int rank = 1;
        for (Subject subject : subjects) {
            double result = priority(subject);
            System.out.println(
                    rank + ". " + subject.name +
                    " -> Priority Score: " +
                    String.format("%.2f", result) +
                    " (Days left: " + subject.getRemainingDays() +
                    ", Difficulty: " + subject.difficulty + "/3)"
            );
            rank++;
        }
    }
    void sortSubjects(ArrayList<Subject> subjects) {
        subjects.sort(
                Comparator.comparingDouble(this::priority)
                        .reversed()
        );
    }
    void generateStudyPlan(
            ArrayList<Subject> subjects,
            double availableHours) {
        if (subjects.isEmpty()) {
            System.out.println("\nNo subjects available to generate a study plan.");
            return;
        }
        if (availableHours <= 0) {
            System.out.println("\nAvailable study hours must be greater than 0.");
            return;
        }
        sortSubjects(subjects);
        System.out.println(
                "\n===== TODAY'S STUDY PLAN ====="
        );
        double remainingHours = availableHours;
        int plannedCount = 0;
        for (Subject subject : subjects) {
            if (remainingHours <= 0) {
                break;
            }
            for (Topic topic : subject.topics) {
                if (topic.completed) {
                    continue;
                }
                if (remainingHours <= 0) {
                    break;
                }
                double studyHours = Math.min(
                        topic.getRemainingHours(),
                        remainingHours
                );
                plannedCount++;
                System.out.println(
                        "\n" + plannedCount + ". Subject: " + subject.name
                );
                System.out.println(
                        "   Topic: " + topic.name
                );
                System.out.println(
                        "   Allocated Study Time: " +
                        String.format("%.2f", studyHours) +
                        " hours"
                );
                topic.study(studyHours);
                remainingHours -= studyHours;
            }
        }
        if (plannedCount == 0) {
            System.out.println("\nAll topics are already completed! Nothing to plan for today.");
        } else {
            System.out.println(
                    "\nTotal Planned Time: " +
                    String.format("%.2f", (availableHours - remainingHours)) +
                    " hours"
            );
            System.out.println(
                    "Unused study time: " +
                    String.format(
                            "%.2f",
                            remainingHours
                    ) +
                    " hours"
            );
        }
    }
    void showProgress(ArrayList<Subject> subjects) {
        System.out.println(
                "\n===== STUDY PROGRESS ====="
        );
        if (subjects.isEmpty()) {
            System.out.println("No subjects available.");
            return;
        }
        for (Subject subject : subjects) {
            System.out.println(
                    "\nSubject: " + subject.name +
                    " | Exam Date: " + subject.examDate +
                    " (" + subject.getRemainingDays() + " days left)" +
                    (subject.isCompleted() ? " [ALL TOPICS COMPLETED]" : "")
            );
            if (subject.topics.isEmpty()) {
                System.out.println("  (No topics added yet)");
            }
            for (Topic topic : subject.topics) {
                double remaining =
                        topic.getRemainingHours();
                String status = topic.completed ? "[DONE] " : "[IN PROGRESS] ";
                System.out.println(
                        "  " + status + topic.name +
                        " -> " +
                        String.format(
                                "%.2f",
                                topic.completedHrs
                        ) +
                        "/" +
                        topic.requiredHrs +
                        " hours (Remaining: " +
                        String.format(
                                "%.2f",
                                remaining
                        ) +
                        " hours)"
                );
            }
            System.out.println(
                    "  Total: " +
                    String.format("%.2f", subject.getTotalCompletedHours()) +
                    "/" +
                    String.format("%.2f", subject.getTotalRequiredHours()) +
                    " hours completed"
            );
        }
    }
}