package nl.svendubbeld.fontys.websockets;

import nl.svendubbeld.fontys.auth.Secured;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint("/ws/events")
public class WebSocketServer {

    @Inject
    private SessionHandler sessionHandler;

    @OnOpen
    @Secured
    public void open(Session session) {
        sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void error(Throwable error) {

    }

    @OnMessage
    public void handleMessage(String message, Session session) {

    }
}
