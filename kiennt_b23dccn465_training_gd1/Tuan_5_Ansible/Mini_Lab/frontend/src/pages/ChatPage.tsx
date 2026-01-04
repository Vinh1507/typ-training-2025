import { useState, useEffect, useRef } from 'react'
import { 
  Box, 
  TextField, 
  IconButton, 
  Typography, 
  Avatar,
  Paper,
  InputAdornment,
  Chip
} from '@mui/material'
import { Send, SmartToy, Person, AttachFile, Mic } from '@mui/icons-material'
import DashboardLayout from '../components/DashboardLayout'
import { getQuickResponse } from '../services/geminiService'

interface Message {
  id: number
  text: string
  sender: 'user' | 'bot'
  timestamp: Date
}

const ChatPage = () => {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: 1,
      text: 'Xin chào! Tôi là trợ lý AI của Vissmart. Tôi có thể giúp bạn quản lý tài chính, phân tích chi tiêu và đưa ra lời khuyên về tiết kiệm. Bạn cần tôi giúp gì?',
      sender: 'bot',
      timestamp: new Date(),
    }
  ])
  const [inputMessage, setInputMessage] = useState('')
  const [isTyping, setIsTyping] = useState(false)
  const messagesEndRef = useRef<null | HTMLDivElement>(null)

  // Auto scroll to bottom when new message
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }

  useEffect(() => {
    scrollToBottom()
  }, [messages])

  const handleSendMessage = async () => {
    if (inputMessage.trim() === '') return

    const userMessageText = inputMessage.trim()
    
    // Add user message
    const userMessage: Message = {
      id: messages.length + 1,
      text: userMessageText,
      sender: 'user',
      timestamp: new Date(),
    }

    setMessages(prev => [...prev, userMessage])
    setInputMessage('')
    setIsTyping(true)

    try {
      // Gọi Gemini API
      const aiResponseText = await getQuickResponse(userMessageText)
      
      const aiResponse: Message = {
        id: messages.length + 2,
        text: aiResponseText,
        sender: 'bot',
        timestamp: new Date(),
      }
      setMessages(prev => [...prev, aiResponse])
    } catch (error) {
      console.error('Error getting AI response:', error)
      const errorResponse: Message = {
        id: messages.length + 2,
        text: '⚠️ Xin lỗi, có lỗi xảy ra khi xử lý yêu cầu của bạn. Vui lòng thử lại sau.',
        sender: 'bot',
        timestamp: new Date(),
      }
      setMessages(prev => [...prev, errorResponse])
    } finally {
      setIsTyping(false)
    }
  }

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSendMessage()
    }
  }

  // Quick suggestions
  const suggestions = [
    'Phân tích chi tiêu của tôi',
    'Làm sao để tiết kiệm?',
    'Xem ngân sách tháng này',
    'Tôi nên đầu tư gì?',
  ]

  const handleSuggestionClick = (suggestion: string) => {
    setInputMessage(suggestion)
  }

  return (
    <DashboardLayout>
      <Box sx={{ height: 'calc(100vh - 100px)', display: 'flex', flexDirection: 'column' }}>
        {/* Header */}
        <Box sx={{ 
          display: 'flex', 
          alignItems: 'center', 
          gap: 2, 
          p: 2, 
          borderBottom: '2px solid #f0f0f0',
          backgroundColor: 'white',
          borderRadius: '16px 16px 0 0',
        }}>
          <Avatar sx={{ bgcolor: '#6B8E7F', width: 48, height: 48 }}>
            <SmartToy />
          </Avatar>
          <Box>
            <Typography variant="h6" sx={{ fontWeight: 600, color: '#2E5B47' }}>
              Vissmart AI Assistant
            </Typography>
            <Typography variant="body2" sx={{ color: '#666' }}>
              Trợ lý tài chính thông minh
            </Typography>
          </Box>
          <Box sx={{ ml: 'auto' }}>
            <Chip 
              label="Online" 
              size="small" 
              sx={{ 
                bgcolor: '#4caf50', 
                color: 'white',
                fontWeight: 600 
              }} 
            />
          </Box>
        </Box>

        {/* Messages Area */}
        <Box sx={{ 
          flex: 1, 
          overflowY: 'auto', 
          p: 3,
          backgroundColor: '#f5f5f5',
          backgroundImage: 'linear-gradient(to bottom, #f5f5f5, #fafafa)',
        }}>
          {messages.map((message) => (
            <Box
              key={message.id}
              sx={{
                display: 'flex',
                justifyContent: message.sender === 'user' ? 'flex-end' : 'flex-start',
                mb: 2,
                gap: 1,
              }}
            >
              {message.sender === 'bot' && (
                <Avatar sx={{ bgcolor: '#6B8E7F', width: 36, height: 36 }}>
                  <SmartToy fontSize="small" />
                </Avatar>
              )}

              <Paper
                elevation={1}
                sx={{
                  maxWidth: '70%',
                  p: 2,
                  borderRadius: message.sender === 'user' ? '20px 20px 4px 20px' : '20px 20px 20px 4px',
                  backgroundColor: message.sender === 'user' ? '#6B8E7F' : 'white',
                  color: message.sender === 'user' ? 'white' : '#333',
                }}
              >
                <Typography 
                  variant="body1" 
                  sx={{ 
                    whiteSpace: 'pre-line',
                    lineHeight: 1.6,
                  }}
                >
                  {message.text}
                </Typography>
                <Typography 
                  variant="caption" 
                  sx={{ 
                    display: 'block', 
                    mt: 0.5,
                    opacity: 0.7,
                    fontSize: '0.7rem',
                  }}
                >
                  {message.timestamp.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })}
                </Typography>
              </Paper>

              {message.sender === 'user' && (
                <Avatar sx={{ bgcolor: '#D4A574', width: 36, height: 36 }}>
                  <Person fontSize="small" />
                </Avatar>
              )}
            </Box>
          ))}

          {/* Typing indicator */}
          {isTyping && (
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
              <Avatar sx={{ bgcolor: '#6B8E7F', width: 36, height: 36 }}>
                <SmartToy fontSize="small" />
              </Avatar>
              <Paper
                elevation={1}
                sx={{
                  p: 2,
                  borderRadius: '20px 20px 20px 4px',
                  backgroundColor: 'white',
                }}
              >
                <Box sx={{ display: 'flex', gap: 0.5 }}>
                  <Box sx={{ 
                    width: 8, 
                    height: 8, 
                    borderRadius: '50%', 
                    bgcolor: '#6B8E7F',
                    animation: 'bounce 1.4s infinite ease-in-out',
                    animationDelay: '0s',
                  }} />
                  <Box sx={{ 
                    width: 8, 
                    height: 8, 
                    borderRadius: '50%', 
                    bgcolor: '#6B8E7F',
                    animation: 'bounce 1.4s infinite ease-in-out',
                    animationDelay: '0.2s',
                  }} />
                  <Box sx={{ 
                    width: 8, 
                    height: 8, 
                    borderRadius: '50%', 
                    bgcolor: '#6B8E7F',
                    animation: 'bounce 1.4s infinite ease-in-out',
                    animationDelay: '0.4s',
                  }} />
                </Box>
              </Paper>
            </Box>
          )}

          <div ref={messagesEndRef} />
        </Box>

        {/* Quick Suggestions */}
        {messages.length <= 1 && (
          <Box sx={{ 
            p: 2, 
            backgroundColor: 'white',
            borderTop: '1px solid #f0f0f0',
          }}>
            <Typography variant="body2" sx={{ mb: 1, color: '#666', fontWeight: 500 }}>
              Gợi ý câu hỏi:
            </Typography>
            <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
              {suggestions.map((suggestion, index) => (
                <Chip
                  key={index}
                  label={suggestion}
                  onClick={() => handleSuggestionClick(suggestion)}
                  sx={{
                    backgroundColor: '#f5f5f5',
                    '&:hover': {
                      backgroundColor: '#6B8E7F',
                      color: 'white',
                    },
                    cursor: 'pointer',
                    transition: 'all 0.3s',
                  }}
                />
              ))}
            </Box>
          </Box>
        )}

        {/* Input Area */}
        <Box sx={{ 
          p: 2, 
          backgroundColor: 'white',
          borderTop: '2px solid #f0f0f0',
          borderRadius: '0 0 16px 16px',
        }}>
          <TextField
            fullWidth
            multiline
            maxRows={4}
            value={inputMessage}
            onChange={(e) => setInputMessage(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="Nhập tin nhắn của bạn..."
            variant="outlined"
            sx={{
              '& .MuiOutlinedInput-root': {
                borderRadius: '24px',
                backgroundColor: '#f5f5f5',
                '& fieldset': {
                  borderColor: 'transparent',
                },
                '&:hover fieldset': {
                  borderColor: '#6B8E7F',
                },
                '&.Mui-focused fieldset': {
                  borderColor: '#6B8E7F',
                },
              },
            }}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <IconButton size="small" sx={{ color: '#666' }}>
                    <AttachFile />
                  </IconButton>
                </InputAdornment>
              ),
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton size="small" sx={{ color: '#666', mr: 1 }}>
                    <Mic />
                  </IconButton>
                  <IconButton
                    onClick={handleSendMessage}
                    disabled={inputMessage.trim() === ''}
                    sx={{
                      backgroundColor: '#6B8E7F',
                      color: 'white',
                      '&:hover': {
                        backgroundColor: '#2E5B47',
                      },
                      '&.Mui-disabled': {
                        backgroundColor: '#ddd',
                        color: '#999',
                      },
                    }}
                  >
                    <Send fontSize="small" />
                  </IconButton>
                </InputAdornment>
              ),
            }}
          />
        </Box>

        {/* CSS for typing animation */}
        <style>
          {`
            @keyframes bounce {
              0%, 60%, 100% {
                transform: translateY(0);
              }
              30% {
                transform: translateY(-10px);
              }
            }
          `}
        </style>
      </Box>
    </DashboardLayout>
  )
}

export default ChatPage

