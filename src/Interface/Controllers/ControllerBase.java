package Interface.Controllers;

import Interface.SceneSwitcher;
import dbComponent.Context.DataModelContext;

public abstract class ControllerBase {

    protected SceneSwitcher switcher;
    protected DataModelContext context = DataModelContext.getInstance();

    public void setSwitcher(SceneSwitcher switcher) {
        this.switcher = switcher;
    }
}
