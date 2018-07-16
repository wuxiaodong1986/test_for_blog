package com.test.controller;

import com.google.gson.Gson;
import com.test.service.EthCryptographyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Keys;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 吴晓冬 on 2018/7/11.
 */
@RestController
public class EthCryptographyController
{
    @Autowired
    private EthCryptographyService ethCryptographyService;

    @PostMapping("/checkSign")
    public Map<String, Object> checkSign(@RequestBody Map<String, Object> map) throws Exception
    {
        String message = (String) map.get("message");
        String sign = (String) map.get("sign");
        String fromAddress = (String) map.get("address");

        String publicKey = ethCryptographyService.getKeyFromSign(message, sign);
        String address = "0x" + Keys.getAddress(publicKey);

        Map<String, Object> response = new HashMap<>();

        if (fromAddress.equals(address))
        {
            String privateKey = "2c5a384940835cdf5408a20658eae4c264045b22a5cc4f16ef5bb98f90a8ff93";
            String message2 = new Gson().toJson(map);

            String sign2 = ethCryptographyService.sign(message2, privateKey);
            response.put("retCode", "0000");
            response.put("retMsg", "验签成功");
            response.put("message", message2);
            response.put("address", "0x86af071d2f71239f2ddfe1c18664a9477c156ab1");
            response.put("sign", sign2);
        }
        else
        {
            response.put("retCode", "0001");
            response.put("retMsg", "验签失败");
        }


        return response;
    }

}