package org.javadov.catmouse.rest;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by asgar on 3/29/17.
 */
public class CatMouseTestsHelper {
    public static final String FILENAME = "game.log";
    private static Logger logger = Logger.getLogger("Cat-Mouse-GameDeprecated");
    static {
        try {
            logger.addHandler(new FileHandler(FILENAME, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
