package com.am.bookplus.app.controller;

import com.am.bookplus.app.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public enum Page
{
    BOOKS("books-view.fxml"),
    STUDENTS("students-view.fxml"),
    REGISTER("register-view.fxml"),
    SETTINGS("settings-view.fxml");

    private final String page_fxml;
    private Node page_root_node;

    private Page(String page_fxml)
    {
        this.page_fxml = page_fxml;
    }

    public String getFXML()
    {
        return page_fxml;
    }

    public Node getPageNode() throws IOException
    {
        if (page_root_node == null)
        {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(page_fxml));
            page_root_node = loader.load();
        }

        return page_root_node;
    }
}