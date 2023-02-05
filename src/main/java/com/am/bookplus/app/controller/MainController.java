package com.am.bookplus.app.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainController
{
    Page current_page;

    @FXML
    private Button btn_books;
    @FXML
    private Button btn_members;
    @FXML
    private Button btn_register;
    @FXML
    private Button btn_settings;

    @FXML
    private VBox main_container;

    @FXML
    public void initialize()
    {
        show_page(Page.BOOKS);
    }

    @FXML
    public void btn_books_click()
    {
        show_page(Page.BOOKS);
    }
    @FXML
    public void btn_members_click()
    {
        show_page(Page.STUDENTS);
    }
    @FXML
    public void btn_register_click()
    {
        show_page(Page.REGISTER);
    }
    @FXML
    public void btn_settings_click()
    {
        show_page(Page.SETTINGS);
    }

    private boolean show_page(Page page)
    {
        if (current_page == page) return false;

        try
        {
            //Show the Page's FXML
            Node page_node = page.getPageNode();

            main_container.getChildren().clear();
            main_container.getChildren().add(page_node);
        }
        catch (IOException ignore)
        {
            return false;
        }

        //Unselect the current page's button
        if (current_page != null)
        {
            switch (current_page)
            {
                case BOOKS:
                    btn_books.getStyleClass().remove("selected");
                    break;
                case STUDENTS:
                    btn_members.getStyleClass().remove("selected");
                    break;
                case REGISTER:
                    btn_register.getStyleClass().remove("selected");
                    break;
                case SETTINGS:
                    btn_settings.getStyleClass().remove("selected");
                    break;
            }
        }

        //Select the new page's button
        switch (page)
        {
            case BOOKS:
                btn_books.getStyleClass().add("selected");
                break;
            case STUDENTS:
                btn_members.getStyleClass().add("selected");
                break;
            case REGISTER:
                btn_register.getStyleClass().add("selected");
                break;
            case SETTINGS:
                btn_settings.getStyleClass().add("selected");
                break;
        }

        //Update the Current Page
        current_page = page;

        return true;
    }
}