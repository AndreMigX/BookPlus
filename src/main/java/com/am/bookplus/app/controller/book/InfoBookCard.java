package com.am.bookplus.app.controller.book;

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

public class InfoBookCard implements INotifyAction
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
    @FXML private Node cover_select;

    @FXML private Label lbl_log;

    @FXML
    public void initialize()
    {
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
    public void btn_save_click()
    {
        //Check input
        boolean input_error = false;

        String title = in_title.getText().trim();
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

        net_saveBook(in_isbn.getText(), title, author, quantity);
    }
    @FXML
    public void btn_delete_click()
    {
        net_deleteBook();
    }

    private void net_deleteBook()
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
                    Response response = Service.deleteBook(in_isbn.getText());

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_deleteBook_result(response.code);
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
                            net_deleteBook_result(RESULT_ERROR);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_deleteBook_result(Response.ResultCode code)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                cover_select.setVisible(true);

                //Update the book list
                if (notifyHandler != null) notifyHandler.notify(INotifyAction.Action.DELETE, in_isbn.getText());
                break;
            }
            case RESULT_BOOK_NOT_EXISTS:
            {
                //Update Log
                logError("ISBN not found");
                break;
            }
            case RESULT_CANT_DELETE_BOOK:
            {
                //Update Log
                logError("Can't delete this book");
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

    private void net_saveBook(String isbn, String title, String author, int quantity)
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
                    Response response = Service.updateBook(book);
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            net_saveBook_result(response.code, book);
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
                            net_saveBook_result(RESULT_ERROR, null);
                        }
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
    private void net_saveBook_result(Response.ResultCode code, EntityBook new_book)
    {
        cover_wait.setVisible(false);

        switch (code)
        {
            case RESULT_OK:
            {
                //Update Log
                log("Book saved");

                //Update the book list
                if (notifyHandler != null) notifyHandler.notify(INotifyAction.Action.UPDATE, new_book);
                break;
            }
            case RESULT_BOOK_NOT_EXISTS:
            {
                //Update Log
                logError("ISBN not found");
                break;
            }
            case RESULT_QUANTITY_NOT_VALID:
            {
                //Update Log
                logError("Quantity not valid");
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
    public void in_quantity_change()
    {
        in_quantity.getStyleClass().removeAll("error");
        logClear();
    }

    //INotifyAction Implementation
    @Override
    public void notify(Action action, Object data)
    {
        if (action != Action.SELECT) return;

        //The User has selected a book from the list
        cover_select.setVisible(false);

        EntityBook book = (EntityBook) data;

        in_isbn.setText(book.getIsbn());
        in_title.setText(book.getTitle());
        in_author.setText(book.getAuthor());
        in_quantity.setText(Integer.toString(book.getQuantity()));

        in_title.getStyleClass().removeAll("error");
        in_author.getStyleClass().removeAll("error");
        in_quantity.getStyleClass().removeAll("error");

        logClear();
    }
}