package xyz.oliwer.twitchbackend.identity;

import org.jetbrains.annotations.NotNull;

/**
 * This interface represents an object identity.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public interface ObjectIdentity<Identifier> {
  /**
   * Get the identifier of this identity.
   *
   * @return {@link Identifier}
   */
  @NotNull
  Identifier identifier();
}