import {Alert, Container, Link, Typography} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";

export default function HomePage() {
    return (
        <Container>
            <Alert severity="info" icon={<InfoIcon />}  sx={{ mb: 4 }}>
                <Typography variant="h6" gutterBottom>
                    Chào mừng đến với hệ thống Online Judge C++!
                </Typography>
                <Typography variant="body2">
                    Đây là nơi bạn có thể luyện tập kỹ năng lập trình thông qua các bài tập thuật toán.
                    Hệ thống sẽ tự động chấm bài và trả về kết quả ngay lập tức.
                </Typography>
                <Typography variant="body2">
                    Bắt đầu ngay tại <Typography component={Link} variant="body2" href='/problems'>đây</Typography>
                </Typography>
            </Alert>
        </Container>
    );
}
