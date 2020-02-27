package Interface.Components.MarkingsPaneComponents;

import Interface.Components.QuickLongTooltip;
import dbComponent.Models.AnswerModel;
import dbComponent.Models.MarkedAnswerModel;
import dbComponent.Models.ModuleModel;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;

public class ModuleTableCell extends TableCell<MarkedAnswerModel, String> {

    // Methods:
    private void setToolTip(ModuleModel module) {
        Label content = new Label(module.getDisplayName());
        content.setTooltip(new QuickLongTooltip(module.getDisplayName()));

        this.setGraphic(content);
    }

    private ModuleModel getModuleModel() {
        TableRow<MarkedAnswerModel> tr = this.getTableRow();
        if (tr == null) {
            return null;
        }

        MarkedAnswerModel ma = tr.getItem();

        if (ma == null) {
            return null;
        }

        AnswerModel am = ma.getAnswer();

        if (am == null) {
            return null;
        }

        return am.getModule();
    }

    @Override
    protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);

        ModuleModel module = getModuleModel();
        if (module == null) {
            this.setGraphic(null);
        } else {
            setToolTip(module);
        }
    }
}
