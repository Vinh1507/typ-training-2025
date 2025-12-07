import Title from "@/components/title/Title";
import {
    Container,
    Card,
    CardContent,
    Typography,
    Box,
    Chip,
    Divider,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Alert,
    Paper
} from "@mui/material";
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import TimerIcon from '@mui/icons-material/Timer';
import MemoryIcon from '@mui/icons-material/Memory';
import ErrorIcon from '@mui/icons-material/Error';
import CodeIcon from '@mui/icons-material/Code';
import TipsAndUpdatesIcon from '@mui/icons-material/TipsAndUpdates';
import InfoIcon from '@mui/icons-material/Info';

export default function Tutorial() {
    const statusList = [
        {
            code: "AC",
            name: "Accepted",
            description: "Kết quả đúng - Chương trình của bạn đã vượt qua tất cả test case",
            icon: <CheckCircleIcon />,
            color: "success" as const
        },
        {
            code: "WA",
            name: "Wrong Answer",
            description: "Kết quả sai - Output không khớp với đáp án đúng",
            icon: <CancelIcon />,
            color: "error" as const
        },
        {
            code: "TLE",
            name: "Time Limit Exceeded",
            description: "Quá giới hạn thời gian - Chương trình chạy quá chậm",
            icon: <TimerIcon />,
            color: "warning" as const
        },
        {
            code: "MLE",
            name: "Memory Limit Exceeded",
            description: "Quá giới hạn bộ nhớ - Chương trình sử dụng quá nhiều RAM",
            icon: <MemoryIcon />,
            color: "warning" as const
        },
        {
            code: "RTE",
            name: "Runtime Error",
            description: "Lỗi thực thi - Chương trình gặp lỗi khi chạy (ví dụ: chia cho 0, truy cập mảng ngoài phạm vi)",
            icon: <ErrorIcon />,
            color: "error" as const
        },
        {
            code: "CE",
            name: "Compile Error",
            description: "Lỗi biên dịch - Code có lỗi cú pháp, không thể compile",
            icon: <CodeIcon />,
            color: "error" as const
        },
        {
            code: "OLE",
            name: "Output Limit Exceeded",
            description: "Quá giới hạn đầu ra - Chương trình in ra quá nhiều dữ liệu",
            icon: <ErrorIcon />,
            color: "warning" as const
        },
        {
            code: "IR",
            name: "Invalid Return",
            description: "Trả về không hợp lệ - Giá trị trả về không đúng định dạng",
            icon: <ErrorIcon />,
            color: "error" as const
        }
    ];

    const tips = [
        "Đọc kỹ đề bài và xác định input/output rõ ràng",
        "Phân tích độ phức tạp thuật toán trước khi code",
        "Test code với các trường hợp biên (edge cases)",
        "Kiểm tra giới hạn của biến (overflow, underflow)",
        "Sử dụng kiểu dữ liệu phù hợp (int, long, double...)",
        "Comment code để dễ debug sau này",
        "Tối ưu code nếu bị TLE",
        "Giải phóng bộ nhớ đúng cách nếu bị MLE"
    ];

    return (
        <Container maxWidth="lg" sx={{ py: 4 }}>
            <Title titlePage="Hướng dẫn sử dụng Online Judge" />

            {/* Cách nộp bài */}
            <Card sx={{ mb: 4 }}>
                <CardContent>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                        <CodeIcon sx={{ mr: 1, color: 'primary.main' }} />
                        <Typography variant="h5" component="h2">
                            Cách nộp bài
                        </Typography>
                    </Box>
                    <Divider sx={{ mb: 2 }} />
                    <List>
                        <ListItem>
                            <ListItemIcon>
                                <Typography variant="h6" color="primary">1</Typography>
                            </ListItemIcon>
                            <ListItemText
                                primary="Chọn bài tập"
                                secondary="Vào mục 'Bài tập' và chọn một bài phù hợp với mức độ của bạn"
                            />
                        </ListItem>
                        <ListItem>
                            <ListItemIcon>
                                <Typography variant="h6" color="primary">2</Typography>
                            </ListItemIcon>
                            <ListItemText
                                primary="Đọc yêu cầu"
                                secondary="Đọc kỹ đề bài, chú ý đến format input/output và giới hạn"
                            />
                        </ListItem>
                        <ListItem>
                            <ListItemIcon>
                                <Typography variant="h6" color="primary">3</Typography>
                            </ListItemIcon>
                            <ListItemText
                                primary="Viết code"
                                secondary="Chọn ngôn ngữ lập trình và viết giải pháp của bạn"
                            />
                        </ListItem>
                        <ListItem>
                            <ListItemIcon>
                                <Typography variant="h6" color="primary">4</Typography>
                            </ListItemIcon>
                            <ListItemText
                                primary="Nộp bài"
                                secondary="Nhấn nút 'Submit' và đợi hệ thống chấm bài"
                            />
                        </ListItem>
                        <ListItem>
                            <ListItemIcon>
                                <Typography variant="h6" color="primary">5</Typography>
                            </ListItemIcon>
                            <ListItemText
                                primary="Xem kết quả"
                                secondary="Kiểm tra kết quả và debug nếu cần thiết"
                            />
                        </ListItem>
                    </List>
                </CardContent>
            </Card>

            {/* Các trạng thái kết quả */}
            <Card sx={{ mb: 4 }}>
                <CardContent>
                    <Typography variant="h5" component="h2" gutterBottom>
                        Các trạng thái kết quả
                    </Typography>
                    <Divider sx={{ mb: 3 }} />
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                        {statusList.map((status, index) => (
                            <Paper
                                key={index}
                                elevation={1}
                                sx={{
                                    p: 2,
                                    display: 'flex',
                                    alignItems: 'flex-start',
                                    gap: 2,
                                    '&:hover': {
                                        boxShadow: 3
                                    }
                                }}
                            >
                                <Box sx={{ color: `${status.color}.main`, mt: 0.5 }}>
                                    {status.icon}
                                </Box>
                                <Box sx={{ flex: 1 }}>
                                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 0.5 }}>
                                        <Chip
                                            label={status.code}
                                            color={status.color}
                                            size="small"
                                            sx={{ fontWeight: 'bold' }}
                                        />
                                        <Typography variant="subtitle1" fontWeight="bold">
                                            {status.name}
                                        </Typography>
                                    </Box>
                                    <Typography variant="body2" color="text.secondary">
                                        {status.description}
                                    </Typography>
                                </Box>
                            </Paper>
                        ))}
                    </Box>
                </CardContent>
            </Card>

            {/* Tips làm bài */}
            <Card>
                <CardContent>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                        <TipsAndUpdatesIcon sx={{ mr: 1, color: 'warning.main' }} />
                        <Typography variant="h5" component="h2">
                            Tips để đạt AC
                        </Typography>
                    </Box>
                    <Divider sx={{ mb: 2 }} />
                    <List>
                        {tips.map((tip, index) => (
                            <ListItem key={index}>
                                <ListItemIcon>
                                    <Chip
                                        label={index + 1}
                                        color="primary"
                                        size="small"
                                        sx={{ width: 32, height: 32 }}
                                    />
                                </ListItemIcon>
                                <ListItemText
                                    primary={tip}
                                    primaryTypographyProps={{ variant: 'body1' }}
                                />
                            </ListItem>
                        ))}
                    </List>
                </CardContent>
            </Card>

            {/* Footer note */}
            <Box sx={{ mt: 4, textAlign: 'center' }}>
                <Typography variant="body2" color="text.secondary">
                    💡 Luyện tập thường xuyên là chìa khóa để cải thiện kỹ năng lập trình!
                </Typography>
            </Box>
        </Container>
    );
}