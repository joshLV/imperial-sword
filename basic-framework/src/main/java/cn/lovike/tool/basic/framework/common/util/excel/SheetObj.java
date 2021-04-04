package cn.lovike.tool.basic.framework.common.util.excel;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @param <E>
 * @Title SheetObj.java
 * @Description: 导出excel时，封装的sheet数据对象
 * @Author jinsong.zhan
 * @Date 2013-1-16下午1:19:48
 * @Version v 1.0
 */
public class SheetObj<E> implements java.io.Serializable {
	
	private static final long serialVersionUID = 1993713951711849723L;

	/**
	 * excel表里的sheet名称
	 */
	private String sheetName;
	/**
	 * 在excel中可以添加表头，更加友好的显示数据，可以为null
	 */
	private String titleName;
	/**
	 * 表头封装数据，ExProtObjInfo中必填（epoiCname、epoiEname、epoiType默认为1）
	 */
	private List<ExProtObjInfo> objInfos;
	
	private Map<String,ExProtObjInfo> objInfoMap=null;
	
	public Map<String, ExProtObjInfo> getObjInfoMap() {
		if(objInfoMap==null){
			objInfoMap=new HashMap<String, ExProtObjInfo>();
			for(ExProtObjInfo ex:objInfos){
				objInfoMap.put(ex.getEpoiEname().trim(), ex);
			}
		}
		return objInfoMap;
	}

	/**
	 * 数据类型list对象
	 */
	private Collection<E> data;

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public List<ExProtObjInfo> getObjInfos() {
		return objInfos;
	}

	public void setObjInfos(List<ExProtObjInfo> objInfos) {
		this.objInfos = objInfos;
	}

	public Collection<E> getData() {
		return data;
	}

	public void setData(Collection<E> data) {
		this.data = data;
	}
	
	public Integer getFieldType(String fieldName){
		for(ExProtObjInfo ex:objInfos){
			if(ex.getEpoiEname().equalsIgnoreCase(fieldName)){
				return ex.getEpoiType();
			}
		}
		return -1;
	}
}
