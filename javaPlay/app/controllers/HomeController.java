package controllers;

import play.mvc.*;

// add new Views file in package Home
import views.html.Home.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    public Result about() {
        return ok("this is about page!");
    }

    //                    acc parameter
    public Result welcome(String name) {
        return ok("Welcome, " + name + "!");
    }

    public Result greetings(String name, String lastName) {
        //        path ---                rendering views from controller
        return ok(views.html.Home.welcome.render(name, lastName));
    }
}
