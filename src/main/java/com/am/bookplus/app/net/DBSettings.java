package com.am.bookplus.app.net;

public class DBSettings
{
    private DBSettings() { }

    public static final String DB_USER = "root", DB_PASSWORD = "root", DB_NAME = "bookplus";
    public static final int DB_PORT = 3306;

    public static final String DB_URL = "jdbc:mysql://127.0.0.1:" + DB_PORT + "?allowMultiQueries=true";

    public static final String CREATE_QUERY =
            "CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "`;\n" +
            "USE `" + DB_NAME + "`;\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS `book` (\n" +
            "  `isbn` varchar(13) NOT NULL,\n" +
            "  `author` varchar(255) DEFAULT NULL,\n" +
            "  `quantity` int DEFAULT NULL,\n" +
            "  `title` varchar(255) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`isbn`)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS `student` (\n" +
            "  `id` VARCHAR(6) NOT NULL,\n" +
            "  `email` varchar(255) DEFAULT NULL,\n" +
            "  `name` varchar(255) DEFAULT NULL,\n" +
            "  `surname` varchar(255) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS `register` (\n" +
            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
            "  `date` datetime(6) DEFAULT NULL,\n" +
            "  `date_return` datetime(6) DEFAULT NULL,\n" +
            "  `days` int DEFAULT NULL,\n" +
            "  `book_isbn` varchar(13) DEFAULT NULL,\n" +
            "  `student_id` VARCHAR(6) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `FKaau6mtt78x4c0k275nw6iomgh` (`book_isbn`),\n" +
            "  KEY `FKfrcjj9h7yxane57ol6i1mfa46` (`student_id`),\n" +
            "  CONSTRAINT `FKaau6mtt78x4c0k275nw6iomgh` FOREIGN KEY (`book_isbn`) REFERENCES `book` (`isbn`),\n" +
            "  CONSTRAINT `FKfrcjj9h7yxane57ol6i1mfa46` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)\n" +
            ");";

    public static final String INSERT_TEST_DATA_QUERY =
            "USE `" + DB_NAME + "`;\n" +
            "DELETE FROM register;\n" +
            "DELETE FROM book;\n" +
            "INSERT INTO book (isbn, author, title, quantity) VALUES ('9783127323207', 'Francis Scott Fitzgerald', 'Il grande Gatsby', 5);\n" +
            "INSERT INTO book (isbn, author, title, quantity) VALUES ('9793127323206', 'Albert Camus', 'Lo straniero', 2);\n" +
            "INSERT INTO book (isbn, author, title, quantity) VALUES ('9793237323202', 'Marcel Proust', 'Alla ricerca del tempo perduto', 1);\n" +
            "INSERT INTO book (isbn, author, title, quantity) VALUES ('9793437323200', 'Franz Kafka', 'Il processo', 3);\n" +
            "INSERT INTO book (isbn, author, title, quantity) VALUES ('9793337323201', 'Antoine de Saint-Exupery', 'Il piccolo principe', 6);\n" +
            "DELETE FROM student;\n" +
            "INSERT INTO student (id, name, surname, email) VALUES ('815564', 'Andrea', 'Migliore', 'a.migliore9@uni.it');\n" +
            "INSERT INTO student (id, name, surname, email) VALUES ('815565', 'Mario', 'Rossi', 'm.rossi5@uni.it');\n" +
            "INSERT INTO student (id, name, surname, email) VALUES ('815566', 'Luigi', 'Bianchi', 'l.bianchi2@uni.it');\n" +
            "INSERT INTO student (id, name, surname, email) VALUES ('815570', 'Giovanni', 'Neri', 'g.neri@uni.it');\n" +
            "INSERT INTO register (book_isbn, student_id, date, days) VALUES ('9783127323207', '815564', '2023-01-01T23:00:00.000+00:00', 5);\n" +
            "INSERT INTO register (book_isbn, student_id, date, days) VALUES ('9793437323200', '815566', '2023-01-19T23:00:00.000+00:00', 2);";
}
