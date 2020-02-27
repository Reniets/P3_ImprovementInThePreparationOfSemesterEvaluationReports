package dbComponent.Context;

import dbComponent.Models.ModelDelete;

public class PendingDelete extends PendingOperation {

    PendingDelete(ModelDelete model) {
        this.operationType = ModelOperationType.DELETE;
        this.modelDelete = model;
    }

}
