/**
  * Copyright 2022 bejson.com 
  */
package cn.shu.dto.module;

import cn.shu.dto.module.Progress;

/**
 * Auto-generated: 2022-03-23 12:33:4
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ModuleRoot {

    private int code;
    private String courseOpenId;
    private String openClassId;
    private int openCourseCellCount;
    private int stuStudyCourseOpenCellCount;
    private Progress progress;
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

    public void setOpenCourseCellCount(int openCourseCellCount) {
         this.openCourseCellCount = openCourseCellCount;
     }
     public int getOpenCourseCellCount() {
         return openCourseCellCount;
     }

    public void setStuStudyCourseOpenCellCount(int stuStudyCourseOpenCellCount) {
         this.stuStudyCourseOpenCellCount = stuStudyCourseOpenCellCount;
     }
     public int getStuStudyCourseOpenCellCount() {
         return stuStudyCourseOpenCellCount;
     }

    public void setProgress(Progress progress) {
         this.progress = progress;
     }
     public Progress getProgress() {
         return progress;
     }

}