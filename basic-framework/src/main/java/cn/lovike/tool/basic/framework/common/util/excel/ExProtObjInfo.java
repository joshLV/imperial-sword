package cn.lovike.tool.basic.framework.common.util.excel;

/**
 * ExProtObjInfo entity. @author MyEclipse Persistence Tools
 */

public class ExProtObjInfo implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -7198444418689761917L;

    private String  epoiId;
    /**
     * 对应导出表 ID
     */
    private String  epoId;
    /**
     * 字段中文名
     */
    private String  epoiCname;
    /**
     * 字段英文名
     */
    private String  epoiEname;
    /**
     * 【0-INT】、【1-STRING】、【2-TIME】、【3-DATE】、// 【4-DATETIME】 、【5-BOOLEAN】、【6-DOUBLE】,默认为1
     */
    private Integer epoiType = 1;

    /**
     * 是否导出 0：导出 1：不导出
     */
    private Integer isPort;
    /**
     * 创建时间
     */

    private Long    creatTime;
    /**
     * 修改时间
     */

    private Long    updateTime;
    /**
     * 序号
     */

    private Integer epoiOrderby;

    // Constructors

    /**
     * default constructor
     */
    public ExProtObjInfo() {
    }

    /**
     * minimal constructor
     */
    public ExProtObjInfo(String epoiId) {
        this.epoiId = epoiId;
    }

    /**
     * full constructor
     */
    public ExProtObjInfo(String epoiId, String epoId, String epoiCname,
                         String epoiEname, Integer epoiType, Integer isPort, Long creatTime,
                         Long updateTime, Integer epoiOrderby) {
        this.epoiId = epoiId;
        this.epoId = epoId;
        this.epoiCname = epoiCname;
        this.epoiEname = epoiEname;
        this.epoiType = epoiType;
        this.isPort = isPort;
        this.creatTime = creatTime;
        this.updateTime = updateTime;
        this.epoiOrderby = epoiOrderby;
    }

    // Property accessors
    public String getEpoiId() {
        return this.epoiId;
    }

    public void setEpoiId(String epoiId) {
        this.epoiId = epoiId;
    }

    public String getEpoId() {
        return this.epoId;
    }

    public void setEpoId(String epoId) {
        this.epoId = epoId;
    }

    public String getEpoiCname() {
        return this.epoiCname;
    }

    public void setEpoiCname(String epoiCname) {
        this.epoiCname = epoiCname;
    }

    public String getEpoiEname() {
        return this.epoiEname;
    }

    public void setEpoiEname(String epoiEname) {
        this.epoiEname = epoiEname;
    }

    public Integer getEpoiType() {
        return this.epoiType;
    }

    public void setEpoiType(Integer epoiType) {
        this.epoiType = epoiType;
    }

    public Integer getIsPort() {
        return this.isPort;
    }

    public void setIsPort(Integer isPort) {
        this.isPort = isPort;
    }

    public Long getCreatTime() {
        return this.creatTime;
    }

    public void setCreatTime(Long creatTime) {
        this.creatTime = creatTime;
    }

    public Long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getEpoiOrderby() {
        return this.epoiOrderby;
    }

    public void setEpoiOrderby(Integer epoiOrderby) {
        this.epoiOrderby = epoiOrderby;
    }

    @Override
    public String toString() {
        return "ExProtObjInfo [epoiId=" + epoiId + ", epoId=" + epoId
                + ", epoiCname=" + epoiCname + ", epoiEname=" + epoiEname
                + ", epoiType=" + epoiType + ", isPort=" + isPort
                + ", creatTime=" + creatTime + ", updateTime=" + updateTime
                + ", epoiOrderby=" + epoiOrderby + "]";
    }
}