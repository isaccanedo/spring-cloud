package br.com.isaccanedo.microsservico02.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Microsservico01Controller {

    @GetMapping("/hello")
    public String hello() {
        return "Microsservico 02";
    }
}
