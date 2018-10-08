package com.qf.shop_pay.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.qf.entity.Order;
import com.qf.service.IOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.alipay.api.AlipayConstants.CHARSET;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Reference
    private IOrderService orderService;

    @RequestMapping("/alipay")
    public String Alipay(String orderid, Model model/*,HttpServletResponse httpResponse*/){

        model.addAttribute("orderid",orderid);
        //pay(order,httpResponse);
        return "topay";
    }

    @RequestMapping("/topay")
    public void pay(String orderid,HttpServletResponse httpResponse){
        
        Order order = orderService.queryOrderById(orderid);
        AlipayClient alipayClient = new DefaultAlipayClient(
                //沙箱的地址
                "https://openapi.alipaydev.com/gateway.do",
                "2016092000558664",
                //自己的私钥
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbgUz2hXlfTP3oxisFnlVcxCsxGHUm+fjagoam22QZrSfeY7rOpPEug9NO2FQIjp0bKHQCDbnHaSGQGZv1+diDtUawl8vtK1JPmfxFl37FdQ7qALqyoZo7rf6up1b4cIiYGEmi128xJu/+44UwBECPdeXcBvFekpfbJ6hUDYZ9FGC7fpp129CbRLJY10D+9GWkVJTKE+eRmrk7Edfbloohc5pu2zN4BZOEj0IBchsCvVVzkqR/GYXrJ5x4gvIeuGAYOqHM8/p4/nyeiuSCkdUHZclMbgWSbk5Jw40Dq5MhXPi2rx8B/Dq4AlI3yVLwI+YagdSswT/6QxdFvsOY88rhAgMBAAECggEBAIENCUX89wDz8DPhYYrEH6tg0UdpOLAj+zW75OtMXwJABTo2Ia2kjNNBa3efZ/3B/Pew1rVtlqO6x7PCzP+RXPHQ2t1q3D3zGdgdvpIya26K+tmPyWC9ZDXq69Ud4rtDCNjQLywJBaoQ2lL5lmqG3N0xxpSW3nDRz1x8yOEYaGgb0vPLdkKdkVV32d8tdSOjR99xyyqCOz++INBHGJ4IqPJWMp+ty87mCNR99qCg9QbG6V7XVhXuGt0zabI10MxLEOIcg2D274OZJxuUdsrhVS4RBgdjeOvmnKIUx4c89d6UeR6qg1b0Wu5MWef+4AaLR32mMclq0E49vFjgukDgDdECgYEA9WJ49QT/2YclmFE36+FxXZk6Fwi0mIwBbQ4XeO47UhW7T6CxwRyM5p/eDJi3a0h8PMuFKqNYonLaXkjIwVX0wxh3hNHF275u/pXzanHcOrxtgatfMMZEwGC3Z1QzLOcY0BAN3sHX4Tx2N+vcSySjl/uWKezPuqWBJbMx8d7oNhcCgYEAojtzg9LbrtdguCFhvuJQzSzaikY7kMJDuR7Ddw8W6rmnb9lwDYz17+1DtgJAiyC2/pwJiWKiU9Y46gmym5Ce3/uAItk0QW9g4Y2hUAHfYYyOHCxDQ874R5+TP0Da+dHphhPx9LArCxcpVOyipGWb6nFSR8/oD6wDqK2ehXeSmccCgYBfUvCwJlAJvfGB2VNDA+IZPTQOzKfzdrf7GHVP2iQbQCvyw/cpkKC/2qzU8eKW/6Kbr2g85xXAjaN86wNp761UuMV9SEx3j+PobHISEMc/3gEgVWV2Oh9tKGU288OpluDRutrYhS8K5YT7Nlnuqv4ORYLRKJiN5ktkTNeDIVfN4wKBgAHwMeYeS8+f9SDeDTvpmkuJQKcsRaPqHkME8hLnFEADrdNxRpxy1bFjJU70Ye8HmggatyZ0DjpwMcfiC8c+CBfm+lXwQGaTvRWUypuZGiEhO8VsZfVWcLLyXiEAVl1ru52FeXuR60G7qhvApATQzfcUGYaQHlapmHWYdW9kyVSbAoGAE9dsrauQwimnNGPtOsKflOq8IUf1jWBkIbfi6M5RQ7w83ybw6VAyxzw95zOdYk0kVpfo1BCTsGJeJfWKelIvr3jlm2dd8eKZ3gB27ixCtzEIzp8S/yfNFReDq2oJR9BfrnIyz3NB4F8Vu762KQg5WbOGSokwJz/lDYooY5XnF4I=",
                "json",
                "UTF-8",
                //支付宝公钥
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvRrBEEG/ldpgkNULwytb6hYPiQ2X8ZE6ltq6oEb4zWT5ZI4/KyqY7E1ZNfZXnq6/N0HKNWP5QU09HjPkVp0uXdPq6TdmYxXDITbrE4Z38CNvWhWPnhtuUdRa5PWdqconLMhCkfawWkwYPSAU1Lg5Gr0GH84UMVSMX9rdtqM7e4wYcFaNAFENTJuYNDjHrTjKtS8ssPCohWZDdXPSWsNRmpAp2UYesgibBKZhcMDK4aYJDQTwKos11H3HDRScZy/xuvAYGJOf+nHDzFepDzbFsAGNUAf3JCzjbfR05EVXRWbxhQUlTjCB6a7pNgh4w6hM3n63gPJIxHB1prOhv7uVwQIDAQAB",
                "RSA2"); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        //设置同步响应的url
        alipayRequest.setReturnUrl("http://www.baidu.com");
        //设置异步响应的url

        alipayRequest.setNotifyUrl("http://www.baidu.com");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+order.getId()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+order.getOprice()+"," +
                "    \"subject\":\""+order.getOrderid()+"\"," +
                "    \"body\":\""+order.getOrderid()+"\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=utf-8");
        try {
            httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/isok")
    public String isok(String orderid){
        System.out.println("订单号为:"+orderid);
        //支付成功，跳到我的订单页面
        AlipayClient alipayClient = new DefaultAlipayClient(
                //沙箱的地址
                "https://openapi.alipaydev.com/gateway.do",
                "2016092000558664",
                //自己的私钥
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbgUz2hXlfTP3oxisFnlVcxCsxGHUm+fjagoam22QZrSfeY7rOpPEug9NO2FQIjp0bKHQCDbnHaSGQGZv1+diDtUawl8vtK1JPmfxFl37FdQ7qALqyoZo7rf6up1b4cIiYGEmi128xJu/+44UwBECPdeXcBvFekpfbJ6hUDYZ9FGC7fpp129CbRLJY10D+9GWkVJTKE+eRmrk7Edfbloohc5pu2zN4BZOEj0IBchsCvVVzkqR/GYXrJ5x4gvIeuGAYOqHM8/p4/nyeiuSCkdUHZclMbgWSbk5Jw40Dq5MhXPi2rx8B/Dq4AlI3yVLwI+YagdSswT/6QxdFvsOY88rhAgMBAAECggEBAIENCUX89wDz8DPhYYrEH6tg0UdpOLAj+zW75OtMXwJABTo2Ia2kjNNBa3efZ/3B/Pew1rVtlqO6x7PCzP+RXPHQ2t1q3D3zGdgdvpIya26K+tmPyWC9ZDXq69Ud4rtDCNjQLywJBaoQ2lL5lmqG3N0xxpSW3nDRz1x8yOEYaGgb0vPLdkKdkVV32d8tdSOjR99xyyqCOz++INBHGJ4IqPJWMp+ty87mCNR99qCg9QbG6V7XVhXuGt0zabI10MxLEOIcg2D274OZJxuUdsrhVS4RBgdjeOvmnKIUx4c89d6UeR6qg1b0Wu5MWef+4AaLR32mMclq0E49vFjgukDgDdECgYEA9WJ49QT/2YclmFE36+FxXZk6Fwi0mIwBbQ4XeO47UhW7T6CxwRyM5p/eDJi3a0h8PMuFKqNYonLaXkjIwVX0wxh3hNHF275u/pXzanHcOrxtgatfMMZEwGC3Z1QzLOcY0BAN3sHX4Tx2N+vcSySjl/uWKezPuqWBJbMx8d7oNhcCgYEAojtzg9LbrtdguCFhvuJQzSzaikY7kMJDuR7Ddw8W6rmnb9lwDYz17+1DtgJAiyC2/pwJiWKiU9Y46gmym5Ce3/uAItk0QW9g4Y2hUAHfYYyOHCxDQ874R5+TP0Da+dHphhPx9LArCxcpVOyipGWb6nFSR8/oD6wDqK2ehXeSmccCgYBfUvCwJlAJvfGB2VNDA+IZPTQOzKfzdrf7GHVP2iQbQCvyw/cpkKC/2qzU8eKW/6Kbr2g85xXAjaN86wNp761UuMV9SEx3j+PobHISEMc/3gEgVWV2Oh9tKGU288OpluDRutrYhS8K5YT7Nlnuqv4ORYLRKJiN5ktkTNeDIVfN4wKBgAHwMeYeS8+f9SDeDTvpmkuJQKcsRaPqHkME8hLnFEADrdNxRpxy1bFjJU70Ye8HmggatyZ0DjpwMcfiC8c+CBfm+lXwQGaTvRWUypuZGiEhO8VsZfVWcLLyXiEAVl1ru52FeXuR60G7qhvApATQzfcUGYaQHlapmHWYdW9kyVSbAoGAE9dsrauQwimnNGPtOsKflOq8IUf1jWBkIbfi6M5RQ7w83ybw6VAyxzw95zOdYk0kVpfo1BCTsGJeJfWKelIvr3jlm2dd8eKZ3gB27ixCtzEIzp8S/yfNFReDq2oJR9BfrnIyz3NB4F8Vu762KQg5WbOGSokwJz/lDYooY5XnF4I=",
                "json",
                "UTF-8",
                //支付宝公钥
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvRrBEEG/ldpgkNULwytb6hYPiQ2X8ZE6ltq6oEb4zWT5ZI4/KyqY7E1ZNfZXnq6/N0HKNWP5QU09HjPkVp0uXdPq6TdmYxXDITbrE4Z38CNvWhWPnhtuUdRa5PWdqconLMhCkfawWkwYPSAU1Lg5Gr0GH84UMVSMX9rdtqM7e4wYcFaNAFENTJuYNDjHrTjKtS8ssPCohWZDdXPSWsNRmpAp2UYesgibBKZhcMDK4aYJDQTwKos11H3HDRScZy/xuvAYGJOf+nHDzFepDzbFsAGNUAf3JCzjbfR05EVXRWbxhQUlTjCB6a7pNgh4w6hM3n63gPJIxHB1prOhv7uVwQIDAQAB",
                "RSA2"); //获得初始化的AlipayClient
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + orderid + "\"" +
                "  }");
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println("相应为:"+response);
        if(response.isSuccess()){
            String tradeStatus = response.getTradeStatus();
            if("TRADE_SUCCESS".equals(tradeStatus)){//支付成功
                //修改订单修改状态,1代表已支付
                orderService.updateStatusByOrderId(orderid,1);
            }
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return "redirect:http://localhost:8087/order/myorder";
    }
}
