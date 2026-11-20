public class Topic {
    String name;
    double requiredHrs;
    double completedHrs;
    boolean completed;
    Topic(String name, double requiredHrs) {
        this.name = name;
        this.requiredHrs = requiredHrs;
        this.completedHrs = 0;
        this.completed = false;
    }
    double getRemainingHours() {
        return requiredHrs - completedHrs;
    }
    void study(double hours) {
        completedHrs += hours;
        if (completedHrs >= requiredHrs) {
            completedHrs = requiredHrs;
            completed = true;
        }
    }
}