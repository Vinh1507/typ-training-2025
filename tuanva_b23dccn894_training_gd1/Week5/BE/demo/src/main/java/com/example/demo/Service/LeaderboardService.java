package com.example.demo.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class LeaderboardService {
    private final RedisTemplate<String, Object> redisTemplate;

    public LeaderboardService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String key(String boardId) { return "lb:" + boardId; }

    public void setScore(String boardId, String member, double score) {
        redisTemplate.opsForZSet().add(key(boardId), member, score); // ZADD
    }

    public double incrScore(String boardId, String member, double delta) {
        Double res = redisTemplate.opsForZSet().incrementScore(key(boardId), member, delta); // ZINCRBY
        return res == null ? 0 : res;
    }

    public List<RankItem> top(String boardId, int n) {
        var zset = redisTemplate.opsForZSet();
        var tuples = zset.reverseRangeWithScores(key(boardId), 0, n - 1); // ZREVRANGE WITHSCORES
        if (tuples == null) return List.of();

        var list = tuples.stream().toList();
        return IntStream.range(0, list.size())
                .mapToObj(i -> new RankItem(i + 1,
                        (String) list.get(i).getValue(),
                        list.get(i).getScore() == null ? 0 : list.get(i).getScore()))
                .toList();
    }

    public record RankItem(int rank, String member, double score) {}
}
