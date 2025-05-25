package org.creche;

import spark.Filter;
import spark.Request;
import spark.Response;
import static spark.Spark.halt;

public class AuthFilter implements Filter {
    @Override
    public void handle(Request request, Response response) throws Exception {
        if (request.session().attribute("user") == null) {
            response.redirect("/login.html");
            halt();
        }
    }
}
