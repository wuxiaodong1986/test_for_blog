package com.test.controller;

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
            response.put("retCode", "0000");
            response.put("retMsg", "验签成功");
        }
        else
        {
            response.put("retCode", "0001");
            response.put("retMsg", "验签失败");
        }


        return response;
    }

}