package com.example.demo.Controller;

import com.example.demo.Service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private RedisService redisService;

    private static final String LEADERBOARD_KEY = "leaderboard";

    @PostMapping("/add")
    public String addScore(@RequestParam String user, @RequestParam double score) {
        redisService.addToSortedSet(LEADERBOARD_KEY, user, score);
        return "Thêm/Cập nhật điểm cho user: " + user;
    }

    @GetMapping("/top")
    public Set<Object> getTopUsers(@RequestParam(defaultValue = "10") int limit) {
        return redisService.getTopFromSortedSet(LEADERBOARD_KEY, limit);
    }
}
