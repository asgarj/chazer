package org.javadov.catmouse.rest;

import org.javadov.catmouse.model.Message;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by ajavadov on 4/1/2017.
 */
public class MessageServiceTest {
    final String BASE_URL = "http://localhost:2005";
    final String PATH = "messages";

    @Test
    @Ignore
    public void requestGameTest() {
        //CatMouseTestsHelper.addDummyPlayers();
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(UriBuilder.fromUri("http://localhost:2005"))
                .path("messages")
                .path("inbox")
                .path("2");
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
        List<Message> messages = response.readEntity(List.class);
//        assertTrue(messages.isEmpty());

        webTarget = client.target(UriBuilder.fromUri(BASE_URL))
                .path(PATH)
                .path("request")
                .path("1")
                .path("2");
        long start = System.currentTimeMillis();
        response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000.0);

        webTarget = client.target(UriBuilder.fromUri("http://localhost:2005"))
                .path("messages")
                .path("inbox")
                .path("2");
        webTarget.request().get();
        System.out.println((System.currentTimeMillis() - end) / (double)1000);
        //response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
        //messages = response.readEntity(List.class);
        //System.out.println(MessageService.messages);
        //assertEquals(1, messages.size());
    }

    @Test
    public void testInbox() {
        WebTarget webTarget = ClientBuilder.newClient().target(BASE_URL).path(PATH).path("inbox").path("1");
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
        List<LinkedHashMap> inbox = response.readEntity(List.class);
        System.out.println(inbox.size());
        System.out.println(inbox.isEmpty());
        int mId = (int) inbox.get(0).get("messageId");
        System.out.println(mId);
    }
}
