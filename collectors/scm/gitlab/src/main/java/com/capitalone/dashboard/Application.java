/**
 * Created by benathmane on 18/05/16.
 */
package com.capitalone.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

/**
 * Application configuration and bootstrap
 */
@SpringBootApplication
@EnableEncryptableProperties
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}