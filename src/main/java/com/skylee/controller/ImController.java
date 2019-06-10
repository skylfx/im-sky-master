package com.skylee.controller;

import com.alibaba.fastjson.JSONObject;
import com.skylee.config.WebSocketConfig;
import com.skylee.model.ImMessage;
import com.skylee.model.User;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * 核心控制器
 */
@RestController
public class ImController {
  @Autowired
  private SimpMessagingTemplate template;
  @Autowired
  WebSocketConfig webSocketConfig;

  @GetMapping("/userlist")
  public List<User> getUserlist(){
    List<User> users = webSocketConfig.onlineUser;
    return users;
  }
  //机器对话
  @MessageMapping("/robot")
  @SendToUser("/robot")
  public ImMessage robotchat(ImMessage message) throws Exception {
    String url = "http://www.tuling123.com/openapi/api";
    //请填写自己的key
    String post = "{\"key\": \"\",\"info\": \""+message.getContent()+"\",\"userid\":\""+message.getName()+"\"}";
    String body = Jsoup.connect(url).method(Connection.Method.POST)
            .requestBody(post)
            .header("Content-Type", "application/json; charset=utf-8")
            .ignoreContentType(true).execute().body();
    String text = JSONObject.parseObject(body).getString("text");
    ImMessage m  =new ImMessage("机器人",text,message.getName(),message.getDate());
    return  m;
  }
  //普通私聊对话
  @MessageMapping("/private")
  public void privatechat(ImMessage message) throws Exception {
      if(message.getReceiver().contains(",")){ //群聊
        String[] names= message.getReceiver().split(",");
        for(int i=0;i< names.length;i++){
            if(!names[i].equals(message.getName())){
                template.convertAndSendToUser(names[i],"/topic/private",message);
            }//除了自己其他的都通知
        }
      }else  if(message.getReceiver().equals("机器人")){ //单聊
          String url = "http://www.tuling123.com/openapi/api";
          //请填写自己的key
          String userid="454995";
          String post = "{\"key\": \"646d321c227045a69253fd07d8703840\",\"info\": \""+message.getContent()+"\",\"userid\":\""+userid+"\"}";
          String body = Jsoup.connect(url).method(Connection.Method.POST)
                  .requestBody(post)
                  .header("Content-Type", "application/json; charset=utf-8")
                  .ignoreContentType(true).execute().body();
          String text = JSONObject.parseObject(body).getString("text");
          ImMessage m  =new ImMessage("机器人",text,message.getName(),message.getDate());
          template.convertAndSendToUser(message.getName(),"/topic/private",m);
      }else{
          template.convertAndSendToUser(message.getReceiver(),"/topic/private",message);//发送其订阅的频道
      }
  }
}
