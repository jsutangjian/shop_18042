package com.qf.shop.shop_items;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopItemsApplicationTests {

	/**
	 * 导入的是freemarker的configuration
	 */
	@Autowired
	private Configuration configuration;

	@Test
	public void contextLoads() {
		//加载模板
		Writer writer = null;
		try {
			Template template = configuration.getTemplate("my.ftl");
			//准备数据
			Map<String ,String> map = new HashMap<String,String>();
			map.put("key","world");
			//指定生成静态页的位置
			writer = new FileWriter("C:\\Users\\tj\\Desktop\\my.html");
			//模板生成静态页,使用的数据是map
			template.process(map,writer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
