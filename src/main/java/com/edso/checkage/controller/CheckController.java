package com.edso.checkage.controller;

import com.edso.checkage.service.CheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CheckController {

    private final CheckService service;

    public CheckController(CheckService service) {
        this.service = service;
    }

    @GetMapping("/check")
    Boolean checkAge() {
        log.info("Run check");
        return service.runCheck();
    }

}
