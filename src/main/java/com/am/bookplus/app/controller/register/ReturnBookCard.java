package com.am.bookplus.app.controller.register;

import com.am.bookplus.app.net.Service;
import com.am.bookplus.app.controller.INotifyAction;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import com.am.bookplus.app.net.entity.EntityRegister;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static com.am.bookplus.app.net.Response.ResultCode.RESULT_ERROR;
import java.io.IOException;

public class ReturnBookCard implements INotifyAction
{
    private INotifyAction notifyHandler;
    public void setNotifyHandler(INotifyAction notifyHandler) {
        this.notifyHandler = notifyHandler;
    }

    @FXML private TextField in_id;
    @FXML private TextField in_date;
    @FXML private TextField in_days;
    @FXML private TextField in_isbn;
    @FXML private TextField in_student_id;

    @FXML private Node cover_wait;
    @FXML private Node cover_select;

    @FXML private Label lbl_log;

    @FXML
    public void btn_return_book()
    {
        net_returnBook(in_id.getText());
    }

    private void net_returnBook(String id)
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
                    Response response = Service.returnBook(id);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_returnBook_result(response.code);
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
                            net_returnBook_result(RESULT_ERROR);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_returnBook_result(Response.ResultCode code)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                cover_select.setVisible(true);

                //Update the register
                if (notifyHandler != null) notifyHandler.notify(INotifyAction.Action.DELETE, Integer.valueOf(in_id.getText()));
                break;
            }
            case RESULT_REGISTER_NOT_EXISTS:
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

    //INotifyAction Implementation
    @Override
    public void notify(Action action, Object data)
    {
        if (action != Action.SELECT) return;

        //The User has selected an issued book
        cover_select.setVisible(false);

        EntityRegister register = (EntityRegister) data;

        in_id.setText(Integer.toString(register.getId()));
        in_isbn.setText(register.getBook_isbn());
        in_student_id.setText(register.getStudent_id());
        in_days.setText(Integer.toString(register.getDays()));
        in_date.setText(register.getStringDate());

        logClear();
    }
}