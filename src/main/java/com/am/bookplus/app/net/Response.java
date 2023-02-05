package com.am.bookplus.app.net;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

public class Response
{
    public enum Request
    {
        GET_BOOKS,
        GET_STUDENTS,
        GET_REGISTER,
        ADD_BOOK,
        ADD_STUDENT,
        ISSUE_BOOK,
        DELETE_BOOK,
        DELETE_STUDENT,
        RETURN_BOOK,
        UPDATE_BOOK,
        UPDATE_STUDENT,
        CHECK_BOOK,
        FIND_BOOK,
        FIND_STUDENT
    }
    public enum ResultCode
    {
        RESULT_OK,
        RESULT_ERROR,
        RESULT_BOOK_NOT_EXISTS,
        RESULT_BOOK_ALREADY_EXISTS,
        RESULT_BOOK_NOT_AVAILABLE,
        RESULT_ISBN_NOT_VALID,
        RESULT_CANT_DELETE_BOOK,
        RESULT_QUANTITY_NOT_VALID,
        RESULT_STUDENT_NOT_EXISTS,
        RESULT_STUDENT_ALREADY_EXISTS,
        RESULT_ID_NOT_VALID,
        RESULT_EMAIL_NOT_VALID,
        RESULT_CANT_DELETE_STUDENT,
        RESULT_REGISTER_NOT_EXISTS
    }

    public Request request;
    public ResultCode code;
    public JsonElement payload;

    public Response() {}
    public Response(ResultCode code, Request request, JsonElement payload)
    {
        this.code = code;
        this.request = request;
        this.payload = payload;
    }
    public Response(ResultCode code, Request request)
    {
        this.code = code;
        this.request = request;
    }

    public static Response parseResponse(String json, Request check) throws MalformedResponseException
    {
        //System.out.println(json);

        try
        {
            Gson gson = new Gson();
            Response response = gson.fromJson(json, Response.class);

            if (response.request != check) throw new MalformedResponseException();               
            return response;
        }
        catch(JsonSyntaxException e)
        {
            throw new MalformedResponseException();
        }
    }
}