/**
  * Copyright 2022 bejson.com 
  */
package cn.shu.dto.cell;
import java.util.List;

/**
 * Auto-generated: 2022-03-23 12:39:13
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CellRoot {

    private int code;
    private String courseOpenId;
    private String openClassId;
    private List<CellList> cellList;
    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setCourseOpenId(String courseOpenId) {
         this.courseOpenId = courseOpenId;
     }
     public String getCourseOpenId() {
         return courseOpenId;
     }

    public void setOpenClassId(String openClassId) {
         this.openClassId = openClassId;
     }
     public String getOpenClassId() {
         return openClassId;
     }

    public void setCellList(List<CellList> cellList) {
         this.cellList = cellList;
     }
     public List<CellList> getCellList() {
         return cellList;
     }

}