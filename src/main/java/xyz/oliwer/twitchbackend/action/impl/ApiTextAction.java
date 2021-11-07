package xyz.oliwer.twitchbackend.action.impl;

import com.jsoniter.Jsoniter;
import xyz.oliwer.twitchbackend.action.PatternTextAction;
import xyz.oliwer.twitchbackend.action.TextAction;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * This class represents the "API" implementation of {@link PatternTextAction}.
 * <br/>
 * Fetch <b>JSON</b> properties from an <b>Endpoint</b> by passed link & target (if any).
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class ApiTextAction extends PatternTextAction {
  /** @see PatternTextAction#PatternTextAction(TextAction.Parser) **/
  public ApiTextAction(TextAction.Parser<PatternTextAction> parser) {
    super(parser);
  }

  /** @see PatternTextAction#parse(String, String...) **/
  @Override
  public Object parse(String origin, String... parameters) {
    // read content of page
    try (final InputStream stream = new URL(parameters[0]).openStream();
         final Scanner scanner = new Scanner(stream)) {
      // builder to append content to
      final StringBuilder builder = new StringBuilder();

      // loop over and append content
      while (scanner.hasNext())
        builder.append(scanner.next());

      // prepare for extraction
      final Jsoniter object = Jsoniter.parse(builder.toString());
      final String[] paths = parameters[1].split("\\.");
      final int lastIndex = paths.length - 1;

      // extract content
      Object next = null;
      Object value = null;
      for (int index = 0; index < paths.length; index++) {
        if (value != null)
          return origin;

        final String path = paths[index];
        final Object fromPath;

        if (index == 0)
          fromPath = object.readAny().get(path);
        else {
          final Class<?> nextClass = next.getClass();
          if (Map.class.isAssignableFrom(nextClass))
            fromPath = ((Map<String, Object>) next).get(path);
          else if (List.class.isAssignableFrom(nextClass))
            fromPath = ((List<Object>) next).get(indexFromSpec(path));
          else if (nextClass.isArray())
            fromPath = ((Object[]) next)[indexFromSpec(path)];
          else return origin;
        }

        if (index != lastIndex) {
          next = fromPath;
        } else {
          value = fromPath;
        }
      }

      // return value
      return value;
    } catch (Exception ignored) {}

    // an exception was caught and has relinquished the url - return the origin
    return origin;
  }

  /** @see PatternTextAction#tag() **/
  @Override
  public String tag() {
    return "api";
  }

  /** @see PatternTextAction#separator() **/
  @Override
  public char separator() {
    return ',';
  }

  /**
   * <b>Example:</b> [index]
   * @return {@link Integer}
   */
  private static int indexFromSpec(String spec) {
    return parseInt(spec.substring(1, spec.length() - 1));
  }
}