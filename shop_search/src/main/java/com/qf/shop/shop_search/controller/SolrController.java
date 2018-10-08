package com.qf.shop.shop_search.controller;

import com.qf.entity.Goods;
import com.qf.entity.SolrPage;
import com.qf.shop.shop_search.SearchUtil.SearchBykeyword;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/solr")
public class SolrController {

    @Autowired
    private SolrClient solrClient;

    @RequestMapping("/add")
    @ResponseBody//这里如果没有用responsebody,它会当成一个视图解析，又因为返回时null，
    //所以视图的名称会当成请求的名字，/solr/add.jsp,找不到
    //用responsebody的前提是，json直接是请求体，不是的话就不能用RequestBody注解了
    //如果json不直接是请求体，就是String name, 拿到value了
    public Boolean addSolr(@RequestBody Goods good){
        //数据接收成功之后，添加进索引库
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("id",good.getId());
        solrInputDocument.addField("gtitle",good.getTitle());
        solrInputDocument.addField("ginfo",good.getGinfo());
        solrInputDocument.addField("gimage",good.getGimage());
        solrInputDocument.addField("gprice",good.getPrice());

        //把添加的字段加到词库客户端
        try {
            solrClient.add(solrInputDocument);
            //添加完成一定要要提交
            solrClient.commit();
            return true;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 前端的currentpage自动封转到SolrPage的对象里面
     * @param keyword
     * @param model
     * @param solrPage
     * @return
     */
    @RequestMapping("/search")
    public String solrSearch(String keyword, Model model, SolrPage<Goods> solrPage){
        solrPage = SearchBykeyword.searchBykeyword(keyword, solrClient, solrPage);
        model.addAttribute("page",solrPage);
        model.addAttribute("keyword",keyword);
        return "searchlist";
    }

}
