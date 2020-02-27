package dbComponent.Models;

import Interface.Components.GraphicUpdateListener;
import dbComponent.database.DB;
import javafx.scene.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ModelUpdate {

    PreparedStatement getUpdateStatement(Connection connection) throws SQLException;

    void addUpdateListener(Node source, GraphicUpdateListener listener);

    void UpdateModel(DB database) throws SQLException;

}
