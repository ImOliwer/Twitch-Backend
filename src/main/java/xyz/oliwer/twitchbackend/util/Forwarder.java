package xyz.oliwer.twitchbackend.util;

/**
 * This functional interface represents a single method
 * used to forward data to a receiver.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
@FunctionalInterface
public interface Forwarder<Receiver, Data> {
  /**
   * Forward data to passed receiver.
   *
   * @param receiver {@link Receiver} who or what to receive the data.
   * @param data {@link Data} the data to forward.
   * @param extra {@link Object} array of optional arguments.
   * @return {@link Boolean} whether the forwarding was successfully executed.
   */
  boolean forward(Receiver receiver, Data data, Object... extra);
}