package com.creppyfm.server.controller;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;

@Controller
@ServerEndpoint(value = "/messages")
public class TestWSGeneric {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Open Connection ...");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Close Connection ...");
    }

    @OnMessage
    public String onMessage(String message) {
        System.out.println("Message from the client: " + message);
        return "Echo from the server : " + message;
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
    }
}