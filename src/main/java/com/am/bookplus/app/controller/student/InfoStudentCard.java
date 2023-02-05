package com.am.bookplus.app.controller.student;

import com.am.bookplus.app.net.Service;
import com.am.bookplus.app.controller.INotifyAction;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import com.am.bookplus.app.net.entity.EntityStudent;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static com.am.bookplus.app.net.Response.ResultCode.RESULT_ERROR;
import java.io.IOException;

public class InfoStudentCard implements INotifyAction
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
    @FXML private Node cover_select;

    @FXML private Label lbl_log;

    @FXML
    public void btn_save_click()
    {
        //Check input
        boolean input_error = false;

        String name = in_name.getText().trim();
        String surname = in_surname.getText().trim();
        String email = in_email.getText().trim();

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
        if (email.length() == 0)
        {
            input_error = true;
            in_email.getStyleClass().add("error");
        }
        if (input_error)
        {
            logError("Some info are incorrect");
            return;
        }

        net_saveStudent(in_id.getText(), name, surname, email);
    }
    @FXML
    public void btn_delete_click()
    {
        net_deleteStudent();
    }

    private void net_deleteStudent()
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
                    Response response = Service.deleteStudent(in_id.getText());

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_deleteStudent_result(response.code);
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
                            net_deleteStudent_result(RESULT_ERROR);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_deleteStudent_result(Response.ResultCode code)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                cover_select.setVisible(true);

                //Update the student list
                if (notifyHandler != null) notifyHandler.notify(INotifyAction.Action.DELETE, in_id.getText());
                break;
            }
            case RESULT_STUDENT_NOT_EXISTS:
            {
                //Update Log
                logError("ID not found");
                break;
            }
            case RESULT_CANT_DELETE_STUDENT:
            {
                //Update Log
                logError("Can't delete this student");
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

    private void net_saveStudent(String id, String name, String surname, String email)
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
                    Response response = Service.updateStudent(student);
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_saveStudent_result(response.code, student);
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
                            net_saveStudent_result(RESULT_ERROR, null);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_saveStudent_result(Response.ResultCode code, EntityStudent new_student)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                //Update Log
                log("Student saved");

                //Update the student list
                notifyHandler.notify(INotifyAction.Action.UPDATE, new_student);
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

    //INotifyAction Implementation
    @Override
    public void notify(Action action, Object data)
    {
        if (action != Action.SELECT) return;

        //The User has selected a Student from the list
        cover_select.setVisible(false);

        EntityStudent student = (EntityStudent) data;

        in_id.setText(student.getId());
        in_name.setText(student.getName());
        in_surname.setText(student.getSurname());
        in_email.setText(student.getEmail());

        in_name.getStyleClass().removeAll("error");
        in_surname.getStyleClass().removeAll("error");
        in_email.getStyleClass().removeAll("error");

        logClear();
    }
}