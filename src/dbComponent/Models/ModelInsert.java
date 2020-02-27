package dbComponent.Models;

import Interface.Components.GraphicUpdateListener;
import dbComponent.database.DB;
import javafx.scene.Node;

import java.sql.SQLException;

public interface ModelInsert {

    void InsertModel(DB database) throws SQLException;

    void addToContext();

    void addInsertListener(Node source, GraphicUpdateListener listener);
}
