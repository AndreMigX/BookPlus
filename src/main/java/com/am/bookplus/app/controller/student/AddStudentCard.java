package com.am.bookplus.app.controller.student;

import com.am.bookplus.app.Utils;
import com.am.bookplus.app.net.Service;
import com.am.bookplus.app.controller.INotifyAction;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import static com.am.bookplus.app.net.Response.ResultCode.RESULT_EMAIL_NOT_VALID;
import com.am.bookplus.app.net.entity.EntityStudent;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static com.am.bookplus.app.net.Response.ResultCode.RESULT_ERROR;
import java.io.IOException;

public class AddStudentCard
{
    private INotifyAction notifyHandler;
    public void setNotifyHandler(INotifyAction notifyHandler) {
        this.notifyHandler = notifyHandler;
    }

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
    public void btn_add_click()
    {
        //Check input
        boolean input_error = false;

        String id = in_id.getText().trim();
        String name = in_name.getText().trim();
        String surname = in_surname.getText().trim();
        String email = in_email.getText().trim();

        if (!Utils.checkStudentId(id))
        {
            input_error = true;
            in_id.getStyleClass().add("error");
        }
        if (name.length() == 0)
        {
            input_error = true;
            in_name.getStyleClass().add("error");
        }
        if (surname.length() == 0)
        {
            input_error = true;
            in_surname.getStyleClass().add("error");
        }
        if (!Utils.checkEmail(email))
        {
            input_error = true;
            in_email.getStyleClass().add("error");
        }
        if (input_error)
        {
            logError("Some info are incorrect");
            return;
        }

        net_addStudent(id, name, surname, email);
    }

    private void net_addStudent(String id, String name, String surname, String email)
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
                    //----Create a new Student-----//
                    EntityStudent student = new EntityStudent(id, name, surname, email);
                    //-----------------------------//
                    
                    //---------Send Request--------//
                    Response response = Service.addStudent(student);
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_addStudent_result(response.code, student);
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
                            net_addStudent_result(RESULT_ERROR, null);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_addStudent_result(Response.ResultCode code, EntityStudent new_student)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                //Clear form
                in_id.clear();
                in_name.clear();
                in_surname.clear();
                in_email.clear();

                //Update Log
                log("Student saved");

                //Update the student list
                if (notifyHandler != null) notifyHandler.notify(INotifyAction.Action.INSERT, new_student);
                break;
            }
            case RESULT_STUDENT_ALREADY_EXISTS:
            {
                //Update Log
                logError("ID already registered");
                break;
            }
            case RESULT_ID_NOT_VALID:
            {
                //Update Log
                logError("ID not valid");
                break;
            }
            case RESULT_EMAIL_NOT_VALID:
            {
                //Update Log
                logError("Email not valid");
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

    @FXML
    public void in_id_change()
    {
        in_id.getStyleClass().removeAll("error");
        logClear();
    }
    @FXML
    public void in_name_change()
    {
        in_name.getStyleClass().removeAll("error");
        logClear();
    }
    @FXML
    public void in_surname_change()
    {
        in_surname.getStyleClass().removeAll("error");
        logClear();
    }
    @FXML
    public void in_email_change()
    {
        in_email.getStyleClass().removeAll("error");
        logClear();
    }
}
