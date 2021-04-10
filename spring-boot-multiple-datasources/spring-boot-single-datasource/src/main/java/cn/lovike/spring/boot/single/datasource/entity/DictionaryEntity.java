package cn.lovike.spring.boot.single.datasource.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "bifrost_dictionary")
public class DictionaryEntity {
    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 字典key
     */
    @Column(name = "dict_key")
    private String dictKey;

    /**
     * 字典值
     */
    @Column(name = "dict_value")
    private String dictValue;

    /**
     * 字典分组 1 默认组
     */
    @Column(name = "dict_group")
    private Integer dictGroup;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String editor;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取字典key
     *
     * @return dict_key - 字典key
     */
    public String getDictKey() {
        return dictKey;
    }

    /**
     * 设置字典key
     *
     * @param dictKey 字典key
     */
    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    /**
     * 获取字典分组 1 默认组
     *
     * @return groups - 字典分组 1 默认组
     */
    public Integer getDictGroup() {
        return dictGroup;
    }

    /**
     * 设置字典分组 1 默认组
     *
     * @param dictGroup 字典分组 1 默认组
     */
    public void setDictGroup(Integer dictGroup) {
        this.dictGroup = dictGroup;
    }

    /**
     * 获取描述信息
     *
     * @return description - 描述信息
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述信息
     *
     * @param description 描述信息
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取修改人
     *
     * @return editor - 修改人
     */
    public String getEditor() {
        return editor;
    }

    /**
     * 设置修改人
     *
     * @param editor 修改人
     */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * 获取字典值
     *
     * @return dict_value - 字典值
     */
    public String getDictValue() {
        return dictValue;
    }

    /**
     * 设置字典值
     *
     * @param dictValue 字典值
     */
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    @Override
    public String toString() {
        return "DictionaryEntity{" +
                "id=" + id +
                ", dictKey='" + dictKey + '\'' +
                ", dictValue='" + dictValue + '\'' +
                ", dictGroup=" + dictGroup +
                ", description='" + description + '\'' +
                ", creator='" + creator + '\'' +
                ", editor='" + editor + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}