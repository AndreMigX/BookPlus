package com.am.bookplus.app.controller.book;

import com.am.bookplus.app.net.Service;
import com.am.bookplus.app.controller.INotifyAction;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import com.am.bookplus.app.net.entity.EntityBook;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import javafx.scene.control.Label;
import static javafx.scene.input.Clipboard.getSystemClipboard;

public class BookListCard implements INotifyAction
{
    private static final short PAGE_SIZE = 2;

    private INotifyAction notifyHandler;
    public void setNotifyHandler(INotifyAction notifyHandler) {
        this.notifyHandler = notifyHandler;
    }

    @FXML
    private TableView<EntityBook> books_table;
    private ObservableList<EntityBook> book_list;

    @FXML private Node cover_downloading;
    @FXML private Node cover_download_error;

    @FXML
    private Button btn_load_more;

    @FXML
    public void initialize()
    {
        //-------Setup TableView-------//
        TableColumn id_col = new TableColumn("ISBN");
        id_col.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn name_col = new TableColumn("Title");
        name_col.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn author_col = new TableColumn("Author");
        author_col.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn quantity_col = new TableColumn("Quantity");
        quantity_col.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        books_table.getColumns().addAll(id_col, name_col, author_col, quantity_col);
        book_list = FXCollections.observableArrayList();
        books_table.setItems(book_list);

        books_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (notifyHandler != null) notifyHandler.notify(Action.SELECT, newSelection);
            }
        });
        
        books_table.setPlaceholder(new Label("There are no Books"));
        //-----------------------------//

        //--------Get Book List--------//
        net_getBookList();
        //-----------------------------//
    }

    @FXML
    public void btn_retry_click()
    {
        net_getBookList();
    }
    @FXML
    public void btn_load_more_click()
    {
        net_getBookList();
    }
    @FXML
    public void copy_isbn_click()
    {
        if (books_table.getSelectionModel().isEmpty()) return;
        
        Clipboard clipboard = getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(books_table.getSelectionModel().getSelectedItem().getIsbn());
        clipboard.setContent(content);
    }
    @FXML
    public void refresh_click()
    {
        book_list.clear();
        net_getBookList();
    }

    private void net_getBookList()
    {
        if (cover_downloading.isVisible()) return;
        cover_download_error.setVisible(false);
        cover_downloading.setVisible(true);

        Task<Void> task = new Task<>()
        {
            @Override
            protected Void call()
            {
            try
            {
                //---Calc the page to request--//
                int page = book_list.size() / PAGE_SIZE;
                //-----------------------------//

                //---------Send Request--------//
                Response response = Service.getBooks(page, PAGE_SIZE);
                //-----------------------------//

                //----------Parse JSON---------//
                JsonObject rootObject = response.payload.getAsJsonObject();
                JsonArray books = rootObject.get("content").getAsJsonArray();

                //Duplicated books
                int element_to_replace = book_list.size() - page * PAGE_SIZE;

                //Insert new books
                Gson gson = new Gson();
                for (int i = 0; i < books.size(); i++)
                {
                    JsonObject book = books.get(i).getAsJsonObject();
                    book_list.add(gson.fromJson(book, EntityBook.class));
                }
                
                //Delete duplicated books
                for (int i = 0; i < element_to_replace; i++)
                {
                    book_list.remove(page * PAGE_SIZE);
                }

                btn_load_more.setDisable(rootObject.get("last").getAsBoolean());
                //-----------------------------//

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        net_getBookList_result(true);
                    }
                });
            }
            catch (MalformedResponseException | JsonSyntaxException | IOException e)
            {
                //An error occurred
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        net_getBookList_result(false);
                    }
                });
            }

            return null;
            }
        };
        new Thread(task).start();
    }
    private void net_getBookList_result(boolean success)
    {
        books_table.refresh();
        
        cover_downloading.setVisible(false);
        cover_download_error.setVisible(!success);
    }

    //INotifyAction Implementation
    @Override
    public void notify(Action action, Object data)
    {
        switch (action)
        {
            case INSERT:
            {
                //A new book has been inserted
                if (!(data instanceof EntityBook)) break;
                EntityBook new_book = (EntityBook) data;

                //Find the position of the new book
                int index = -1;
                for (int i = 0; i < book_list.size(); i++)
                {
                    if (new_book.getIsbn().compareTo(book_list.get(i).getIsbn()) < 0)
                    {
                        index = i;
                        break;
                    }
                }

                if (index != -1)
                {
                    book_list.add(index, new_book);
                }
                else if (btn_load_more.isDisable())
                {
                    //If the book-table's "load more" button is disabled, then
                    //we have loaded all the books.
                    //So we can safely insert the new book at the end of the list

                    book_list.add(new_book);
                }

                break;
            }
            case UPDATE:
            {
                //A book has been updated
                if (!(data instanceof EntityBook)) break;
                EntityBook new_book = (EntityBook) data;

                //Find and update the book
                for (EntityBook book : book_list)
                {
                    if (book.getIsbn().equals(new_book.getIsbn()))
                    {
                        book.setTitle(new_book.getTitle());
                        book.setAuthor(new_book.getAuthor());
                        book.setQuantity(new_book.getQuantity());
                    }
                }

                books_table.refresh();
                break;
            }
            case DELETE:
            {
                //A book has been deleted
                if (!(data instanceof String)) break;
                String book_isbn = (String) data;

                //Find and remove the book
                for (int i = 0; i < book_list.size(); i++)
                {
                    if (book_list.get(i).getIsbn().equals(book_isbn))
                    {
                        book_list.remove(i);
                        break;
                    }
                }
                break;
            }
        }
    }
}
