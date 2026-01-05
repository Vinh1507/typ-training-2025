const amqp = require('amqplib');

const RABBITMQ_URL = process.env.RABBITMQ_URL || 'amqp://localhost';
const QUEUE_NAME = 'email_queue';

let connection = null;
let channel = null;

async function connect() {
  try {
    if (!connection) {
      connection = await amqp.connect(RABBITMQ_URL);
      channel = await connection.createChannel();
      
      await channel.assertQueue(QUEUE_NAME, { 
        durable: true
      });
      
      console.log('[MQ] Connected to RabbitMQ');
    }
    return { connection, channel };
  } catch (error) {
    console.error('[MQ] Connection error:', error.message);
    throw error;
  }
}

async function sendEmailMessage(userEmail, userName, messageType = 'welcome') {
  try {
    const { channel: ch } = await connect();
    
    const message = {
      email: userEmail,
      name: userName,
      type: messageType,
      timestamp: new Date().toISOString()
    };
    
    const messageId = `msg_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
    
    const sent = ch.sendToQueue(
      QUEUE_NAME,
      Buffer.from(JSON.stringify(message)),
      {
        persistent: true,
        messageId: messageId
      }
    );
    
    if (sent) {
      console.log('[MQ] Message sent:', messageId, '-', userEmail);
      return { success: true, messageId };
    } else {
      console.error('[MQ] Failed to send - queue full');
      return { success: false, error: 'Queue is full' };
    }
  } catch (error) {
    console.error('[MQ] Send error:', error.message);
    return { success: false, error: error.message };
  }
}

async function closeConnection() {
  try {
    if (channel) await channel.close();
    if (connection) await connection.close();
    console.log('[MQ] Connection closed');
  } catch (error) {
    console.error('[MQ] Close error:', error.message);
  }
}

module.exports = {
  sendEmailMessage,
  connect,
  closeConnection
};
