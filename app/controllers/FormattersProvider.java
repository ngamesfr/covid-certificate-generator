package controllers;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import play.data.format.Formatters;
import play.data.format.Formatters.SimpleFormatter;
import play.i18n.MessagesApi;

@Singleton
public class FormattersProvider implements Provider<Formatters> {
  private static final DateTimeFormatter PRINT_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private static final DateTimeFormatter PARSE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private final MessagesApi messagesApi;

  @Inject
  public FormattersProvider(MessagesApi messagesApi) {
    this.messagesApi = messagesApi;
  }

  @Override
  public Formatters get() {
    Formatters formatters = new Formatters(messagesApi);

    formatters.register(
        LocalDate.class,
        new SimpleFormatter<LocalDate>() {
          @Override
          public LocalDate parse(String input, Locale l) throws ParseException {
            return LocalDate.parse(input, PARSE_PATTERN);
          }

          @Override
          public String print(LocalDate localDate, Locale l) {
            return localDate.format(PRINT_PATTERN);
          }
        });

    return formatters;
  }
}