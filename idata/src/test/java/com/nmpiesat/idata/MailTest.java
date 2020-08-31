package com.nmpiesat.idata;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {IdataApplication.class})
@AutoConfigureMockMvc  //测试接口用
public class MailTest {

//    @Autowired
//    private SendMail sendMail;

    @Test
    public void test(){
        System.out.println("要发发送邮件。。。");
//        sendMail.sendMessage();
        System.out.println("发送邮件。。。");
    }
}
