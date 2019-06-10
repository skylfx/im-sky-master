package com.skylee.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skylee.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.util.ArrayList;
import java.util.List;
/**
 * WebSocketConfig 配置
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

  public List<User> onlineUser=new ArrayList<User>();
  @Autowired
  private SimpMessagingTemplate template;
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setUserDestinationPrefix("/user");
    config.setApplicationDestinationPrefixes("/app");
    config.setCacheLimit(1048576);//大小 1M
  }
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    //注册的websocket接入点，前端链接的就是它
    registry.addEndpoint("/im-websocket").withSockJS();
  }

  @Override
  public void configureWebSocketTransport(final WebSocketTransportRegistration registration) {
    //设置文件缓冲 大小 1M
    //如不设置文件稍微大一点就报错了
    registration.setMessageSizeLimit(1048576);
    registration.setSendBufferSizeLimit(1048576);
    registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
      @Override
      public WebSocketHandler decorate(final WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {
          @Override
          public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
            User user = new User();
            user.setName(session.getPrincipal().getName());
            user.setId(session.getId());
            user.setOnline(true);
            onlineUser.add(user);
            template.convertAndSend("/topic/userlist", JSON.toJSON(user));
            System.out.println("连接成功："+session.getId());
            super.afterConnectionEstablished(session);
          }

          @Override
          public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
            throws Exception {
            User user = new User();
            user.setName(session.getPrincipal().getName());
            user.setId(session.getId());
            user.setOnline(false);
            onlineUser.remove(user);
            template.convertAndSend("/topic/userlist", JSON.toJSON(user));
            System.out.println("断开连接: " + session.getId());
            super.afterConnectionClosed(session, closeStatus);
          }
        };
      }
    });
    super.configureWebSocketTransport(registration);
  }
}
