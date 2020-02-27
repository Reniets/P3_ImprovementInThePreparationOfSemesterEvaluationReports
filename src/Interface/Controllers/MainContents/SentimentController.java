package Interface.Controllers.MainContents;

import Enums.SentimentPolarity;
import Interface.Controllers.ControllerBase;
import dbComponent.Models.ModuleModel;
import dbComponent.Models.SentimentModel;
import dbComponent.database.DB;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;
import java.util.List;

public class SentimentController extends ControllerBase {

    @FXML
    private BarChart sentimentBarChart;

    public void initialize() throws SQLException, ClassNotFoundException {
        DB database = new DB();

        for (ModuleModel module : context.getCurrentSemester().getModules()) {
            int negativeScore = 0;
            int positiveScore = 0;
            XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
            dataSeries.setName(module.getName());
            List<SentimentModel> sentiments = database.SELECT_SentimentsWhereModuleID(module.getId());

            for (SentimentModel sentiment : sentiments) {
                if (sentiment.getSentimentPolarity() == SentimentPolarity.NEGATIVE) {
                    negativeScore++;
                } else if (sentiment.getSentimentPolarity() == SentimentPolarity.POSITIVE) {
                    positiveScore++;
                } else {
                    throw new RuntimeException("Null sentiment received from database");
                }
            }

            dataSeries.getData().add(new XYChart.Data<String, Integer>("Positiv", positiveScore));
            dataSeries.getData().add(new XYChart.Data<String, Integer>("Negativ", negativeScore));
            this.sentimentBarChart.getData().add(dataSeries);
        }

        // Apply the dataseries to the BarChart

    }
}
