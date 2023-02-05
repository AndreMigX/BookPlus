package com.am.bookplus.app.controller.book;

import com.am.bookplus.app.Utils;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import static com.am.bookplus.app.net.Response.ResultCode.RESULT_ERROR;
import static com.am.bookplus.app.net.Response.ResultCode.RESULT_OK;
import com.am.bookplus.app.net.Service;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CheckBookCard
{
    @FXML private TextField in_isbn;
    @FXML private TextField in_quantity;

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
    }

    @FXML
    public void btn_add_click()
    {
        //Check input
        String isbn = in_isbn.getText().trim();

        if (!Utils.checkISBN(isbn))
        {
            in_isbn.getStyleClass().add("error");

            logError("ISBN not valid");
            return;
        }

        net_checkBook(isbn);
    }

    private void net_checkBook(String isbn)
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
                    Response response = Service.checkBook(isbn);
                    
                    if (response.code == RESULT_OK)
                    {
                        int books_left = response.payload.getAsInt();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                net_checkBook_result(response.code, books_left);
                            }
                        });
                        
                        return null;
                    }
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_checkBook_result(response.code, -1);
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
                            net_checkBook_result(RESULT_ERROR, -1);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_checkBook_result(Response.ResultCode code, int books_left)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                //Update Log
                logClear();

                //Show
                in_quantity.setText(Integer.toString(books_left));

                break;
            }
            case RESULT_BOOK_NOT_EXISTS:
            {
                //Update Log
                logError("ISBN not found");
                
                in_quantity.clear();
                break;
            }
            case RESULT_ERROR:
            {
                //Update Log
                logError("An error occurred");
                
                in_quantity.clear();
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
        
        in_quantity.clear();
        
        logClear();
    }
}
