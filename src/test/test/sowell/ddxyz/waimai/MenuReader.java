package test.sowell.ddxyz.waimai;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import cn.sowell.copframe.dto.format.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;

public class MenuReader {
	private InputStream input;
	public MenuReader(InputStream input) {
		this.input = input;
	}
	
	public List<MenuItem> getMenu(){
		Reader reader = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(reader);
		String line;
		Pattern pattern = Pattern.compile("^([^\\s]*)-([^\\s]+)\\s(\\d+)\\s*(.*)$");
		List<MenuItem> list = new ArrayList<MenuItem>();
		try {
			while((line = br.readLine()) != null){
				Matcher matcher = pattern.matcher(line);
				if(matcher.matches()){
					String sizeStr = matcher.group(1),
							name = matcher.group(2),
							priceStr = matcher.group(3),
							tagsStr = matcher.group(4)
							;
					int size;
					if("中".equals(sizeStr)){
						size = 2;
					}else if("大".equals(sizeStr)){
						size = 3;
					}else{
						size = 0;
					}
					Integer price = FormatUtils.toInteger(priceStr) * 100;
					String content = sizeStr + "-" + name + "￥" + priceStr; 
					MenuItem item = new MenuItem();
					item.setContent(content);
					item.setPrice(price);
					item.setSize(size);
					item.setId(TextUtils.randomStr(5, 62));
					item.setName(sizeStr + "-" + name);
					if(StringUtils.hasText(tagsStr)){
						String[] tags = tagsStr.split(",");
						for (String tag : tags) {
							item.addTag(tag);
						}
					}
					if(size == 0){
						item.setDisabled(true);
					}else{
						item.setData(name);
					}
					list.add(item);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<AdditionItem> getAdditions(){
		Reader reader = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(reader);
		String line;
		Pattern pattern = Pattern.compile("^([^\\s]+)\\s(\\d+)\\s*(.*)$");
		List<AdditionItem> list = new ArrayList<AdditionItem>();
		try {
			int index = 0;
			while((line = br.readLine()) != null){
				Matcher matcher = pattern.matcher(line);
				if(matcher.matches()){
					AdditionItem item = new AdditionItem();
					String data = matcher.group(1),
							priceStr = matcher.group(2),
							tagsStr = matcher.group(3);
					if(index % 6 == 4){
						item.addClass("heat");
					}else if(index % 6 == 5){
						item.addClass("sweetness");
					}
					Integer price = Integer.valueOf(priceStr) * 100;
					if(StringUtils.hasText(tagsStr)){
						String[] tags = tagsStr.split(",");
						for (String tag : tags) {
							item.addTag(tag);
						}
					}
					item.setId(TextUtils.randomStr(5, 62));
					item.setName(data);
					item.setContent(data + "￥" + priceStr);
					item.setPrice(price);
					list.add(item);
				}
				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static void printMenu(String filePath){
		try {
			FileInputStream fis = new FileInputStream(filePath);
			MenuReader reader = new MenuReader(fis);
			List<MenuItem> menu = reader.getMenu();
			for (MenuItem item : menu) {
				HTMLTag span = new HTMLTag("span");
				if(item.isDisabled()){
					span.putAttr("class", "disabled");
				}
				span.putAttr("data-price", item.getPrice());
				span.putAttr("data-size", item.getSize());
				span.putAttr("data-id", item.getId());
				span.putAttr("data-name", item.getName());
				if(item.hasTag()){
					span.putAttr("data-tags", item.getTagsStr());
				}
				span.setContent(item.getContent());
				System.out.println(span);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printAdditions(String filePath){
		try {
			FileInputStream fis = new FileInputStream(filePath);
			MenuReader reader = new MenuReader(fis);
			List<AdditionItem> items = reader.getAdditions();
			for (AdditionItem item : items) {
				HTMLTag span = new HTMLTag("span");
				if(!item.getClasses().isEmpty()){
					span.putAttr("class", item.getClassStr());
				}
				if(!item.getTagsStr().isEmpty()){
					span.putAttr("data-tags", item.getTagsStr());
				}
				span.putAttr("data-id", item.getId());
				span.putAttr("data-price", item.getPrice());
				span.putAttr("data-name", item.getName());
				span.setContent(item.getContent());
				System.out.println(span);
			}
			fis.close();
		} catch (IOException e) {
		}
		
	}
	
	public static void main(String[] args) {
		//printMenu("d://menu1.txt");
		printAdditions("d://addition.txt");
	}
	
	
	
	
}
