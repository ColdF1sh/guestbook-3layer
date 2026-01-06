package ua.edu.lab.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/demo")
public class DemoFieldInjectionController {

    @Autowired
    private Clock clock;

    @GetMapping("/clock")
    public Map<String, Object> getClock() {
        return Map.of(
            "message", "Clock endpoint - Field Injection Demo",
            "timestamp", Instant.now(clock).toString(),
            "injectionType", "Field Injection (@Autowired)",
            "clockZone", clock.getZone().toString(),
            "note", "Field injection is shown for demo purposes. Constructor injection is preferred."
        );
    }
}

