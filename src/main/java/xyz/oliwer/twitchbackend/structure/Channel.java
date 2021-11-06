package xyz.oliwer.twitchbackend.structure;

import org.jetbrains.annotations.NotNull;
import xyz.oliwer.twitchbackend.identity.ObjectIdentity;

/**
 * This class represents a Twitch channel.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class Channel implements ObjectIdentity<String> {
  /** {@link String} the id of this channel. **/
  private final String id;

  /**
   * @param id {@link String} the id to be assigned to this channel.
   */
  public Channel(String id) {
    this.id = id;
  }

  /** @see ObjectIdentity#identifier() **/
  @Override
  public @NotNull String identifier() {
    return this.id;
  }
}