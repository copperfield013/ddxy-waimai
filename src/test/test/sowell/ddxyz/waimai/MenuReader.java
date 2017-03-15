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

import cn.sowell.copframe.dto.format.FormatUtils;

public class MenuReader {
	private InputStream input;
	public MenuReader(InputStream input) {
		this.input = input;
	}
	
	public List<MenuItem> getMenu(){
		Reader reader = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(reader);
		String line;
		Pattern pattern = Pattern.compile("^([^\\s]*)-([^\\s]+)\\s(\\d+)$");
		List<MenuItem> list = new ArrayList<MenuItem>();
		try {
			while((line = br.readLine()) != null){
				Matcher matcher = pattern.matcher(line);
				if(matcher.matches()){
					String sizeStr = matcher.group(1),
							name = matcher.group(2),
							priceStr = matcher.group(3)
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
					MenuItem menu = new MenuItem();
					menu.setContent(content);
					menu.setPrice(price);
					menu.setSize(size);
					if(size == 0){
						menu.setDisabled(true);
					}else{
						menu.setData(name);
					}
					list.add(menu);
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
		Pattern pattern = Pattern.compile("^([^\\s]+)\\s(\\d+)$");
		List<AdditionItem> list = new ArrayList<AdditionItem>();
		try {
			int index = 0;
			while((line = br.readLine()) != null){
				Matcher matcher = pattern.matcher(line);
				if(matcher.matches()){
					AdditionItem item = new AdditionItem();
					String data = matcher.group(1),
							priceStr = matcher.group(2);
					if(index % 6 == 4){
						item.addClass("heat");
					}else if(index % 6 == 5){
						item.addClass("sweetness");
					}
					Integer price = Integer.valueOf(priceStr) * 100;
					
					item.setContent(data + "￥" + priceStr);
					item.setPrice(price);
					item.setData(data);
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
				System.out.println("<span " + (item.isDisabled()? "class=\"disabled\" data=\"" + item.getData() + "\"": "") + "price=\"" + item.getPrice() + "\" size=\"" + item.getSize() + "\">" + item.getContent() + "</span>");
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
				StringBuffer classbuffer = new StringBuffer();
				if(!item.getClasses().isEmpty()){
					classbuffer.append("class=\"");
					for (String clazz : item.getClasses()) {
						classbuffer.append(clazz + " ");
					}
					classbuffer.deleteCharAt(classbuffer.length() - 1);
					classbuffer.append("\" ");
				}
				System.out.println("<span data=\"" + item.getData() + "\" " + classbuffer + "price=\"" + item.getPrice() + "\">" + item.getContent() + "</span>");
			}
			fis.close();
		} catch (IOException e) {
		}
		
	}
	
	public static void main(String[] args) {
		printAdditions("d://addition.txt");
	}
	
	
	
	
}
