import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
public class FileManager {
    void saveSubjects(ArrayList<Subject> subjects) {
        try {
            FileWriter writer =
                    new FileWriter("study_data.txt");
            for (Subject subject : subjects) {
                writer.write(
                        subject.name + "\n"
                );
                writer.write(
                        subject.difficulty + "\n"
                );
                writer.write(
                        subject.examDate + "\n"
                );
                for (Topic topic : subject.topics) {
                    writer.write(
                            topic.name + "," +
                            topic.requiredHrs + "," +
                            topic.completedHrs + "\n"
                    );
                }
                writer.write("END\n");
            }
            writer.close();
            System.out.println(
                    "\nStudy data saved successfully!"
            );
        } catch (IOException e) {
            System.out.println(
                    "Error while saving data."
            );
        }
    }
    ArrayList<Subject> loadSubjects() {
        ArrayList<Subject> subjects = new ArrayList<>();
        File file = new File("study_data.txt");
        if (!file.exists()) {
            return subjects;
        }
        try {
            Scanner fileSc = new Scanner(file);
            while (fileSc.hasNextLine()) {
                String name = fileSc.nextLine().trim();
                if (name.isEmpty()) {
                    continue;
                }
                if (!fileSc.hasNextLine()) break;
                int difficulty = Integer.parseInt(fileSc.nextLine().trim());
                if (!fileSc.hasNextLine()) break;
                LocalDate examDate = LocalDate.parse(fileSc.nextLine().trim());
                Subject subject = new Subject(name, difficulty, examDate);
                while (fileSc.hasNextLine()) {
                    String line = fileSc.nextLine().trim();
                    if (line.equals("END")) {
                        break;
                    }
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String topicName = parts[0];
                        double reqHrs = Double.parseDouble(parts[1]);
                        double compHrs = Double.parseDouble(parts[2]);
                        Topic topic = new Topic(topicName, reqHrs);
                        topic.study(compHrs);
                        subject.addTopic(topic);
                    }
                }
                subjects.add(subject);
            }
            fileSc.close();
            System.out.println("Loaded " + subjects.size() + " subjects from study_data.txt.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved data file found.");
        } catch (Exception e) {
            System.out.println("Error while loading data: " + e.getMessage());
        }
        return subjects;
    }
}