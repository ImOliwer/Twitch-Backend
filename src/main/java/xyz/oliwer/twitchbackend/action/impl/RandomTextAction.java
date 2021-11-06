package xyz.oliwer.twitchbackend.action.impl;

import xyz.oliwer.twitchbackend.action.PatternTextAction;
import xyz.oliwer.twitchbackend.action.TextAction;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents a "random of" implementation of {@link PatternTextAction}.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class RandomTextAction extends PatternTextAction {
  /** @see PatternTextAction#PatternTextAction(TextAction.Parser) **/
  public RandomTextAction(TextAction.Parser<PatternTextAction> parser) {
    super(parser);
  }

  /** @see PatternTextAction#parse(String, String...) **/
  @Override
  public Object parse(String origin, String... parameters) {
    final int length = parameters.length;
    if (length == 0)
      return origin;

    final ThreadLocalRandom random = ThreadLocalRandom.current();
    return parameters[random.nextInt(parameters.length)];
  }

  /** @see PatternTextAction#tag() **/
  @Override
  public String tag() {
    return "random";
  }
}