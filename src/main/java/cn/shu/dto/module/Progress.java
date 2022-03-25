/**
  * Copyright 2022 bejson.com 
  */
package cn.shu.dto.module;
import cn.shu.dto.module.ModuleList;

import java.util.List;

/**
 * Auto-generated: 2022-03-23 12:33:4
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Progress {

    private List<ModuleList> moduleList;
    private String moduleId;
    public void setModuleList(List<ModuleList> moduleList) {
         this.moduleList = moduleList;
     }
     public List<ModuleList> getModuleList() {
         return moduleList;
     }

    public void setModuleId(String moduleId) {
         this.moduleId = moduleId;
     }
     public String getModuleId() {
         return moduleId;
     }

}