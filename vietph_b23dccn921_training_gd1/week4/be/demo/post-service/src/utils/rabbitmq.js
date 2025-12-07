import amqp from 'amqplib';
import logger from './logger.js';
import dotenv from "dotenv";
dotenv.config();
let connection=null;
let channel=null;;

const EXCHANGE_NAME='facebook_events';
export const connectToRabbitMQ = async () => {
    try {
        connection = await amqp.connect(process.env.RABBITMQ_URL);
        channel = await connection.createChannel();
        await channel.assertExchange(EXCHANGE_NAME, 'topic', { durable: false });
        logger.info('Connected to RabbitMQ');
        return channel;
    } catch (error) {
        logger.error(`Error connecting to RabbitMQ: ${error.message}`);
        throw error;
    }
} 

export const publishPostEvent = async (routingKey,message)=>{
    if(!channel){
        await connectToRabbitMQ();
    }
    channel.publish(EXCHANGE_NAME,routingKey,Buffer.from(JSON.stringify(message)));
    logger.info(`Published event to ${routingKey}`);
}