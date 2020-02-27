package dbComponent.Context;

import dbComponent.Models.ModelDelete;
import dbComponent.Models.ModelInsert;
import dbComponent.Models.ModelUpdate;

public abstract class PendingOperation {
    // Fields:
    protected ModelInsert modelInsert;
    protected ModelDelete modelDelete;
    protected ModelUpdate modelUpdate;
    protected ModelOperationType operationType;

    // Constructor:

    // Getters
    public ModelInsert getModelInsert() {
        return this.modelInsert;
    }

    public ModelDelete getModelDelete() {
        return modelDelete;
    }

    public ModelUpdate getModelUpdate() {
        return modelUpdate;
    }

    public ModelOperationType getOperationType() {
        return this.operationType;
    }
}
