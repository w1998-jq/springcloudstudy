package com.buba.springclould.order.controller;


import com.buba.springcloud.pojo.CommonResult;
import com.buba.springcloud.pojo.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class OrderController {
    //调用支付订单服务端的ip+端口号
    public static final  String PAYMENT_URL = "http://localhost:8001";


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    //创建支付订单的接口
    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment, CommonResult.class);
    }
    //获取id获取支付订单
    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
    }
    @GetMapping("/consumer/payment/getVarlable/{id}")
    public CommonResult<Payment> getPaymentVariableUrl(@PathVariable("id") Long id){
        ServiceInstance choose = loadBalancerClient.choose("mcroservice-payment");
        String url = String.format("http://%s:%s", choose.getHost(), choose.getPort());
        return restTemplate.getForObject(url+"/payment/get/"+id,CommonResult.class);
    }

    @GetMapping("/consumer/hello/{id}")
    public Object getHello(@PathVariable("id") Long id){
        ServiceInstance choose = loadBalancerClient.choose("mcroservice-payment");
        String url = String.format("http://%s:%s", choose.getHost(), choose.getPort());
        return restTemplate.getForObject(url+"/hello/"+id,String.class);
    }
}