const express = require('express');
const router = express.Router();
const controller = require('../controllers/product.controller');

router.get('/', controller.getAll);
router.get('/category/:category', controller.getByCategory);
router.get('/:id', controller.getById);
router.get('/:id/cache-info', controller.getCacheInfo);
router.put('/:id', controller.update);

module.exports = router;

