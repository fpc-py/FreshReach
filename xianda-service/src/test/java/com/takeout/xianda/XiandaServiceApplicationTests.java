package com.takeout.xianda;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootTest
class XiandaServiceApplicationTests {

    public static void main(String[] args) {
        String pwd ="123456";
        System.out.println(BCrypt.hashpw(pwd,BCrypt.gensalt()));
    }

}
