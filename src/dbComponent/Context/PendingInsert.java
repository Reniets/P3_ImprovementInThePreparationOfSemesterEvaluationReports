package dbComponent.Context;

import dbComponent.Models.ModelInsert;

public class PendingInsert extends PendingOperation {

    public PendingInsert(ModelInsert model) {
        this.operationType = ModelOperationType.INSERT;
        this.modelInsert = model;
    }

}
