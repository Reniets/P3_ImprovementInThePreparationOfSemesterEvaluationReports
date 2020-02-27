package dbComponent.Models;

import Interface.Components.GraphicUpdateListener;
import dbComponent.database.DB;
import javafx.scene.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ModelDelete {

    PreparedStatement getDeleteStatement(Connection connection) throws SQLException;

    void DeleteModel(DB database) throws SQLException;

    void addRemoveListener(Node source, GraphicUpdateListener listener);

    void remove();
}
