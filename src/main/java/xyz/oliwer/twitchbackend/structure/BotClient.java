package xyz.oliwer.twitchbackend.structure;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import xyz.oliwer.twitchbackend.util.ChatMessage;
import xyz.oliwer.twitchbackend.util.Connector;
import xyz.oliwer.twitchbackend.util.Forwarder;

import java.util.Properties;
import java.util.function.Function;

/**
 * This class represents the client for our bot.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class BotClient implements Connector<String>, Forwarder<String, ChatMessage> {
  /** {@link TwitchClient} the main client for this application. **/
  public final TwitchClient twitch;

  /**
   * Primary constructor.
   *
   * @param properties {@link Properties} the properties of this bot client.
   */
  public BotClient(Properties properties) {
    if (properties == null) {
      throw new NullPointerException("Bot client properties must not be null");
    }

    final String clientId = properties.getProperty("Client-Id");
    final String chatAccessToken = properties.getProperty("Chat-Bot-Access-Token");

    if (clientId == null || chatAccessToken == null) {
      throw new NullPointerException("Property 'Client-Id' and/or 'Chat-Bot-Access-Token' are missing");
    }

    this.twitch = TwitchClientBuilder
      .builder()
      .withClientId(clientId)
      .withEnableChat(true)
      .withChatAccount(new OAuth2Credential("twitch", chatAccessToken))
      .withEnablePubSub(true)
      .withEnableHelix(true)
      .withEnableKraken(true)
      .build();
  }

  /**
   * Subscribe to an event handler.
   *
   * @param eventBus {@link Object} instance of the handler to register.
   */
  public void subscribe(Object eventBus) {
    twitch
      .getEventManager()
      .getEventHandler(SimpleEventHandler.class)
      .registerListener(eventBus);
  }

  /**
   * @see Forwarder#forward(Object, Object, Object...)
   */
  @Override
  public boolean forward(String receiver, ChatMessage data, Object... extra) {
    return with(twitch.getChat(), chat -> {
      if (receiver == null || data == null || extra.length == 0) {
        return false;
      }

      final String message = extra[0].toString();
      switch (data) {
        case REGULAR: return chat.sendMessage(receiver, message);
        case ACTION : return chat.sendActionMessage(receiver, message);
        case WHISPER: chat.sendPrivateMessage(receiver, message);
      }
      return true;
    });
  }

  /**
   * @see Connector#connect(Object)
   */
  @Override
  public boolean connect(String channel) {
    return handleConnection(true, channel);
  }

  /**
   * @see Connector#disconnect(Object)
   */
  @Override
  public boolean disconnect(String channel) {
    return handleConnection(false, channel);
  }

  /**
   * Handle the connection.
   *
   * @param isConnect {@link Boolean} whether this operation is a "connect" (if not it's "disconnect").
   * @param channel {@link String} the channel to connect to / disconnect from.
   * @return {@link Boolean} state of operation - successful or not.
   */
  private boolean handleConnection(boolean isConnect, String channel) {
    return with(twitch.getChat(), chat -> {
      final boolean shouldContinue = channel != null && isConnect != chat.isChannelJoined(channel);
      if (shouldContinue) {
        if (isConnect) chat.joinChannel(channel);
        else chat.leaveChannel(channel);
      }
      return shouldContinue;
    });
  }

  /**
   * Perform an operation with passed instance in a cleaner way.
   */
  private static <Type, Return> Return with(Type instance, Function<Type, Return> application) {
    return application.apply(instance);
  }
}