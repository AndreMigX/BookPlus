package com.am.bookplus.app.controller.book;

import com.am.bookplus.app.Utils;
import com.am.bookplus.app.net.Service;
import com.am.bookplus.app.controller.INotifyAction;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import static com.am.bookplus.app.net.Response.ResultCode.RESULT_ERROR;
import com.am.bookplus.app.net.entity.EntityBook;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddBookCard
{
    private INotifyAction notifyHandler;
    public void setNotifyHandler(INotifyAction notifyHandler) {
        this.notifyHandler = notifyHandler;
    }

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
        in_quantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*")) {
                    in_quantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    public void btn_add_click()
    {
        //Check input
        boolean input_error = false;

        String title = in_title.getText().trim();
        String isbn = in_isbn.getText().trim();
        String author = in_author.getText().trim();
        int quantity = in_quantity.getText().equals("") ? -1 : Integer.parseUnsignedInt(in_quantity.getText());

        if (title.length() == 0)
        {
            input_error = true;
            in_title.getStyleClass().add("error");
        }
        if (author.length() == 0)
        {
            input_error = true;
            in_author.getStyleClass().add("error");
        }
        if (!Utils.checkISBN(isbn))
        {
            input_error = true;
            in_isbn.getStyleClass().add("error");
        }
        if (quantity < 0)
        {
            input_error = true;
            in_quantity.getStyleClass().add("error");
        }
        if (input_error)
        {
            logError("Some info are incorrect");
            return;
        }

        net_addBook(isbn, title, author, quantity);
    }

    private void net_addBook(String isbn, String title, String author, int quantity)
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
                    //------Create a new Book------//
                    EntityBook book = new EntityBook(isbn, title, author, quantity);
                    //-----------------------------//
                    
                    //---------Send Request--------//
                    Response response = Service.addBook(book);
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_addBook_result(response.code, book);
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
                            net_addBook_result(RESULT_ERROR, null);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_addBook_result(Response.ResultCode code, EntityBook new_book)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                //Clear form
                in_title.clear();
                in_isbn.clear();
                in_author.clear();
                in_quantity.clear();

                //Update Log
                log("Book saved");

                //Update the book list
                if (notifyHandler != null) notifyHandler.notify(INotifyAction.Action.INSERT, new_book);
                break;
            }
            case RESULT_BOOK_ALREADY_EXISTS:
            {
                //Update Log
                logError("ISBN already registered");
                break;
            }
            case RESULT_ISBN_NOT_VALID:
            {
                //Update Log
                logError("ISBN not valid");
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
    public void in_title_change()
    {
        in_title.getStyleClass().removeAll("error");
        logClear();
    }
    @FXML
    public void in_author_change()
    {
        in_author.getStyleClass().removeAll("error");
        logClear();
    }
    @FXML
    public void in_isbn_change()
    {
        in_isbn.getStyleClass().removeAll("error");
        logClear();
    }
    @FXML
    public void in_quantity_change()
    {
        in_quantity.getStyleClass().removeAll("error");
        logClear();
    }
}
