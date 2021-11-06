package xyz.oliwer.twitchbackend.action;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.regex.Pattern.compile;

/**
 * This abstract class represents the layer for {@link Pattern} text actions.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public abstract class PatternTextAction implements TextAction {
  /**
   * This class represents the parser for {@link PatternTextAction}.
   *
   * @author Oliwer - https://www.github.com/ImOliwer
   */
  public static final class Parser extends TextAction.Parser<PatternTextAction> {
    /** @see TextAction.Parser#Parser(char, char) **/
    private Parser(char startDelimiter, char endDelimiter) {
      super(startDelimiter, endDelimiter);
    }

    /** @see TextAction.Parser#resolve(String) **/
    @Override
    public String resolve(String origin) {
      for (final PatternTextAction action : this.actions) {
        origin = action
          .pattern
          .matcher(origin)
          .replaceAll(result ->
            action.parse(
              result.group(0),
              result.group(2).split(valueOf(action.separator()))
            ).toString()
          );
      }
      return origin;
    }
  }

  /** {@link Pattern} this property represents the pattern of this action. **/
  public final Pattern pattern;

  /**
   * Primary constructor; used to format and compile the pattern of this text action.
   *
   * @param parser {@link TextAction.Parser<PatternTextAction>} the parser to fetch start and end delimiters from.
   */
  public PatternTextAction(TextAction.Parser<PatternTextAction> parser) {
    this.pattern = compile(
      format(
        "%s(%s)\\((.*?)\\)\\%s",
        parser.startDelimiter,
        tag(),
        parser.endDelimiter
      )
    );
  }

  /**
   * Check if a delimiter (char) is reserved (due to regex limitations).
   *
   * @param delimiter {@link Character} the character to check.
   */
  public static void checkReserved(char delimiter) {
    if (delimiter == '{' || delimiter == '}' || delimiter == '[' || delimiter == ']')
      throw new RuntimeException(format("Delimiter %s is reserved", delimiter));
  }

  /**
   * Create a new parser instance with start and end delimiters.
   *
   * @param startDelimiter {@link Character} the desired start delimiter.
   * @param endDelimiter {@link Character} the desired end delimiter.
   * @return {@link TextAction.Parser<PatternTextAction>}
   */
  public static @NotNull TextAction.Parser<PatternTextAction> parser(char startDelimiter, char endDelimiter) {
    return new Parser(startDelimiter, endDelimiter);
  }
}