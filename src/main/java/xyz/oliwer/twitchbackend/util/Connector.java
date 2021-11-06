package xyz.oliwer.twitchbackend.util;

/**
 * This interface represents an object of which provides
 * {@link Type} the ability to connect & disconnect to and from something.
 *
 * @see xyz.oliwer.twitchbackend.structure.BotClient
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public interface Connector<Type> {
  /**
   * Connect to * by passed down instance.
   *
   * @param type {@link Type} the instance used in this connection.
   * @return {@link Boolean} whether the connection was successful.
   */
  boolean connect(Type type);

  /**
   * Disconnect from * by passed down instance.
   *
   * @param type {@link Type} the instance used in this disconnection.
   * @return {@link Boolean} whether the disconnection was successful.
   */
  boolean disconnect(Type type);
}