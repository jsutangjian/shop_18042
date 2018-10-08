
package com.qf.shop.shop_search.SearchUtil;

import com.qf.entity.Goods;
import com.qf.entity.SolrPage;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchBykeyword {

    public static SolrPage<Goods> searchBykeyword(String keyword, SolrClient solrClient, SolrPage<Goods> page){
        SolrQuery solrQuery = new SolrQuery();
        //关键字为空，就查询所有
        if(keyword == null || keyword.trim() ==""){
            solrQuery.setQuery("*:*");
        }else{
            solrQuery.setQuery("goods_info:"+keyword);
        }
        //给搜索字段添加高亮
        solrQuery.setHighlight(true);//设置允许开放
        solrQuery.setHighlightSimplePre("<font color='red'>");//高亮前
        solrQuery.setHighlightSimplePost("</font>");//高亮后
        //设置高亮的字段,一定要添加高亮的字段
        solrQuery.addHighlightField("gtitle");

        //设置缩略
       /* solrQuery.setHighlightSnippets(3);
        solrQuery.setHighlightFragsize(10);*/
        //solrQuery.addHighlightField("gtitle"); //这里不能设置在普通的字段上，设置在这里不会高亮

        //开始计算
        solrQuery.setStart((page.getCurrentPage()-1)*page.getPageSize());//从哪页开始
        solrQuery.setRows(page.getPageSize());//每页显示的条数

        QueryResponse query = null;
        List<Goods> goods = null;
        SolrDocumentList results = null;
        try {
            query = solrClient.query(solrQuery);//得到搜索出来的结果集
            Map<String, Map<String, List<String>>> highlighting =  query.getHighlighting();
            for(Map.Entry<String, Map<String, List<String>>> map1:highlighting.entrySet()){
                System.out.println("key:"+map1.getKey());
                System.out.println("value:"+map1.getValue());
            }
            results = query.getResults();//查询出来的是一个结果集，


            long pageTotal = results.getNumFound();//查询出来索引的总条数
            page.setPageTotal((int)pageTotal);
            page.setPageCount(page.getPageTotal() % page.getPageSize() ==0 ?
                    page.getPageTotal() / page.getPageSize():
                    page.getPageTotal() / page.getPageSize() + 1);

            goods = new ArrayList<Goods>();


            for (SolrDocument result : results) {
                Goods good = new Goods();
                good.setId(Integer.parseInt(result.getFieldValue("id")+""));
                good.setTitle(result.getFieldValue("gtitle")+"");
                good.setGimage(result.getFieldValue("gimage")+"");
                good.setPrice(Double.parseDouble(result.getFieldValue("gprice")+""));

                //高亮的子弹已经拥有，要把它体现出来，在页面上体现出来
                if(highlighting.containsKey(good.getId()+"")){//如果要查询的高亮字段信息包含商品的id
                    //就把这个商品的标题的这个字段设置为高亮字段
                    //根据商品的id查询它所对应的value
                    Map<String, List<String>> goodMap = highlighting.get(good.getId() + "");
                    List<String> gtitles = goodMap.get("gtitle");//得到的字段可能有很多处和查询的高亮字段是相同的
                    System.out.println(gtitles);
                    //如果是设置摘要,如果有多个搜索字段，将它缩略
                   /* StringBuilder sb = new StringBuilder("");
                    for (String gtitle : gtitles) {
                        sb.append(gtitle+"...");
                    }
                    good.setTitle(sb.toString());
                    System.out.println(sb.toString());*/
                   if(gtitles != null){
                       String gtitle = gtitles.get(0);//第一个就是商品的标题
                           good.setTitle(gtitle);//有就把高亮字段覆盖普通字段，没有就让他正常显示
                           //这样就可以了
                   }
                }
                goods.add(good);
            }
            page.setPageDatas(goods);
            return page;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
