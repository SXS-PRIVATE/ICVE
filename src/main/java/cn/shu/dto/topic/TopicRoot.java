/**
  * Copyright 2022 bejson.com 
  */
package cn.shu.dto.topic;
import java.util.List;

/**
 * Auto-generated: 2022-03-23 12:37:47
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TopicRoot {

    private int code;
    private List<TopicList> topicList;
    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setTopicList(List<TopicList> topicList) {
         this.topicList = topicList;
     }
     public List<TopicList> getTopicList() {
         return topicList;
     }

}