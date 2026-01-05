require('dotenv').config();
const amqp = require('amqplib');

const RABBITMQ_URL = process.env.RABBITMQ_URL || 'amqp://localhost';
const QUEUE_NAME = 'email_queue';

// luu message da xu ly de tranh duplicate
const processedMessages = new Set();

async function sendEmail(email, name, type) {
  console.log(`[Worker] Sending ${type} email to ${name} (${email})...`);
  
  // gia lap gui email
  await new Promise(resolve => setTimeout(resolve, 1000 + Math.random() * 1000));
  
  console.log(`[Worker] Email sent to ${email}`);
  return { success: true };
}

async function processMessage(msg) {
  try {
    const messageId = msg.properties.messageId;
    const data = JSON.parse(msg.content.toString());
    
    // check duplicate
    if (processedMessages.has(messageId)) {
      console.log('[Worker] Duplicate:', messageId);
      return true;
    }
    
    console.log('[Worker] Processing:', messageId);
    console.log('  Email:', data.email);
    console.log('  Type:', data.type);
    
    await sendEmail(data.email, data.name, data.type);
    
    processedMessages.add(messageId);
    
    // gioi han size
    if (processedMessages.size > 1000) {
      const first = processedMessages.values().next().value;
      processedMessages.delete(first);
    }
    
    return true;
  } catch (error) {
    console.error('[Worker] Error:', error.message);
    return false;
  }
}

async function startWorker() {
  try {
    console.log('[Worker] Starting...');
    
    const connection = await amqp.connect(RABBITMQ_URL);
    const channel = await connection.createChannel();
    
    await channel.assertQueue(QUEUE_NAME, { durable: true });
    channel.prefetch(1);
    
    console.log('[Worker] Connected, waiting for messages...');
    
    channel.consume(QUEUE_NAME, async (msg) => {
      if (msg) {
        const success = await processMessage(msg);
        
        if (success) {
          channel.ack(msg);
        } else {
          channel.nack(msg, false, true);
          console.log('[Worker] Message rejected, will retry');
        }
      }
    }, { noAck: false });
    
    process.on('SIGINT', async () => {
      console.log('\n[Worker] Shutting down...');
      await channel.close();
      await connection.close();
      process.exit(0);
    });
    
  } catch (error) {
    console.error('[Worker] Failed to start:', error.message);
    console.log('Make sure RabbitMQ is running');
    process.exit(1);
  }
}

startWorker();
