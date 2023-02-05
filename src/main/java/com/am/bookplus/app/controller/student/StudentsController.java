package com.am.bookplus.app.controller.student;

import com.am.bookplus.app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class StudentsController
{
    @FXML public FlowPane card_container;

    @FXML
    public void initialize()
    {
        try
        {
            //Load the "Student List" Card
            FXMLLoader loader = new FXMLLoader(App.class.getResource("card-student-list-view.fxml"));
            Node students_list_card = loader.load();
            card_container.getChildren().add(students_list_card);

            StudentListCard studentListCardController = loader.getController();

            //Load the "Info Student" Card
            loader = new FXMLLoader(App.class.getResource("card-info-student-view.fxml"));
            Node info_student_card = loader.load();
            card_container.getChildren().add(info_student_card);

            InfoStudentCard infoStudentCardController = loader.getController();

            //Load the "Add Student" Card
            loader = new FXMLLoader(App.class.getResource("card-add-student-view.fxml"));
            Node add_student_card = loader.load();
            card_container.getChildren().add(add_student_card);

            AddStudentCard addStudentCardController = loader.getController();
            
            //Load the "Find Student" Card
            loader = new FXMLLoader(App.class.getResource("card-find-student-view.fxml"));
            Node find_student_card = loader.load();
            card_container.getChildren().add(find_student_card);

            //Setup Notify Handlers
            addStudentCardController.setNotifyHandler(studentListCardController);
            infoStudentCardController.setNotifyHandler(studentListCardController);
            studentListCardController.setNotifyHandler(infoStudentCardController);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}