package org.javadov.catmouse.rest;

import org.javadov.catmouse.model.Message;
import org.javadov.catmouse.model.Player;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles game request messages
 * @author asgar on 3/31/17.
 */

@Path("/messages")
public class MessageService {
    private static List<Message> messages = new ArrayList<>();

    @GET
    @Path("/request/{requestorId}/{responderId}")
    public Response requestGame(@PathParam("requestorId") int requestorId,
                                @PathParam("responderId") int responderId) {
        Response response;
        Player requestor = PlayerService.getPlayerById(requestorId);
        Player responder = PlayerService.getPlayerById(responderId);
        Message message = Message.createMessage(requestor, responder);
        if (message != null) {
            messages.add(message);
            boolean playWithRequestor = message.respond();
            if (playWithRequestor) {
                response = Response
                        .temporaryRedirect(UriBuilder.fromPath("/newgame")
                                .path("" + requestorId)
                                .path("" + responderId)
                                .build())
                        .build();
            } else {
                response = Response.status(Response.Status.FORBIDDEN)
                        .header("reason", "The opponent is not able to play at the moment.")
                        .build();
            }
        } else {
            response = Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
        return response;
    }

    @POST
    @Path("/response/{messageId}/{responderId}")
    public void respondToMessage(@PathParam("messageId") int messageId,
                            @PathParam("responderId") int responderId) {
        List<Message> receivedMessages = getReceivedMessages(responderId);
        receivedMessages.forEach(message -> message.setResponse(message.getMessageId() == messageId));
    }

    @GET
    @Path("/inbox/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response inboxStatus(@PathParam("id") int playerId) {
        List<Message> receivedMessages = getReceivedMessages(playerId);
        return Response.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .entity(receivedMessages)
                .build();
    }

    private List<Message> getReceivedMessages(int playerId) {
        List<Message> unreadMessages = messages.stream()
                .filter(m -> m.getResponder().getId() == playerId)
                .collect(Collectors.toList());
        return unreadMessages;
    }
}
