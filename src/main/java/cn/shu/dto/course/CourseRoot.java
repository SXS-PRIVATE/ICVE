/**
  * Copyright 2022 bejson.com 
  */
package cn.shu.dto.course;
import java.util.List;

/**
 * Auto-generated: 2022-03-23 11:45:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CourseRoot {

    private int code;
    private String type;
    private String termId;
    private List<CourseList> courseList;
    private List<TermList> termList;
    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setTermId(String termId) {
         this.termId = termId;
     }
     public String getTermId() {
         return termId;
     }

    public void setCourseList(List<CourseList> courseList) {
         this.courseList = courseList;
     }
     public List<CourseList> getCourseList() {
         return courseList;
     }

    public void setTermList(List<TermList> termList) {
         this.termList = termList;
     }
     public List<TermList> getTermList() {
         return termList;
     }

}