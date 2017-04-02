package org.javadov.catmouse.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by asgar on 3/31/17.
 */

@XmlRootElement
public class Message {
    private static int counter = 0;
    private final int messageId;
    Player requestor;
    Player responder;
    OpponentResponse response;

    private Message(Player requestor, Player receiver) {
        this.messageId = ++counter;
        this.requestor = requestor;
        this.responder = receiver;
        this.response = OpponentResponse.NONE;
    }

    public static Message createMessage(Player requestor, Player responder) {
        if (requestor != null && responder != null)
            return new Message(requestor, responder);
        else return null;
    }

    public int getMessageId() {
        return messageId;
    }

    public Player getRequestor() {
        return requestor;
    }

    public Player getResponder() {
        return responder;
    }

    public boolean respond() {
        CompletableFuture<Void> res = CompletableFuture.runAsync(() -> {
            while (response == OpponentResponse.NONE)
                try {
                    TimeUnit.MILLISECONDS.sleep(200L);
                } catch (InterruptedException e) {}

        });
        try {
            res.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//            e.printStackTrace();
        }
        return response == OpponentResponse.ACCEPTED;
    }

    public void setResponse(boolean wantToPlay) {
        response = wantToPlay ? OpponentResponse.ACCEPTED : OpponentResponse.REJECTED;
    }
    private enum OpponentResponse {
        NONE, ACCEPTED, REJECTED;
    }
}
