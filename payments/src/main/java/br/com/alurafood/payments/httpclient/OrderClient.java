package br.com.alurafood.payments.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("order")
public interface OrderClient {

    @PutMapping("/order/{id}/paid")
    void updateOrder(@PathVariable Long id);

}
