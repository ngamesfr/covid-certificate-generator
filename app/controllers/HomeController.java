package controllers;

import java.io.IOException;

import javax.inject.Inject;

import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.*;

public class HomeController extends Controller {
    @Inject
    MessagesApi messagesApi;

    @Inject
    FormFactory formFactory;

    public Result index(Http.Request request) {
        Form<User> userForm = formFactory.form(User.class);
        return ok(views.html.index.render(userForm, request, this.messagesApi.preferred(request)));
    }

    public Result generate(Http.Request request) throws IOException {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest(request);

        if (!userForm.hasErrors()) {
            return ok(new PDFGenerator().generate(userForm.get())).as("application/pdf");
        }

        return badRequest(views.html.index.render(userForm, request, this.messagesApi.preferred(request)));
    }
}
