const { client } = require('../config/redis');

const LEADERBOARD_KEY = 'game:leaderboard';

// them hoac cap nhat score
async function updateScore(req, res) {
  const { playerId, score } = req.body;
  
  if (!playerId || score === undefined) {
    return res.status(400).json({ message: 'playerId and score required' });
  }
  
  try {
    await client.zAdd(LEADERBOARD_KEY, {
      score: parseFloat(score),
      value: String(playerId)
    });
    
    // lay rank moi
    const rank = await client.zRevRank(LEADERBOARD_KEY, String(playerId));
    
    console.log('[Leaderboard] Updated:', playerId, 'score:', score, 'rank:', rank + 1);
    
    res.json({
      message: 'Score updated',
      playerId,
      score,
      rank: rank + 1
    });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
}

// lay top N players
async function getTop(req, res) {
  const limit = parseInt(req.query.limit) || 10;
  
  try {
    const top = await client.zRangeWithScores(
      LEADERBOARD_KEY,
      0,
      limit - 1,
      { REV: true }
    );
    
    const result = top.map((item, index) => ({
      rank: index + 1,
      playerId: item.value,
      score: item.score
    }));
    
    res.json({ data: result, total: result.length });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
}

// lay rank cua player
async function getPlayerRank(req, res) {
  const { playerId } = req.params;
  
  try {
    const rank = await client.zRevRank(LEADERBOARD_KEY, playerId);
    const score = await client.zScore(LEADERBOARD_KEY, playerId);
    
    if (rank === null) {
      return res.status(404).json({ message: 'Player not found in leaderboard' });
    }
    
    res.json({
      playerId,
      rank: rank + 1,
      score
    });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
}

// lay players xung quanh 1 player (tren/duoi)
async function getAround(req, res) {
  const { playerId } = req.params;
  const range = parseInt(req.query.range) || 2;
  
  try {
    const rank = await client.zRevRank(LEADERBOARD_KEY, playerId);
    
    if (rank === null) {
      return res.status(404).json({ message: 'Player not found' });
    }
    
    const start = Math.max(0, rank - range);
    const end = rank + range;
    
    const players = await client.zRangeWithScores(
      LEADERBOARD_KEY,
      start,
      end,
      { REV: true }
    );
    
    const result = players.map((item, index) => ({
      rank: start + index + 1,
      playerId: item.value,
      score: item.score,
      isTarget: item.value === playerId
    }));
    
    res.json({ data: result });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
}

// reset leaderboard
async function reset(req, res) {
  try {
    await client.del(LEADERBOARD_KEY);
    res.json({ message: 'Leaderboard reset' });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
}

// them sample data
async function seed(req, res) {
  const players = [
    { playerId: 'player1', score: 1500 },
    { playerId: 'player2', score: 2300 },
    { playerId: 'player3', score: 1800 },
    { playerId: 'player4', score: 3100 },
    { playerId: 'player5', score: 2700 },
    { playerId: 'player6', score: 1200 },
    { playerId: 'player7', score: 2000 },
    { playerId: 'player8', score: 2500 }
  ];
  
  try {
    for (const p of players) {
      await client.zAdd(LEADERBOARD_KEY, {
        score: p.score,
        value: p.playerId
      });
    }
    
    res.json({ message: 'Sample data added', count: players.length });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
}

module.exports = { updateScore, getTop, getPlayerRank, getAround, reset, seed };

