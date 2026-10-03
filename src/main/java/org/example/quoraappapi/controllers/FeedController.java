package org.example.quoraappapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.quoraappapi.dtos.FeedResponse;
import org.example.quoraappapi.service.FeedService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public FeedResponse getFeed(Authentication authentication,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        return feedService.getFeed(authentication.getName(), page, size);
    }
}