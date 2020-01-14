package com.shier.common.boot.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JpaApplication {

    public static void main(String[] args) {
        System.out.println(
                "           ||             \n" +
                        "           ||             \n" +
                        "           ||             \n" +
                        "===========||=============\n" +
                        "        // || \\\\        \n" +
                        "      //   ||   \\\\      \n" +
                        "    //     ||     \\\\    \n" +
                        "  //       ||       \\\\  \n" +
                        "//         ||         \\\\\n");
        SpringApplication.run(JpaApplication.class, args);

    }

}
