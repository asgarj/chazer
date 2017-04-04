package org.javadov.catmouse.rest;

import org.javadov.catmouse.model.Message;
import org.javadov.catmouse.model.Player;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Handles game request messages
 * @author asgar on 3/31/17.
 */

@Path("/messages")
public class MessageService {
    private static List<Message> messages = new ArrayList<>();
    private static Logger logger = Logger.getLogger("Chazzer-Game");

    @GET
    @Path("/request/{requestorId}/{responderId}")
    public Response requestGame(@PathParam("requestorId") int requestorId,
                                @PathParam("responderId") int responderId) {
        logger.fine(String.format("reqId:%d, resId:%d", requestorId, responderId));
        Response response;
        Player requestor = PlayerService.getPlayerById(requestorId);
        Player responder = PlayerService.getPlayerById(responderId);
        Message message = Message.createMessage(requestor, responder);
        if (message != null) {
            logger.info("Message created with id: " + message.getMessageId());
            messages.add(message);
            boolean playWithRequestor = message.respond();
            logger.info(String.format("Response for message:%d is %s", message.getMessageId(),
                    playWithRequestor ? "YES" : "NO"));
            if (playWithRequestor) {
                logger.info("playWithRequestor: " + playWithRequestor);
                response = Response
                        .temporaryRedirect(UriBuilder.fromPath("/games/newgame")
                                .path("" + requestorId)
                                .path("" + responderId)
                                .build())
                        .build();
                logger.info(response.toString());
            } else {
                response = Response.status(Response.Status.FORBIDDEN)
                        .header("reason", "The opponent is not able to play at the moment.")
                        .build();
            }
            messages.remove(message);
        } else {
            response = Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
        return response;
    }

    @POST
    @Path("/response/{messageId}/{responderId}")
    public void respondToMessage(@PathParam("messageId") int messageId,
                            @PathParam("responderId") int responderId) {
        logger.info(String.format("Player: %d responded to message: %d", responderId, messageId));
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
