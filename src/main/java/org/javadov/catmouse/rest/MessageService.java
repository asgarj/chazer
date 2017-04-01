package org.javadov.catmouse.rest;

import org.javadov.catmouse.model.Game;
import org.javadov.catmouse.model.Message;
import org.javadov.catmouse.model.Player;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by asgar on 3/31/17.
 */

@Path("/messages")
public class MessageService {
    private List<Message> messages = new ArrayList<>();

    @Context UriInfo uriInfo;

    @GET
    @Path("/request/{source}/{destination}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestGame(@PathParam("source") int requestorId, @PathParam("destination") int responderId) {
        Response response;
        Player requestor = PlayerService.getPlayerById(requestorId);
        Player responder = PlayerService.getPlayerById(responderId);
        Message message = Message.createMessage(requestor, responder);
        if (message != null) {
            messages.add(message);
            boolean playWithRequestor = message.getResponse();
            if (playWithRequestor) {
                response = Response
                        .temporaryRedirect(UriBuilder.fromPath("/newgame").build())
                        .entity(requestor)
                        .entity(responder)
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
    @Path("/response")
    @Consumes(MediaType.APPLICATION_JSON)
    public void newGame(){

    }

    @GET
    @Path("/inbox/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response inboxStatus(@PathParam("id") int playerId) {
        List<Message> unreadMessage = getReceivedMessages(playerId);
//        Message messageToRespond = unreadMessage.get(0);
//        Player opponent = messageToRespond.getRequestor();
        return Response.ok()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .entity(unreadMessage)
                .build();
    }

    private List<Message> getReceivedMessages(int playerId) {
        List<Message> unreadMessages = this.messages.stream()
                .filter(m -> m.getResponder().getId() == playerId)
                .collect(Collectors.toList());
        return unreadMessages;
    }
}
