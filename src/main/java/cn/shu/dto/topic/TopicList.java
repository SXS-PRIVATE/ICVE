/**
  * Copyright 2022 bejson.com 
  */
package cn.shu.dto.topic;

/**
 * Auto-generated: 2022-03-23 12:37:47
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TopicList {

    private String id;
    private String name;
    private int sortOrder;
    private String upTopicId;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setSortOrder(int sortOrder) {
         this.sortOrder = sortOrder;
     }
     public int getSortOrder() {
         return sortOrder;
     }

    public void setUpTopicId(String upTopicId) {
         this.upTopicId = upTopicId;
     }
     public String getUpTopicId() {
         return upTopicId;
     }

}