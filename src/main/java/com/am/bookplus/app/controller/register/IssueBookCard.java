package com.am.bookplus.app.controller.register;

import com.am.bookplus.app.Utils;
import com.am.bookplus.app.net.Service;
import com.am.bookplus.app.controller.INotifyAction;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import static com.am.bookplus.app.net.Response.ResultCode.RESULT_ERROR;
import static com.am.bookplus.app.net.Response.ResultCode.RESULT_OK;
import com.am.bookplus.app.net.entity.EntityRegister;
import com.am.bookplus.app.net.entity.RegisterData;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.text.ParseException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.Instant;
import java.util.Date;

public class IssueBookCard
{
    private INotifyAction notifyHandler;
    public void setNotifyHandler(INotifyAction notifyHandler) {
        this.notifyHandler = notifyHandler;
    }

    @FXML private TextField in_isbn;
    @FXML private TextField in_id;
    @FXML private TextField in_days;

    @FXML private Node cover_wait;

    @FXML private Label lbl_log;

    @FXML
    public void initialize()
    {
        in_isbn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                in_isbn.setText(newValue.toUpperCase());
            }
        });
        in_days.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*")) {
                    in_days.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
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

        String isbn = in_isbn.getText().trim();
        String student_id = in_id.getText().trim();
        int days = in_days.getText().equals("") ? -1 : Integer.parseUnsignedInt(in_days.getText());

        if (!Utils.checkISBN(isbn))
        {
            input_error = true;
            in_isbn.getStyleClass().add("error");
        }
        if (!Utils.checkStudentId(student_id))
        {
            input_error = true;
            in_id.getStyleClass().add("error");
        }
        if (days <= 0)
        {
            input_error = true;
            in_days.getStyleClass().add("error");
        }
        if (input_error)
        {
            logError("Some info are incorrect");
            return;
        }

        net_issueBook(isbn, student_id, days);
    }

    private void net_issueBook(String isbn, String student_id, int days)
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
                    //--Create a new RegisterData--//
                    RegisterData data = new RegisterData(isbn, student_id, Date.from(Instant.now()), days);
                    //-----------------------------//

                    //---------Send Request--------//
                    Response response = Service.issueBook(data);
                    
                    if (response.code == RESULT_OK)
                    {
                        JsonObject jsonRegister = response.payload.getAsJsonObject();
                        EntityRegister new_register = new EntityRegister(jsonRegister.get("id").getAsInt(), jsonRegister.get("book").getAsJsonObject().get("isbn").getAsString(), jsonRegister.get("student").getAsJsonObject().get("id").getAsString(), jsonRegister.get("date").getAsString(), jsonRegister.get("days").getAsInt());

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                net_issueBook_result(response.code, new_register);
                            }
                        });
                        
                        return null;
                    }
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_issueBook_result(response.code, null);
                        }
                    });
                    //-----------------------------//
                }
                catch (MalformedResponseException | IOException | ParseException e)
                {
                    //An error occurred
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_issueBook_result(RESULT_ERROR, null);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_issueBook_result(Response.ResultCode code, EntityRegister new_register)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                //Clear form
                in_isbn.clear();
                in_id.clear();
                in_days.clear();

                //Update Log
                log("Book issued");

                //Update the register
                if (notifyHandler != null) notifyHandler.notify(INotifyAction.Action.INSERT, new_register);
                break;
            }
            case RESULT_BOOK_NOT_EXISTS:
            {
                //Update Log
                logError("ISBN not registered");
                break;
            }
            case RESULT_STUDENT_NOT_EXISTS:
            {
                //Update Log
                logError("Student not registered");
                break;
            }
            case RESULT_BOOK_NOT_AVAILABLE:
            {
                //Update Log
                logError("Book not available");
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
    public void in_isbn_change()
    {
        in_isbn.getStyleClass().removeAll("error");
        logClear();
    }
    @FXML
    public void in_id_change()
    {
        in_id.getStyleClass().removeAll("error");
        logClear();
    }
    @FXML
    public void in_days_change()
    {
        in_days.getStyleClass().removeAll("error");
        logClear();
    }
}
