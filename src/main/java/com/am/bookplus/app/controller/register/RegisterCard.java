package com.am.bookplus.app.controller.register;

import com.am.bookplus.app.net.Service;
import com.am.bookplus.app.controller.INotifyAction;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import com.am.bookplus.app.net.entity.EntityRegister;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.text.ParseException;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Callback;

import static javafx.scene.input.Clipboard.getSystemClipboard;

public class RegisterCard implements INotifyAction
{
    private static final short PAGE_SIZE = 2;

    private INotifyAction notifyHandler;
    public void setNotifyHandler(INotifyAction notifyHandler) {
        this.notifyHandler = notifyHandler;
    }

    @FXML
    private TableView<EntityRegister> register_table;
    private ObservableList<EntityRegister> register_list;

    @FXML private Node cover_downloading;
    @FXML private Node cover_download_error;

    @FXML
    private Button btn_load_more;

    @FXML
    public void initialize()
    {
        //-------Setup TableView-------//
        TableColumn id_col = new TableColumn("ID");
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn book_isbn_col = new TableColumn("Book ISBN");
        book_isbn_col.setCellValueFactory(new PropertyValueFactory<>("book_isbn"));

        TableColumn student_id_col = new TableColumn("Student ID");
        student_id_col.setCellValueFactory(new PropertyValueFactory<>("student_id"));

        TableColumn date_col = new TableColumn("Date");
        date_col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EntityRegister, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EntityRegister, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getStringDate());
            }
        });

        TableColumn days_col = new TableColumn("Days");
        days_col.setCellValueFactory(new PropertyValueFactory<>("days"));

        register_table.getColumns().addAll(id_col, book_isbn_col, student_id_col, date_col, days_col);
        register_list = FXCollections.observableArrayList();
        register_table.setItems(register_list);

        register_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (notifyHandler != null) notifyHandler.notify(Action.SELECT, newSelection);
            }
        });
        
        register_table.setPlaceholder(new Label("There are no Issued Books"));
        //-----------------------------//

        //---------Get Register--------//
        net_getRegister();
        //-----------------------------//
    }

    @FXML
    public void btn_retry_click()
    {
        net_getRegister();
    }
    @FXML
    public void btn_load_more_click()
    {
        net_getRegister();
    }
    @FXML
    public void copy_isbn_click()
    {
        if (register_table.getSelectionModel().isEmpty()) return;
        
        Clipboard clipboard = getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(register_table.getSelectionModel().getSelectedItem().getBook_isbn());
        clipboard.setContent(content);
    }
    @FXML
    public void copy_student_id_click()
    {
        if (register_table.getSelectionModel().isEmpty()) return;
        
        Clipboard clipboard = getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(register_table.getSelectionModel().getSelectedItem().getStudent_id());
        clipboard.setContent(content);
    }
    @FXML
    public void refresh_click()
    {
        register_list.clear();
        net_getRegister();
    }

    private void net_getRegister()
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
                int page = register_list.size() / PAGE_SIZE;
                //-----------------------------//

                //---------Send Request--------//
                Response response = Service.getRegister(page, PAGE_SIZE);
                //-----------------------------//

                //----------Parse JSON---------//
                JsonObject rootObject = response.payload.getAsJsonObject();
                JsonArray registerPage = rootObject.get("content").getAsJsonArray();

                //Duplicated values
                int element_to_replace = register_list.size() - page * PAGE_SIZE;

                //Insert new values
                for (int i = 0; i < registerPage.size(); i++)
                {
                    JsonObject value = registerPage.get(i).getAsJsonObject();
                    register_list.add(new EntityRegister(value.get("id").getAsInt(), value.get("book").getAsJsonObject().get("isbn").getAsString(), value.get("student").getAsJsonObject().get("id").getAsString(), value.get("date").getAsString(), value.get("days").getAsInt()));
                }
                
                //Delete duplicated values
                for (int i = 0; i < element_to_replace; i++)
                {
                    register_list.remove(page * PAGE_SIZE);
                }

                btn_load_more.setDisable(rootObject.get("last").getAsBoolean());
                //-----------------------------//

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        net_getRegister_result(true);
                    }
                });
            }
            catch (MalformedResponseException | IOException | ParseException e)
            {
                //An error occurred
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        net_getRegister_result(false);
                    }
                });
            }

            return null;
            }
        };
        new Thread(task).start();
    }
    private void net_getRegister_result(boolean success)
    {
        register_table.refresh();
        
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
                //A new book has been issued
                if (!(data instanceof EntityRegister)) break;
                EntityRegister new_register = (EntityRegister) data;

                //Find the position
                int index = -1;
                for (int i = 0; i < register_list.size(); i++)
                {
                    if (new_register.getId() < register_list.get(i).getId())
                    {
                        index = i;
                        break;
                    }
                }

                if (index != -1)
                {
                    register_list.add(index, new_register);
                }
                else if (btn_load_more.isDisable())
                {
                    //If the register-table's "load more" button is disabled, then
                    //we have loaded all the issued books.
                    //So we can safely insert the new value at the end of the list

                    register_list.add(new_register);
                }

                break;
            }
            case DELETE:
            {
                //A book has been returned
                if (!(data instanceof Integer)) break;
                int register_id = (int) data;

                //Find and remove
                for (int i = 0; i < register_list.size(); i++)
                {
                    if (register_list.get(i).getId() == register_id)
                    {
                        register_list.remove(i);
                        break;
                    }
                }
                break;
            }
        }
    }
}
