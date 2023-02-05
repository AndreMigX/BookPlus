package com.am.bookplus.app.controller;

public interface INotifyAction
{
    enum Action
    {
        INSERT,
        UPDATE,
        SELECT,
        DELETE;
    }
    void notify(Action action, Object data);
}