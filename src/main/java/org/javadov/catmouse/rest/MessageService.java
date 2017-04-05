package org.javadov.catmouse.rest;

import org.javadov.catmouse.model.Game;
import org.javadov.catmouse.model.Message;
import org.javadov.catmouse.model.Player;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static java.lang.String.format;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
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
        Response response;
        Player requestor = PlayerService.getPlayerById(requestorId);
        Player responder = PlayerService.getPlayerById(responderId);
        Message message = Message.createMessage(requestor, responder);
        if (message != null) {
            logger.log(Level.FINE, format("Message created [%d]", message.getMessageId()));
            messages.add(message);
            boolean playWithRequestor = message.respond();
            if (playWithRequestor) {
                logger.log(Level.FINE, format("CHALLENGE [%d] ACCEPTED!", message.getMessageId()));
                response = Response
                        .temporaryRedirect(UriBuilder.fromPath("/games/new")
                                .path("" + message.getMessageId())
                                .build())
                        .build();
            } else {
                logger.log(Level.FINE, format("CHALLENGE [%d] REJECTED!", message.getMessageId()));
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
    @Path("/respond/{messageId}/{responderId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response respondToMessage(@PathParam("messageId") int messageId,
                            @PathParam("responderId") int responderId) {
        List<Message> receivedMessages = getReceivedMessages(responderId);
        Optional<Message> message = receivedMessages.stream().filter(m -> m.getMessageId() == messageId).findFirst();
        if (message.isPresent()) {
            message.get().setResponse(true);
            Game newgame = GameService.getGameByMessageId(messageId);
            if (newgame != null) {
                return Response.ok(newgame.getId(), MediaType.TEXT_PLAIN_TYPE).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
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
        return messages.stream()
                .filter(m -> m.getResponder().getId() == playerId)
                .collect(Collectors.toList());
    }

    public static Message getMessageById(int id) {
        return messages.stream().filter(m -> m.getMessageId() == id).findFirst().get();
    }
}
