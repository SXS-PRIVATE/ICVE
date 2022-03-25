/**
  * Copyright 2022 bejson.com 
  */
package cn.shu.dto.module;

/**
 * Auto-generated: 2022-03-23 12:33:4
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ModuleList {

    private String id;
    private String name;
    private int sortOrder;
    private int percent;
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

    public void setPercent(int percent) {
         this.percent = percent;
     }
     public int getPercent() {
         return percent;
     }

}