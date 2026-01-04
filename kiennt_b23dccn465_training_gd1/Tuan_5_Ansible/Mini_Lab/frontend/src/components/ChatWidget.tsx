import { useState, useEffect, useRef } from 'react'
import { 
  Box, 
  TextField, 
  IconButton, 
  Typography, 
  Avatar,
  Paper,
  InputAdornment,
  Chip,
  Fab,
  Fade,
  Slide,
} from '@mui/material'
import { 
  Send, 
  SmartToy, 
  Person, 
  Close, 
  Minimize,
  Chat,
} from '@mui/icons-material'
import { getQuickResponse } from '../services/geminiService'

interface Message {
  id: number
  text: string
  sender: 'user' | 'bot'
  timestamp: Date
}

const ChatWidget = () => {
  const [isOpen, setIsOpen] = useState(false)
  const [isMinimized, setIsMinimized] = useState(false)
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
  const [unreadCount, setUnreadCount] = useState(0)
  const messagesEndRef = useRef<null | HTMLDivElement>(null)

  // Auto scroll to bottom
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }

  useEffect(() => {
    scrollToBottom()
  }, [messages])

  // Reset unread when open
  useEffect(() => {
    if (isOpen) {
      setUnreadCount(0)
    }
  }, [isOpen])

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
      
      // If minimized or closed, increment unread
      if (isMinimized || !isOpen) {
        setUnreadCount(prev => prev + 1)
      }
    } catch (error) {
      console.error('Error getting AI response:', error)
      const errorResponse: Message = {
        id: messages.length + 2,
        text: '⚠️ Xin lỗi, có lỗi xảy ra khi xử lý yêu cầu của bạn. Vui lòng thử lại sau.',
        sender: 'bot',
        timestamp: new Date(),
      }
      setMessages(prev => [...prev, errorResponse])
      
      // If minimized or closed, increment unread
      if (isMinimized || !isOpen) {
        setUnreadCount(prev => prev + 1)
      }
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

  const suggestions = [
    'Chi tiêu tháng này?',
    'Làm sao để tiết kiệm?',
    'Xem ngân sách',
    'Thu nhập của tôi?',
    'Phân tích chi tiêu',
    'Lời khuyên đầu tư',
    'Mục tiêu tiết kiệm',
    'So sánh với tháng trước',
  ]

  const handleToggle = () => {
    setIsOpen(!isOpen)
    setIsMinimized(false)
  }

  const handleMinimize = () => {
    setIsMinimized(!isMinimized)
  }

  return (
    <>
      {/* Chat Bubble Button */}
      <Fade in={!isOpen}>
        <Fab
          color="primary"
          aria-label="chat"
          onClick={handleToggle}
          sx={{
            position: 'fixed',
            bottom: 24,
            right: 24,
            width: 64,
            height: 64,
            backgroundColor: '#6B8E7F',
            zIndex: 1300,
            '&:hover': {
              backgroundColor: '#2E5B47',
              transform: 'scale(1.1)',
            },
            transition: 'all 0.3s',
            boxShadow: '0 8px 24px rgba(107, 142, 127, 0.4)',
          }}
        >
          <Box sx={{ position: 'relative' }}>
            <Chat sx={{ fontSize: 32 }} />
            {unreadCount > 0 && (
              <Box
                sx={{
                  position: 'absolute',
                  top: -10,
                  right: -10,
                  backgroundColor: '#f44336',
                  color: 'white',
                  borderRadius: '50%',
                  width: 24,
                  height: 24,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '0.75rem',
                  fontWeight: 600,
                  border: '2px solid white',
                }}
              >
                {unreadCount}
              </Box>
            )}
          </Box>
        </Fab>
      </Fade>

      {/* Chat Window */}
      <Slide direction="up" in={isOpen} mountOnEnter unmountOnExit>
        <Paper
          elevation={8}
          sx={{
            position: 'fixed',
            bottom: isMinimized ? -440 : 24,
            right: 24,
            width: { xs: 'calc(100% - 48px)', sm: 380 },
            height: 550,
            borderRadius: '16px',
            overflow: 'hidden',
            display: 'flex',
            flexDirection: 'column',
            zIndex: 1300,
            transition: 'bottom 0.3s ease',
            boxShadow: '0 12px 48px rgba(0,0,0,0.2)',
          }}
        >
          {/* Header */}
          <Box sx={{ 
            display: 'flex', 
            alignItems: 'center', 
            gap: 2, 
            p: 2, 
            backgroundColor: '#6B8E7F',
            color: 'white',
          }}>
            <Avatar sx={{ bgcolor: 'rgba(255,255,255,0.3)', width: 40, height: 40 }}>
              <SmartToy />
            </Avatar>
            <Box sx={{ flex: 1 }}>
              <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                Vissmart AI
              </Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                <Box sx={{ 
                  width: 8, 
                  height: 8, 
                  borderRadius: '50%', 
                  bgcolor: '#4caf50',
                  animation: 'pulse 2s infinite',
                }} />
                <Typography variant="caption">
                  Online
                </Typography>
              </Box>
            </Box>
            <IconButton size="small" onClick={handleMinimize} sx={{ color: 'white' }}>
              <Minimize />
            </IconButton>
            <IconButton size="small" onClick={handleToggle} sx={{ color: 'white' }}>
              <Close />
            </IconButton>
          </Box>

          {/* Messages Area */}
          <Box sx={{ 
            flex: 1, 
            overflowY: 'auto', 
            p: 2,
            backgroundColor: '#f5f5f5',
          }}>
            {messages.map((message) => (
              <Box
                key={message.id}
                sx={{
                  display: 'flex',
                  justifyContent: message.sender === 'user' ? 'flex-end' : 'flex-start',
                  mb: 1.5,
                  gap: 1,
                }}
              >
                {message.sender === 'bot' && (
                  <Avatar sx={{ bgcolor: '#6B8E7F', width: 32, height: 32 }}>
                    <SmartToy fontSize="small" />
                  </Avatar>
                )}

                <Paper
                  elevation={1}
                  sx={{
                    maxWidth: '75%',
                    p: 1.5,
                    borderRadius: message.sender === 'user' ? '16px 16px 4px 16px' : '16px 16px 16px 4px',
                    backgroundColor: message.sender === 'user' ? '#6B8E7F' : 'white',
                    color: message.sender === 'user' ? 'white' : '#333',
                  }}
                >
                  <Typography 
                    variant="body2" 
                    sx={{ 
                      whiteSpace: 'pre-line',
                      lineHeight: 1.5,
                      fontSize: '0.9rem',
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
                      fontSize: '0.65rem',
                    }}
                  >
                    {message.timestamp.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })}
                  </Typography>
                </Paper>

                {message.sender === 'user' && (
                  <Avatar sx={{ bgcolor: '#D4A574', width: 32, height: 32 }}>
                    <Person fontSize="small" />
                  </Avatar>
                )}
              </Box>
            ))}

            {/* Typing indicator */}
            {isTyping && (
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1.5 }}>
                <Avatar sx={{ bgcolor: '#6B8E7F', width: 32, height: 32 }}>
                  <SmartToy fontSize="small" />
                </Avatar>
                <Paper elevation={1} sx={{ p: 1.5, borderRadius: '16px 16px 16px 4px' }}>
                  <Box sx={{ display: 'flex', gap: 0.5 }}>
                    {[0, 1, 2].map((i) => (
                      <Box
                        key={i}
                        sx={{ 
                          width: 6, 
                          height: 6, 
                          borderRadius: '50%', 
                          bgcolor: '#6B8E7F',
                          animation: 'bounce 1.4s infinite ease-in-out',
                          animationDelay: `${i * 0.2}s`,
                        }} 
                      />
                    ))}
                  </Box>
                </Paper>
              </Box>
            )}

            <div ref={messagesEndRef} />
          </Box>

          {/* Quick Suggestions - Always visible */}
          <Box sx={{ 
            p: 1.5, 
            backgroundColor: 'white', 
            borderTop: '1px solid #f0f0f0',
            maxHeight: 100,
            overflowY: 'auto',
            overflowX: 'hidden',
          }}>
            <Typography variant="caption" sx={{ color: '#666', mb: 0.5, display: 'block', fontWeight: 500 }}>
              💡 Gợi ý câu hỏi:
            </Typography>
            <Box sx={{ 
              display: 'flex', 
              gap: 0.5, 
              flexWrap: 'wrap',
              mt: 0.5,
            }}>
              {suggestions.map((suggestion, index) => (
                <Chip
                  key={index}
                  label={suggestion}
                  size="small"
                  onClick={() => setInputMessage(suggestion)}
                  sx={{
                    fontSize: '0.7rem',
                    backgroundColor: '#f5f5f5',
                    height: 24,
                    '&:hover': {
                      backgroundColor: '#6B8E7F',
                      color: 'white',
                    },
                    cursor: 'pointer',
                    transition: 'all 0.2s',
                  }}
                />
              ))}
            </Box>
          </Box>

          {/* Input Area */}
          <Box sx={{ p: 1.5, backgroundColor: 'white', borderTop: '1px solid #f0f0f0' }}>
            <TextField
              fullWidth
              size="small"
              multiline
              maxRows={3}
              value={inputMessage}
              onChange={(e) => setInputMessage(e.target.value)}
              onKeyPress={handleKeyPress}
              placeholder="Nhập tin nhắn..."
              variant="outlined"
              sx={{
                '& .MuiOutlinedInput-root': {
                  borderRadius: '20px',
                  backgroundColor: '#f5f5f5',
                  fontSize: '0.9rem',
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
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={handleSendMessage}
                      disabled={inputMessage.trim() === ''}
                      size="small"
                      sx={{
                        backgroundColor: '#6B8E7F',
                        color: 'white',
                        width: 32,
                        height: 32,
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

          {/* CSS Animations */}
          <style>
            {`
              @keyframes bounce {
                0%, 60%, 100% { transform: translateY(0); }
                30% { transform: translateY(-6px); }
              }
              @keyframes pulse {
                0%, 100% { opacity: 1; }
                50% { opacity: 0.5; }
              }
            `}
          </style>
        </Paper>
      </Slide>
    </>
  )
}

export default ChatWidget

