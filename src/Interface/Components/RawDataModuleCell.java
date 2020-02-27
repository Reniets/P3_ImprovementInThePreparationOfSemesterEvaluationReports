package Interface.Components;

import dbComponent.Context.DataModelContext;
import dbComponent.Models.ModuleModel;
import dbComponent.Models.RawDataTable.Row;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;

public class RawDataModuleCell extends TableCell<Row, String> {

    @Override
    protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);

        ModuleModel module = this.getModuleModel();
        if (module != null) {
            this.setText(module.getDisplayName());
            this.setTooltip(new QuickLongTooltip(module.getName()));
        } else {
            this.setText("");
            this.setTooltip(null);
        }


    }

    private Row getRow() {
        TableRow<Row> tr = getTableRow();
        if (tr == null) {
            return null;
        }

        return tr.getItem() == null ? null : tr.getItem();
    }

    private ModuleModel getModuleModel() {
        Row row = this.getRow();
        if (row == null) {
            return null;
        }

        ModuleModel module = DataModelContext.getInstance().getCurrentSemester().getModules().stream().filter(m -> m.getId() == row.getModuleID()).findFirst().orElse(null);

        return module;
    }
}
