package dbComponent.Models;

import dbComponent.Context.DataModelContext;
import dbComponent.data.Coordinator;
import dbComponent.database.DB;

import java.util.List;

public class CoordinatorModel extends Coordinator {
    private List<SemesterModel> semesters;

    public CoordinatorModel(int coordinatorID, String email) {
        super(coordinatorID, email);
    }

    public void loadData(DB database) {
        try {
            this.semesters = database.SELECT_semestersWhereCoordinatorId(this.getId());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public List<SemesterModel> getSemesters() {
        this.loadData(DataModelContext.getInstance().getSyncronousDatabase());
        return this.semesters;
    }

    public SemesterModel getSemesterOfModuleId(int id) {
        //Returns first semester that contains a module where the id corresponds to the given Id
        return this.semesters
                .stream()
                .filter(s -> s.getModules().stream().filter(m -> m.getId() == id)
                        .count() > 0)
                .findFirst()
                .orElse(null);
    }
}
