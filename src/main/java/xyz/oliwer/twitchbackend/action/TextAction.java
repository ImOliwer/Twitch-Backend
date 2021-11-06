package xyz.oliwer.twitchbackend.action;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static xyz.oliwer.twitchbackend.action.PatternTextAction.checkReserved;

/**
 * This interface represents the base of every text action.
 *
 * @see PatternTextAction
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public interface TextAction {
  /**
   * This abstract class represents the base of every {@link TextAction} parser.
   *
   * @see PatternTextAction.Parser
   * @param <A> type of {@link TextAction}.
   * @author Oliwer - https://www.github.com/ImOliwer
   */
  abstract class Parser<A extends TextAction> {
    /** {@link Character} start delimiter for this parser. **/
    protected final char startDelimiter;

    /** {@link Character} end delimiter for this parser. **/
    protected final char endDelimiter;

    /** {@link Set<A>} set of registered actions. **/
    protected final Set<A> actions = new HashSet<>();

    /**
     * Primary constructor.
     *
     * @param startDelimiter {@link Character} the start delimiter to be set for this parser.
     * @param endDelimiter {@link Character} the end delimiter to be set for this parser.
     */
    public Parser(char startDelimiter, char endDelimiter) {
      // ensure the delimiters are not whitespace
      if (startDelimiter == ' ' || endDelimiter == ' ')
        throw new RuntimeException("start and end delimiters must NOT be a whitespace");

      // ensure the delimiters are not reserved
      checkReserved(startDelimiter);
      checkReserved(endDelimiter);

      // initialize
      this.startDelimiter = startDelimiter;
      this.endDelimiter = endDelimiter;
    }

    /**
     * Register a new action to this parser.
     *
     * @param action {@link A} action to be registered.
     * @return {@link Parser<A>} current instance.
     */
    public final Parser<A> withAction(A action) {
      this.actions.add(action);
      return this;
    }

    /**
     * @see Parser#withAction(TextAction)
     */
    public final Parser<A> withAction(Function<Parser<A>, A> supplier) {
      return withAction(supplier.apply(this));
    }

    /**
     * Resolve the actions inside the origin passed.
     *
     * @param origin {@link String} the string to be processed & resolved.
     * @return {@link String}
     */
    public abstract String resolve(String origin);
  }

  /**
   * Parse this action from origin & parameters accordingly.
   *
   * @param origin {@link String} the origin of this action (i.e `%action(first_param,second_param)%`).
   * @param parameters {@link String} array of parameters provided in the action invocation.
   * @return {@link Object} parsed action.
   */
  Object parse(String origin, String... parameters);

  /**
   * Get the tag of this action.
   *
   * @return {@link String}
   */
  String tag();

  /**
   * Get the separator for this action.
   *
   * @return {@link Character}
   */
  default char separator() {
    return ',';
  }
}