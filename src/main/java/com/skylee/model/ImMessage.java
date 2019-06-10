package com.skylee.model;

/**
 */
public class ImMessage {
  /**
   * 频道号
   */
  private String room;
  /**
   * 发送者
   */
  private String name;
  /**
   * 内容
   */
  private String content;
  /**
   * 接收者
   */
  private String receiver;
  /**
   * 时间
   */
  private String date;

  public ImMessage(String name, String content, String receiver, String date) {
    this.name = name;
    this.content = content;
    this.receiver = receiver;
    this.date = date;
  }

  public ImMessage() {
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
