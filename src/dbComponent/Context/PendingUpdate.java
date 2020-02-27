package dbComponent.Context;

import dbComponent.Models.ModelUpdate;

public class PendingUpdate extends PendingOperation {

    public PendingUpdate(ModelUpdate model) {
        this.operationType = ModelOperationType.UPDATE;
        this.modelUpdate = model;
    }

}
