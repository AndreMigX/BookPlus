package com.am.bookplus.app.controller.student;

import com.am.bookplus.app.Utils;
import com.am.bookplus.app.net.Service;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import static com.am.bookplus.app.net.Response.ResultCode.RESULT_ERROR;
import static com.am.bookplus.app.net.Response.ResultCode.RESULT_OK;
import com.am.bookplus.app.net.entity.EntityStudent;
import com.google.gson.Gson;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FindStudentCard
{
    @FXML private TextField in_id;
    @FXML private TextField in_name;
    @FXML private TextField in_surname;
    @FXML private TextField in_email;

    @FXML private Node cover_wait;

    @FXML private Label lbl_log;
    
    @FXML
    public void initialize()
    {
        in_id.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*")) {
                    in_id.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    public void btn_find_click()
    {
        //Check input
        String id = in_id.getText().trim();

        if (!Utils.checkStudentId(id))
        {
            in_id.getStyleClass().add("error");

            logError("ID not valid");
            return;
        }

        net_findStudent(id);
    }

    private void net_findStudent(String id)
    {
        if (cover_wait.isVisible()) return;
        cover_wait.setVisible(true);

        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call()
            {
                try
                {
                    //---------Send Request--------//
                    Response response = Service.findStudent(id);
                    
                    if (response.code == RESULT_OK)
                    {
                        Gson gson = new Gson();
                        EntityStudent student = gson.fromJson(response.payload, EntityStudent.class);
                        
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                net_findStudent_result(response.code, student);
                            }
                        });
                        
                        return null;
                    }
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_findStudent_result(response.code, null);
                        }
                    });
                    //-----------------------------//
                }
                catch (MalformedResponseException | IOException e)
                {
                    //An error occurred
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_findStudent_result(RESULT_ERROR, null);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_findStudent_result(Response.ResultCode code, EntityStudent student)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                //Update Log
                logClear();

                //Show
                in_name.setText(student.name);
                in_surname.setText(student.surname);
                in_email.setText(student.email);
                
                break;
            }
            case RESULT_STUDENT_NOT_EXISTS:
            {
                //Update Log
                logError("ID not found");
                break;
            }
            case RESULT_ERROR:
            {
                //Update Log
                logError("An error occurred");
                break;
            }
        }
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
    
    public void in_id_change()
    {
        in_id.getStyleClass().removeAll("error");
        
        in_name.clear();
        in_surname.clear();
        in_email.clear();
        
        logClear();
    }
}
