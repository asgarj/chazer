package org.javadov.catmouse.model;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by asgar on 3/31/17.
 */
public class Message {
    Player requestor;
    Player responder;
    OpponentResponse response;

    private Message(Player requestor, Player receiver) {
        this.requestor = requestor;
        this.responder = receiver;
        this.response = OpponentResponse.NONE;
    }

    public static Message createMessage(Player requestor, Player responder) {
        if (requestor != null && responder != null)
            return new Message(requestor, responder);
        else return null;
    }

    public Player getRequestor() {
        return requestor;
    }

    public Player getResponder() {
        return responder;
    }

    public boolean getResponse() {
        CompletableFuture<Void> res = CompletableFuture.runAsync(() -> {
            while (response == OpponentResponse.NONE)
                try {
                    TimeUnit.MILLISECONDS.sleep(200L);
                } catch (InterruptedException e) {}

        });
        try {
            res.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return response == OpponentResponse.ACCEPTED;
    }
    private enum OpponentResponse {
        NONE, ACCEPTED, REJECTED;
    }
}
