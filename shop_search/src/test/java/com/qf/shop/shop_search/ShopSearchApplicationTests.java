package com.qf.shop.shop_search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopSearchApplicationTests {

	@Autowired
	private SolrClient solrClient;

	@Test
	public void addSolr() {

			for(int i=10;i<50;i++){
				//创建索引对象,一个索引对象只能添加一条索引
				SolrInputDocument solrInputDocument = new SolrInputDocument();
				//添加索引要添加id,否则会报错
				//id不一样，视为新增
				//http://192.168.140.128:8080/solr: Document is missing mandatory uniqueKey field: id

				solrInputDocument.addField("id",+i);
				solrInputDocument.addField("gtitle","红富士苹果"+i);
				solrInputDocument.addField("ginfo","好吃又美味"+i);
				solrInputDocument.addField("gprice","1099");
				solrInputDocument.addField("gimage","group1/M00/00/00/wKiMgFuiRVeABgseAAAuwQXiid4643.jpg");
				try {
					//将索引对象添加进索引库
					solrClient.add(solrInputDocument);
				} catch (SolrServerException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		try {
			//提交索引库
			solrClient.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除索引库
	 */
	@Test
	public void deleteSolr(){
		try {
			//solrClient.deleteById(1+"");根据id删除索引
			solrClient.deleteByQuery("*:*");
			//一定要提交才生效
			solrClient.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void querySolr(){
		SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("gtitle:1");
        try {
            QueryResponse query = solrClient.query(solrQuery);
            SolrDocumentList results = query.getResults();//得到查询的结果集
            //public class SolrDocumentList extends ArrayList<SolrDocument>
            for(SolrDocument solrDocument :results){
                String id = solrDocument.getFieldValue("id")+"";
                String gtitle = solrDocument.getFieldValue("gtitle")+"";
                String ginfo = solrDocument.getFieldValue("ginfo") + "";
                String gimage = solrDocument.getFieldValue("gimage")+"";
                Double gprice = (Double)solrDocument.getFieldValue("gprice");
                System.out.println(id);
                System.out.println(gtitle);
                System.out.println(ginfo);
                System.out.println(gimage);
                System.out.println(gprice);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
