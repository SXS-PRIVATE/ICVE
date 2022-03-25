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
public class CellList {

    private String Id;
    private int cellType;
    private int isGJS;
    private int isOriginal;
    private int fromType;
    private String parentId;
    private String courseOpenId;
    private String topicId;
    private String categoryName;
    private String categoryNameDb;
    private String cellName;
    private String resourceUrl;
    private String externalLinkUrl;
    private String cellContent;
    private int sortOrder;
    private boolean isAllowDownLoad;
    private List<CellList> childNodeList;
    private String upCellId;
    private int stuCellCount;
    private Integer stuCellPercent;
    private Integer stuCellFourPercent;
    public void setId(String Id) {
         this.Id = Id;
     }
     public String getId() {
         return Id;
     }

    public void setCellType(int cellType) {
         this.cellType = cellType;
     }
     public int getCellType() {
         return cellType;
     }

    public void setIsGJS(int isGJS) {
         this.isGJS = isGJS;
     }
     public int getIsGJS() {
         return isGJS;
     }

    public void setIsOriginal(int isOriginal) {
         this.isOriginal = isOriginal;
     }
     public int getIsOriginal() {
         return isOriginal;
     }

    public void setFromType(int fromType) {
         this.fromType = fromType;
     }
     public int getFromType() {
         return fromType;
     }

    public void setParentId(String parentId) {
         this.parentId = parentId;
     }
     public String getParentId() {
         return parentId;
     }

    public void setCourseOpenId(String courseOpenId) {
         this.courseOpenId = courseOpenId;
     }
     public String getCourseOpenId() {
         return courseOpenId;
     }

    public void setTopicId(String topicId) {
         this.topicId = topicId;
     }
     public String getTopicId() {
         return topicId;
     }

    public void setCategoryName(String categoryName) {
         this.categoryName = categoryName;
     }
     public String getCategoryName() {
         return categoryName;
     }

    public void setCategoryNameDb(String categoryNameDb) {
         this.categoryNameDb = categoryNameDb;
     }
     public String getCategoryNameDb() {
         return categoryNameDb;
     }

    public void setCellName(String cellName) {
         this.cellName = cellName;
     }
     public String getCellName() {
         return cellName;
     }

    public void setResourceUrl(String resourceUrl) {
         this.resourceUrl = resourceUrl;
     }
     public String getResourceUrl() {
         return resourceUrl;
     }

    public void setExternalLinkUrl(String externalLinkUrl) {
         this.externalLinkUrl = externalLinkUrl;
     }
     public String getExternalLinkUrl() {
         return externalLinkUrl;
     }

    public void setCellContent(String cellContent) {
         this.cellContent = cellContent;
     }
     public String getCellContent() {
         return cellContent;
     }

    public void setSortOrder(int sortOrder) {
         this.sortOrder = sortOrder;
     }
     public int getSortOrder() {
         return sortOrder;
     }

    public void setIsAllowDownLoad(boolean isAllowDownLoad) {
         this.isAllowDownLoad = isAllowDownLoad;
     }
     public boolean getIsAllowDownLoad() {
         return isAllowDownLoad;
     }

    public void setChildNodeList(List<CellList> childNodeList) {
         this.childNodeList = childNodeList;
     }
     public List<CellList> getChildNodeList() {
         return childNodeList;
     }

    public void setUpCellId(String upCellId) {
         this.upCellId = upCellId;
     }
     public String getUpCellId() {
         return upCellId;
     }

    public void setStuCellCount(int stuCellCount) {
         this.stuCellCount = stuCellCount;
     }
     public int getStuCellCount() {
         return stuCellCount;
     }

    public Integer getStuCellPercent() {
        return stuCellPercent;
    }

    public void setStuCellPercent(Integer stuCellPercent) {
        this.stuCellPercent = stuCellPercent;
    }

    public Integer getStuCellFourPercent() {
        return stuCellFourPercent;
    }

    public void setStuCellFourPercent(Integer stuCellFourPercent) {
        this.stuCellFourPercent = stuCellFourPercent;
    }

    public boolean isAllowDownLoad() {
        return isAllowDownLoad;
    }

    public void setAllowDownLoad(boolean allowDownLoad) {
        isAllowDownLoad = allowDownLoad;
    }


}