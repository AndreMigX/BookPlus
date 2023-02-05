package com.am.bookplus.app.controller.register;

import com.am.bookplus.app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class RegisterController
{
    @FXML
    public FlowPane card_container;

    @FXML
    public void initialize()
    {
        try
        {
            //Load the "Register" Card
            FXMLLoader loader = new FXMLLoader(App.class.getResource("card-register-view.fxml"));
            Node register_card = loader.load();
            card_container.getChildren().add(register_card);

            RegisterCard registerCardController = loader.getController();

            //Load the "Info Register" Card
            loader = new FXMLLoader(App.class.getResource("card-return-book-view.fxml"));
            Node return_book_card = loader.load();
            card_container.getChildren().add(return_book_card);

            ReturnBookCard returnBookCardController = loader.getController();

            //Load the "Issue a Book" Card
            loader = new FXMLLoader(App.class.getResource("card-issue-book-view.fxml"));
            Node issue_card = loader.load();
            card_container.getChildren().add(issue_card);

            IssueBookCard issueBookCardController = loader.getController();

            //Setup Notify Handlers
            issueBookCardController.setNotifyHandler(registerCardController);
            returnBookCardController.setNotifyHandler(registerCardController);
            registerCardController.setNotifyHandler(returnBookCardController);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
