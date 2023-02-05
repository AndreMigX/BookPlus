module com.am.bookplus.app.bookplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;

    opens com.am.bookplus.app to javafx.fxml;
    exports com.am.bookplus.app;
    exports com.am.bookplus.app.controller;
    exports com.am.bookplus.app.net.entity;
    opens com.am.bookplus.app.controller to javafx.fxml;

    opens com.am.bookplus.app.net.entity to javafx.base;

    exports com.am.bookplus.app.controller.book;
    opens com.am.bookplus.app.controller.book to javafx.fxml;
    exports com.am.bookplus.app.controller.student;
    opens com.am.bookplus.app.controller.student to javafx.fxml;
    exports com.am.bookplus.app.controller.register;
    opens com.am.bookplus.app.controller.register to javafx.fxml;
    exports com.am.bookplus.app.controller.settings;
    opens com.am.bookplus.app.controller.settings to javafx.fxml;
    exports com.am.bookplus.app.net;
    opens com.am.bookplus.app.net to javafx.fxml;
}