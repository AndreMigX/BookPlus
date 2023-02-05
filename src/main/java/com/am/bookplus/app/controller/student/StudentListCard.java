package com.am.bookplus.app.controller.student;

import com.am.bookplus.app.net.Service;
import com.am.bookplus.app.controller.INotifyAction;
import com.am.bookplus.app.net.MalformedResponseException;
import com.am.bookplus.app.net.Response;
import com.am.bookplus.app.net.entity.EntityStudent;
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

public class StudentListCard implements INotifyAction
{
    private static final short PAGE_SIZE = 2;

    private INotifyAction notifyHandler;
    public void setNotifyHandler(INotifyAction notifyHandler) {
        this.notifyHandler = notifyHandler;
    }

    @FXML
    private TableView<EntityStudent> students_table;
    private ObservableList<EntityStudent> student_list;

    @FXML private Node cover_downloading;
    @FXML private Node cover_download_error;

    @FXML
    private Button btn_load_more;

    @FXML
    public void initialize()
    {
        //-------Setup TableView-------//
        TableColumn id_col = new TableColumn("Id");
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn name_col = new TableColumn("Name");
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn surname_col = new TableColumn("Surname");
        surname_col.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn email_col = new TableColumn("Email");
        email_col.setCellValueFactory(new PropertyValueFactory<>("email"));

        students_table.getColumns().addAll(id_col, name_col, surname_col, email_col);
        student_list = FXCollections.observableArrayList();
        students_table.setItems(student_list);

        students_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (notifyHandler != null) notifyHandler.notify(Action.SELECT, newSelection);
            }
        });
        
        students_table.setPlaceholder(new Label("There are no Students"));
        //-----------------------------//

        //--------Get Student List--------//
        net_getStudentList();
        //-----------------------------//
    }

    @FXML
    public void btn_retry_click()
    {
        net_getStudentList();
    }
    @FXML
    public void btn_load_more_click()
    {
        net_getStudentList();
    }
    @FXML
    public void copy_student_id_click()
    {
        if (students_table.getSelectionModel().isEmpty()) return;
        
        Clipboard clipboard = getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(students_table.getSelectionModel().getSelectedItem().getId());
        clipboard.setContent(content);
    }
    @FXML
    public void copy_email_click()
    {
        if (students_table.getSelectionModel().isEmpty()) return;
        
        Clipboard clipboard = getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(students_table.getSelectionModel().getSelectedItem().getEmail());
        clipboard.setContent(content);
    }
    @FXML
    public void refresh_click()
    {
        student_list.clear();
        net_getStudentList();
    }

    private void net_getStudentList()
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
                int page = student_list.size() / PAGE_SIZE;
                //-----------------------------//

                //---------Send Request--------//
                Response response = Service.getStudents(page, PAGE_SIZE);
                //-----------------------------//

                //----------Parse JSON---------//
                JsonObject rootObject = response.payload.getAsJsonObject();
                JsonArray students = rootObject.get("content").getAsJsonArray();

                //Duplicated Students
                int element_to_replace = student_list.size() - page * PAGE_SIZE;

                //Insert new students
                Gson gson = new Gson();
                for (int i = 0; i < students.size(); i++)
                {
                    JsonObject student = students.get(i).getAsJsonObject();
                    student_list.add(gson.fromJson(student, EntityStudent.class));
                }
                
                //Delete duplicated Students
                for (int i = 0; i < element_to_replace; i++)
                {
                    student_list.remove(page * PAGE_SIZE);
                }

                btn_load_more.setDisable(rootObject.get("last").getAsBoolean());
                //-----------------------------//

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        net_getStudentList_result(true);
                    }
                });
            }
            catch (MalformedResponseException | JsonSyntaxException | IOException e)
            {
                //An error occurred
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        net_getStudentList_result(false);
                    }
                });
            }

            return null;
            }
        };
        new Thread(task).start();
    }
    private void net_getStudentList_result(boolean success)
    {
        students_table.refresh();
        
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
                //A new Student has been inserted
                if (!(data instanceof EntityStudent)) break;
                EntityStudent new_student = (EntityStudent) data;

                //Find the position of the new Student
                int index = -1;
                for (int i = 0; i < student_list.size(); i++)
                {
                    if (new_student.getId().compareTo(student_list.get(i).getId()) < 0)
                    {
                        index = i;
                        break;
                    }
                }

                if (index != -1)
                {
                    student_list.add(index, new_student);
                }
                else if (btn_load_more.isDisable())
                {
                    //If the student-table's "load more" button is disabled, then
                    //we have loaded all the students.
                    //So we can safely insert the new Student at the end of the list

                    student_list.add(new_student);
                }

                break;
            }
            case UPDATE:
            {
                //A Student has been updated
                if (!(data instanceof EntityStudent)) break;
                EntityStudent new_student = (EntityStudent) data;

                //Find and update the Student
                for (EntityStudent student : student_list)
                {
                    if (student.getId().equals(new_student.getId()))
                    {
                        student.setName(new_student.getName());
                        student.setSurname(new_student.getSurname());
                        student.setEmail(new_student.getEmail());
                    }
                }

                students_table.refresh();
                break;
            }
            case DELETE:
            {
                //A Student has been deleted
                if (!(data instanceof String)) break;
                String student_id = (String) data;

                //Find and remove the Student
                for (int i = 0; i < student_list.size(); i++)
                {
                    if (student_list.get(i).getId().equals(student_id))
                    {
                        student_list.remove(i);
                        break;
                    }
                }
                break;
            }
        }
    }
}
