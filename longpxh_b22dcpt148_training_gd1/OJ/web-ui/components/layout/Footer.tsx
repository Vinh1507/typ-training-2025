import {Box} from "@mui/system";
import {Typography} from "@mui/material";

export default function Footer() {
    return (
        <Box component="footer" sx={{ textAlign: 'center', p: 2, bgcolor: '#f0f0f0'}} className="mt-10">
            <Typography>Phát triển bởi Hoàng Long - 2025</Typography>
        </Box>
    )
}