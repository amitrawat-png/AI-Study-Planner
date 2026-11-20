import java.util.ArrayList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public class Subject {
  String name;
  int difficulty;
  LocalDate examDate;
  ArrayList<Topic>topics=new ArrayList<>();
  Subject(String name,int difficulty,LocalDate examDate){
    this.name=name;
    this.difficulty=difficulty;
    this.examDate=examDate;
  }
   void addTopic(Topic topic){
    topics.add(topic);
   } 
   long getRemainingDays() {
        LocalDate today=LocalDate.now();
        long days=ChronoUnit.DAYS.between(today,examDate);
        if(days<=0){
            return 1;
        }
        return days;
   }
   double getTotalRequiredHours() {
       double total = 0;
       for (Topic topic : topics) {
           total += topic.requiredHrs;
       }
       return total;
   }
   double getTotalCompletedHours() {
       double total = 0;
       for (Topic topic : topics) {
           total += topic.completedHrs;
       }
       return total;
   }
   double getRemainingHours() {
       double remaining = 0;
       for (Topic topic : topics) {
           remaining += topic.getRemainingHours();
       }
       return remaining;
   }
   boolean isCompleted() {
       if (topics.isEmpty()) {
           return false;
       }
       for (Topic topic : topics) {
           if (!topic.completed) {
               return false;
           }
       }
       return true;
   }
}
