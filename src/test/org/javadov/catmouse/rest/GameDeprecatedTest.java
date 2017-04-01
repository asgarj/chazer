package org.javadov.catmouse.rest;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by asgar on 3/26/17.
 */
public class GameDeprecatedTest {
    private Logger logger;
    @Before
    public void setUp() throws Exception {
        logger = CatMouseTestsHelper.getLogger();
    }

    @Test
    public void newGameForm() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:2005").path("newgame");

        Form form = new Form();
        form.param("name", "Asgar");

        Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(Entity.form(form));
        logger.info(response.toString());
        logger.info(response.getHeaders().toString());
        logger.warning(response.getHeaderString("message"));
    }
    @Test
    public void newGameMultivaluedMap() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:2005").path("newgame");
        System.out.println(target.getUri());

        MultivaluedMap<String, String> form = new MultivaluedHashMap<>(1);
        form.add("name", "Asgar");

        Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(Entity.form(form));
        logger.info(response.toString());
        logger.info(response.getHeaders().toString());
        logger.warning(response.getHeaderString("message"));
    }

}