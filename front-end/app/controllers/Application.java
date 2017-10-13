package controllers;

/**
 * Created by skylai on 2017/10/13.
 */

import play.mvc.Controller;
import play.*;
import play.mvc.*;
import views.html.index;

public class Application extends Controller {

    public Result index() {
        return ok(views.html.index.render());
    }
}
