package dbComponent.data;

import java.util.ArrayList;
import java.util.List;

public class StructureDB {

    // Fields:
    private Coordinator coordinator;
    private Semester semester;
    private List<Module> modules = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();
    private List<Answer> answers = new ArrayList<>();

    // Setters
    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    // Getters
    public Coordinator getCoordinator() {
        return this.coordinator;
    }

    public Semester getSemester() {
        return this.semester;
    }

    public List<Module> getModules() {
        return this.modules;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }
}
