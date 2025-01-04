package com.example.urlshortener.controller;

import com.example.urlshortener.service.RequestProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class RequestProducerController {

  @Autowired
  private RequestProducerService requestProducerService;

  @GetMapping("/simulate-high-traffic")
  public String simulateHighTraffic() {
    requestProducerService.simulateHighTraffic();
    return "High traffic simulation started. Check logs for results.";
  }
}