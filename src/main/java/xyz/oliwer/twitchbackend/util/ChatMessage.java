package xyz.oliwer.twitchbackend.util;

/**
 * This enumeration represents the types of chat messages
 * there are available.
 * <br/>
 *
 * <ul>
 *   <li>REGULAR = Channel chat</li>
 *   <li>ACTION  = /me [message]</li>
 *   <li>WHISPER = /whisper [user] [message]</li>
 * </ul>
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public enum ChatMessage {
  REGULAR,
  ACTION,
  WHISPER
}