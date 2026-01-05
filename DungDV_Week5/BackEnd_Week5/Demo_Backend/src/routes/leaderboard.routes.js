const express = require('express');
const router = express.Router();
const controller = require('../controllers/leaderboard.controller');

router.get('/top', controller.getTop);
router.get('/player/:playerId', controller.getPlayerRank);
router.get('/around/:playerId', controller.getAround);
router.post('/score', controller.updateScore);
router.post('/seed', controller.seed);
router.delete('/reset', controller.reset);

module.exports = router;

