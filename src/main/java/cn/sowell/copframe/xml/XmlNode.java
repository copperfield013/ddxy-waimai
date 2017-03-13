package cn.sowell.copframe.xml;

import java.util.LinkedHashMap;
import java.util.List;
/**
 * 
 * <p>Title: XmlObject</p>
 * <p>Description: </p><p>
 * 通用xml对象接口
 * </p>
 * @author Copperfield Zhang
 * @date 2017年3月9日 下午2:57:13
 */
public interface XmlNode {
	/**
	 * 获得当前节点的类型
	 * @return
	 */
	String getTagName();
	/**
	 * 获得当前节点的某个属性的值
	 * @return
	 */
	String getAttribute(String attrName);
	/**
	 * 获得内容
	 * @return
	 */
	String getText();
	/**
	 * 获得第一个类型是tagName的子节点
	 * @param tagName
	 * @return
	 */
	XmlNode getFirstElement(String tagName);
	/**
	 * 获得第一个attr是attrValue的子节点
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	XmlNode getFirstElement(String attrName, String attrValue);
	/**
	 * 获得所有attr是attrValue的子节点
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	List<XmlNode> getElements(String attrName, String attrValue);
	/**
	 * 获得所有子节点
	 * @return
	 */
	List<XmlNode> getElements();
	/**
	 * 获得所有类型是tagName的子节点
	 * @param tagName
	 * @return
	 */
	List<XmlNode> getElements(String tagName);
	/**
	 * 获得当前节点的父亲节点。如果当前节点是根节点，则返回null
	 * @return
	 */
	XmlNode getParent();
	/**
	 * 当前节点及下级节点转换为xml
	 * @return
	 */
	String asXML();
	
	/**
	 * 获得第一个类型是tagName的节点的内容
	 * 如果节点不存在，那么返回null
	 * @param tagName
	 * @return
	 */
	String getFirstElementText(String tagName);
	
	/**
	 * 添加一个类型为tagName的节点
	 * @param name
	 * @return 返回创建的节点
	 */
	XmlNode addNode(String tagName);
	
	/**
	 * 添加一个类型为tagName的子节点，并且为子节点添加cdata
	 * @param tagName
	 * @param cdata
	 * @return 返回创建的的子节点
	 */
	XmlNode addNodeWithCDATA(String tagName, String cdata);
	
	/**
	 * 直接为当前节点设置内容
	 * @param text
	 * @return 返回自身
	 */
	XmlNode setText(String text);
	/**
	 * 为当前节点设置内容
	 * @param text
	 * @return 返回自身
	 */
	XmlNode addCDATA(String text);
	/**
	 * 清空当前节点内的所有内容
	 * @return 返回自身
	 */
	XmlNode empty();
	
	/**
	 * 设置当前节点的某个属性
	 * @param attrName
	 * @param attrValue
	 * @return 返回自身
	 */
	XmlNode setAttribute(String attrName, String attrValue);
	
	/**
	 * 将当前所有的子节点转换为map
	 * 如果有重复的子节点，只取第一个
	 * @return
	 */
	LinkedHashMap<String, String> toTagTextMap();
}
