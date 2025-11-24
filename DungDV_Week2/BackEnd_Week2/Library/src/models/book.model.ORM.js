const { DataTypes } = require('sequelize');
const sequelize = require('../../config/database');

const Book = sequelize.define('Book', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  title: {
    type: DataTypes.STRING,
    allowNull: false,
    validate: {
      notEmpty: {
        msg: 'Title không được để trống'
      },
      len: {
        args: [3, 200],
        msg: 'Title phải từ 3-200 ký tự'
      }
    }
  },
  author: {
    type: DataTypes.STRING,
    allowNull: false,
    validate: {
      notEmpty: {
        msg: 'Author không được để trống'
      }
    }
  },
  year: {
    type: DataTypes.INTEGER,
    allowNull: true,
    validate: {
      min: {
        args: [1900],
        msg: 'Year phải >= 1900'
      },
      max: {
        args: [2100],
        msg: 'Year phải <= 2100'
      }
    }
  }
}, {
  tableName: 'books',
  timestamps: true
});

sequelize.sync()
  .then(() => {
    console.log('Book table synced');
  })
  .catch(err => {
    console.error('Error syncing table:', err);
  });

module.exports = Book;


