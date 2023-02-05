package com.am.bookplus.app.controller.book;

import com.am.bookplus.app.Utils;
import com.am.bookplus.app.net.Service;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import static com.am.bookplus.app.net.Response.ResultCode.RESULT_ERROR;
import static com.am.bookplus.app.net.Response.ResultCode.RESULT_OK;
import com.am.bookplus.app.net.entity.EntityBook;
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

public class FindBookCard
{
    @FXML private TextField in_title;
    @FXML private TextField in_author;
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
    public void btn_find_click()
    {
        //Check input
        String isbn = in_isbn.getText().trim();

        if (!Utils.checkISBN(isbn))
        {
            in_isbn.getStyleClass().add("error");

            logError("ISBN not valid");
            return;
        }

        net_findBook(isbn);
    }

    private void net_findBook(String isbn)
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
                    Response response = Service.findBook(isbn);
                    
                    if (response.code == RESULT_OK)
                    {
                        Gson gson = new Gson();
                        EntityBook book = gson.fromJson(response.payload, EntityBook.class);
                        
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                net_findBook_result(response.code, book);
                            }
                        });
                        
                        return null;
                    }
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_findBook_result(response.code, null);
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
                            net_findBook_result(RESULT_ERROR, null);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_findBook_result(Response.ResultCode code, EntityBook book)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                //Update Log
                logClear();

                //Show
                in_title.setText(book.title);
                in_author.setText(book.author);
                in_quantity.setText(Integer.toString(book.quantity));
                
                break;
            }
            case RESULT_BOOK_NOT_EXISTS:
            {
                //Update Log
                logError("ISBN not found");
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
    
    public void in_isbn_change()
    {
        in_isbn.getStyleClass().removeAll("error");
        
        in_title.clear();
        in_author.clear();
        in_quantity.clear();
        
        logClear();
    }
}
