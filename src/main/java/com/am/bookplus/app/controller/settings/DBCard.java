package com.am.bookplus.app.controller.settings;

import com.am.bookplus.app.net.DBSettings;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBCard
{
    @FXML private Node cover_wait;
    @FXML private Label lbl_log;

    @FXML
    public void btn_create_click()
    {
        cover_wait.setVisible(true);

        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call()
            {
                try (Connection connection = DriverManager.getConnection(DBSettings.DB_URL, DBSettings.DB_USER, DBSettings.DB_PASSWORD);
                     Statement stmt = connection.createStatement()
                ){
                    stmt.execute(DBSettings.CREATE_QUERY);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            create_db_result(true);
                        }
                    });
                }
                catch (Exception e)
                {
                    //An error occurred
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            create_db_result(false);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    @FXML
    public void btn_load_click()
    {
        cover_wait.setVisible(true);

        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call()
            {
                try (Connection connection = DriverManager.getConnection(DBSettings.DB_URL, DBSettings.DB_USER, DBSettings.DB_PASSWORD);
                     Statement stmt = connection.createStatement()
                ){
                    stmt.execute(DBSettings.INSERT_TEST_DATA_QUERY);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            load_data_result(true);
                        }
                    });
                }
                catch (Exception e)
                {
                    //An error occurred
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            load_data_result(false);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }

    private void load_data_result(boolean success)
    {
        cover_wait.setVisible(false);

        if (success) log("Test data loaded successfully");
        else logError("Couldn't load test data");
    }
    private void create_db_result(boolean success)
    {
        cover_wait.setVisible(false);

        if (success) log("Database created successfully");
        else logError("Database creation failed");
    }

    private void logError(String msg)
    {
        if (!lbl_log.getStyleClass().contains("error"))
            lbl_log.getStyleClass().add("error");

        lbl_log.setText(msg);
    }
    private void log(String msg)
    {
        lbl_log.getStyleClass().remove("error");
        lbl_log.setText(msg);
    }
    private void logClear()
    {
        lbl_log.setText("");
    }
}
