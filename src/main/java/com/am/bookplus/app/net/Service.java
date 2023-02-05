package com.am.bookplus.app.net;

import com.am.bookplus.app.net.entity.EntityBook;
import com.am.bookplus.app.net.entity.EntityStudent;
import com.am.bookplus.app.net.entity.RegisterData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Service
{
    private Service() { }
    
    /*-------SERVICE-Settings-------*/
    public static final String SERVICE_URL = "http://127.0.0.1";
    public static final int SERVICE_PORT = 8080;
    public static final String SERVICE = SERVICE_URL + ":" + SERVICE_PORT;
    /*------------------------------*/

    /*--------SERVICE-Methods-------*/
    public static Response getBooks(int page, int size) throws IOException, MalformedResponseException
    {
        //------Create Connection------//
        URL url = new URL(SERVICE + "/book/list?page=" + page + "&size=" + size);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.GET_BOOKS);
            return response;
        }
        //-----------------------------//
    }
    public static Response addBook(EntityBook book) throws IOException, MalformedResponseException
    {
        //----Serialize book to JSON---//
        Gson gson = new Gson();
        String json_book = gson.toJson(book);
        //-----------------------------//

        //------Create Connection------//
        URL url = new URL(SERVICE + "/book");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        //-----------------------------//

        //----------Send Data----------//
        try (OutputStream os = con.getOutputStream())
        {
            byte[] input = json_book.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.ADD_BOOK);
            return response;
        }
        //-----------------------------//
    }
    public static Response updateBook(EntityBook book) throws IOException, MalformedResponseException
    {
        //----Serialize book to JSON---//
        Gson gson = new Gson();
        String json_book = gson.toJson(book);
        //-----------------------------//

        //------Create Connection------//
        URL url = new URL(SERVICE + "/book/update");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        //-----------------------------//

        //----------Send Data----------//
        try (OutputStream os = con.getOutputStream())
        {
            byte[] input = json_book.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.UPDATE_BOOK);
            return response;
        }
        //-----------------------------//
    }
    public static Response deleteBook(String isbn) throws IOException, MalformedResponseException
    {
        //------Create Connection------//
        URL url = new URL(SERVICE + "/book?isbn=" + isbn);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.DELETE_BOOK);
            return response;
        }
        //-----------------------------//
    }
    public static Response checkBook(String isbn) throws IOException, MalformedResponseException
    {
        //------Create Connection------//
        URL url = new URL(SERVICE + "/book/check?isbn=" + isbn);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.CHECK_BOOK);
            return response;
        }
        //-----------------------------//
    }
    public static Response findBook(String isbn) throws IOException, MalformedResponseException
    {
        //------Create Connection------//
        URL url = new URL(SERVICE + "/book?isbn=" + isbn);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.FIND_BOOK);
            return response;
        }
        //-----------------------------//
    }
    
    public static Response getStudents(int page, int size) throws IOException, MalformedResponseException
    {
        //------Create Connection------//
        URL url = new URL(SERVICE + "/student/list?page=" + page + "&size=" + size);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.GET_STUDENTS);
            return response;
        }
        //-----------------------------//
    }
    public static Response addStudent(EntityStudent student) throws IOException, MalformedResponseException
    {
        //--Serialize student to JSON--//
        Gson gson = new Gson();
        String json_student = gson.toJson(student);
        //-----------------------------//

        //------Create Connection------//
        URL url = new URL(SERVICE + "/student");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        //-----------------------------//

        //----------Send Data----------//
        try (OutputStream os = con.getOutputStream())
        {
            byte[] input = json_student.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.ADD_STUDENT);
            return response;
        }
        //-----------------------------//
    }
    public static Response updateStudent(EntityStudent student) throws IOException, MalformedResponseException
    {
        //--Serialize student to JSON--//
        Gson gson = new Gson();
        String json_student = gson.toJson(student);
        //-----------------------------//

        //------Create Connection------//
        URL url = new URL(SERVICE + "/student/update");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        //-----------------------------//

        //----------Send Data----------//
        try (OutputStream os = con.getOutputStream())
        {
            byte[] input = json_student.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.UPDATE_STUDENT);
            return response;
        }
        //-----------------------------//
    }
    public static Response deleteStudent(String id) throws IOException, MalformedResponseException
    {
        //------Create Connection------//
        URL url = new URL(SERVICE + "/student?id=" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.DELETE_STUDENT);
            return response;
        }
        //-----------------------------//            
    }
    public static Response findStudent(String id) throws IOException, MalformedResponseException
    {
        //------Create Connection------//
        URL url = new URL(SERVICE + "/student?id=" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.FIND_STUDENT);
            return response;
        }
        //-----------------------------//
    }
    
    public static Response getRegister(int page, int size) throws IOException, MalformedResponseException
    {
        //------Create Connection------//
        URL url = new URL(SERVICE + "/register/list?page=" + page + "&size=" + size);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.GET_REGISTER);
            return response;
        }
        //-----------------------------//            
    }
    public static Response issueBook(RegisterData registerData) throws IOException, MalformedResponseException
    {
        //--Serialize to JSON--//
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String json_data = gson.toJson(registerData);
        //---------------------//

        //------Create Connection------//
        URL url = new URL(SERVICE + "/register");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        //-----------------------------//

        //----------Send Data----------//
        try (OutputStream os = con.getOutputStream())
        {
            byte[] input = json_data.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.ISSUE_BOOK);
            return response;
        }
        //-----------------------------//            
    }
    public static Response returnBook(String id) throws IOException, MalformedResponseException
    {
        //------Create Connection------//
        URL url = new URL(SERVICE + "/register?id=" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        //-----------------------------//

        //--------Read Response--------//
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
        {
            String inputLine;
            StringBuilder str = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                str.append(inputLine);
            }

            Response response = Response.parseResponse(str.toString(), Response.Request.RETURN_BOOK);
            return response;
        }
        //-----------------------------//            
        
    }
    /*------------------------------*/
}