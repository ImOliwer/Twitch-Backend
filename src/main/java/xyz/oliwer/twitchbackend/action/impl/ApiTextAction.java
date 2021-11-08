package xyz.oliwer.twitchbackend.action.impl;

import com.jsoniter.any.Any;
import xyz.oliwer.twitchbackend.action.PatternTextAction;
import xyz.oliwer.twitchbackend.action.TextAction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.jsoniter.JsonIterator.deserialize;
import static com.jsoniter.output.JsonStream.serialize;
import static java.lang.Integer.parseInt;
import static java.net.http.HttpRequest.BodyPublisher;
import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpResponse.BodyHandlers;
import static java.nio.charset.StandardCharsets.UTF_8;

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
    // handle request
    try {
      // create the client & request
      final HttpClient client = HttpClient.newHttpClient();
      final HttpRequest.Builder request = HttpRequest
        .newBuilder(URI.create(parameters[0]));

      // prepare request headers
      final String[] headers = parameters[3].split(";;");
      for (final String header : headers) {
        final String[] pair = header.split("=");
        if (pair.length < 2)
          continue;
        request.setHeader(pair[0], pair[1]);
      }

      // overridden headers
      request.setHeader("User-Agent", "API-Text-Action");
      request.setHeader("Content-Type", "application/json");

      // prepare request body
      final String requestType = parameters[2];
      switch (requestType) {
        case "GET":
          request.GET();
          break;
        case "DELETE":
          request.DELETE();
          break;
        default: {
          // fetch and build the body
          final Map<String, String> body = new LinkedHashMap<>();
          final String[] bodyProperties = parameters[4].split(";;");

          for (final String bodyProperty : bodyProperties) {
            final String[] pair = bodyProperty.split("=");
            if (pair.length < 2)
              continue;
            body.put(pair[0], pair[1]);
          }

          final BodyPublisher bodyPublisher = BodyPublishers.ofString(serialize(body));
          // set request type accordingly
          switch (requestType) {
            case "POST":
              request.POST(bodyPublisher);
              break;
            case "PUT":
              request.PUT(bodyPublisher);
              break;
            default:
              return origin;
          }
        }
      }

      // send request
      final HttpResponse<String> response = client.send(request.build(), BodyHandlers.ofString(UTF_8));
      final String responseBody = response.body();

      // prepare for extraction
      final String[] paths = parameters[1].split("\\.");
      final int lastIndex = paths.length - 1;
      Any next = deserialize(responseBody);
      Object value = null;

      // return the deserialized value if there is no path
      if (lastIndex == -1 || lastIndex == 0 && paths[0].isBlank())
        return next;

      // extract content
      for (int index = 0; index < paths.length; index++) {
        if (value != null)
          return origin;

        final String path = paths[index];
        final int pathLength = path.length();

        if (pathLength >= 3 && path.charAt(0) == '[' && path.charAt(pathLength - 1) == ']') {
          next = next.get(indexFromSpec(path));
        } else {
          next = next.get(path);
        }

        if (index == lastIndex)
          value = next;
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