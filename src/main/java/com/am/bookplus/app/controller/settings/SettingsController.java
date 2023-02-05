package com.am.bookplus.app.controller.settings;

import com.am.bookplus.app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class SettingsController
{
    @FXML
    public FlowPane card_container;

    @FXML
    public void initialize()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("card-db-view.fxml"));
            Node db_card = loader.load();
            card_container.getChildren().add(db_card);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
