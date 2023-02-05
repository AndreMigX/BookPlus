package com.am.bookplus.app.controller.book;

import com.am.bookplus.app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class BooksController
{
    @FXML public FlowPane card_container;

    @FXML
    public void initialize()
    {
        try
        {
            //Load the "Book List" Card
            FXMLLoader loader = new FXMLLoader(App.class.getResource("card-book-list-view.fxml"));
            Node book_list_card = loader.load();
            card_container.getChildren().add(book_list_card);

            BookListCard bookListCardController = loader.getController();

            //Load the "Info Book" Card
            loader = new FXMLLoader(App.class.getResource("card-info-book-view.fxml"));
            Node info_book_card = loader.load();
            card_container.getChildren().add(info_book_card);

            InfoBookCard infoBookCardController = loader.getController();

            //Load the "Add Book" Card
            loader = new FXMLLoader(App.class.getResource("card-add-book-view.fxml"));
            Node add_book_card = loader.load();
            card_container.getChildren().add(add_book_card);

            AddBookCard addBookCardController = loader.getController();

            //Load the "Check Book" Card
            loader = new FXMLLoader(App.class.getResource("card-check-book-view.fxml"));
            Node check_book_card = loader.load();
            card_container.getChildren().add(check_book_card);
            
            //Load the "Find Book" Card
            loader = new FXMLLoader(App.class.getResource("card-find-book-view.fxml"));
            Node find_book_card = loader.load();
            card_container.getChildren().add(find_book_card);

            //Setup Notify Handlers
            addBookCardController.setNotifyHandler(bookListCardController);
            infoBookCardController.setNotifyHandler(bookListCardController);
            bookListCardController.setNotifyHandler(infoBookCardController);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}