package com.example.demo.Controller;

import com.example.demo.Service.LeaderboardService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/leaderboards")
public class LeaderboardController {
    private final LeaderboardService svc;

    public LeaderboardController(LeaderboardService svc) {
        this.svc = svc;
    }

    @PostMapping("/{boardId}/score")
    public Map<String, Object> setScore(@PathVariable String boardId, @RequestBody Map<String, Object> body) {
        String member = (String) body.get("member");
        double score = Double.parseDouble(body.get("score").toString());
        svc.setScore(boardId, member, score);
        return Map.of("message", "OK", "boardId", boardId, "member", member, "score", score);
    }

    @PostMapping("/{boardId}/incr")
    public Map<String, Object> incr(@PathVariable String boardId, @RequestBody Map<String, Object> body) {
        String member = (String) body.get("member");
        double delta = Double.parseDouble(body.get("delta").toString());
        double newScore = svc.incrScore(boardId, member, delta);
        return Map.of("message", "OK", "boardId", boardId, "member", member, "newScore", newScore);
    }

    @GetMapping("/{boardId}/top")
    public Map<String, Object> top(@PathVariable String boardId, @RequestParam(defaultValue = "10") int n) {
        n = Math.max(1, Math.min(100, n));
        return Map.of("boardId", boardId, "top", svc.top(boardId, n));
    }
}
