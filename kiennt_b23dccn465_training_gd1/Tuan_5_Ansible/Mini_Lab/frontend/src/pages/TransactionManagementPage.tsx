import { useState, useEffect } from 'react'
import {
    Box, Typography, TextField, Button, Table, TableBody, TableCell, TableContainer,
    TableHead, TableRow, Paper, IconButton, InputAdornment, Modal,
    Switch, FormControlLabel, CircularProgress, FormControl, InputLabel, Select, MenuItem
} from '@mui/material'
import { Search, Edit, Delete } from '@mui/icons-material'
import DashboardLayout from '../components/DashboardLayout'
import EmptyState from '../components/EmptyState'
import api from '../api';

const modalStyle = {
    position: 'absolute' as 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: { xs: '90%', sm: 500 },
    bgcolor: 'background.paper',
    borderRadius: 2,
    boxShadow: 24,
    p: 4,
};

interface Transaction {
    id: number;
    transactionDate?: string; // Backend dùng transactionDate
    date?: string; // Fallback
    description: string;
    amount: number;
    category: string;
    isIncome: boolean;
    walletId: number;
}

interface Wallet {
    id: number;
    walletName: string;
    balance: number;
}

interface NewTransactionData {
    date: string;
    description: string;
    amount: string;
    category: string;
    isIncome: boolean;
    walletId: number | '';
}

const TransactionManagementPage = () => {
    const currentUserId = Number(localStorage.getItem('userId')) || null
    const [searchTerm, setSearchTerm] = useState('')
    const [isModalOpen, setIsModalOpen] = useState(false)
    const [transactions, setTransactions] = useState<Transaction[]>([])
    const [isLoading, setIsLoading] = useState(false);

    const [wallets, setWallets] = useState<Wallet[]>([]);
    const [budgetList, setBudgetList] = useState<string[]>([]);
    const [editingId, setEditingId] = useState<number | null>(null);

    // Danh sách categories mặc định
    const defaultCategories = [
        'Ăn uống',
        'Di chuyển',
        'Mua sắm',
        'Giải trí',
        'Y tế',
        'Giáo dục',
        'Hóa đơn',
        'Nhà cửa',
        'Quần áo',
        'Làm đẹp',
        'Thể thao',
        'Du lịch',
        'Quà tặng',
        'Từ thiện',
        'Khác'
    ];

    const [newTransaction, setNewTransaction] = useState<NewTransactionData>({
        date: new Date().toISOString().split('T')[0],
        description: '',
        amount: '',
        category: '',
        isIncome: false,
        walletId: '',
    });

    const formatCurrency = (amount: number) => {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
    };

    const formatDate = (dateString: string | undefined) => {
        if (!dateString) return "";
        // Xử lý cả format từ backend (transactionDate) và format cũ (date)
        const date = dateString.includes('T') ? dateString.split('T')[0] : dateString;
        const [year, month, day] = date.split('-');
        return `${day}/${month}/${year}`;
    };

    const fetchTransactions = async () => {
        if (!currentUserId) {
            setTransactions([]);
            return;
        }
        setIsLoading(true);
        try {
            const response = await api.get('/transactions', {
                params: {
                    searchTerm: searchTerm || undefined,
                    page: 0, limit: 100, sortBy: 'date', order: 'DESC', userId: currentUserId
                }
            });
            // Backend trả về Page object, lấy content từ đó
            const transactionsData = response.data.content || response.data || [];
            console.log('✅ Fetched transactions from API:', transactionsData.length, 'items');
            // Map dữ liệu từ backend format sang frontend format
            const mappedTransactions = transactionsData.map((t: any) => ({
                id: t.id,
                date: t.date || t.transactionDate, // Backend trả về 'date' (LocalDate)
                transactionDate: t.date || t.transactionDate,
                description: t.description || '',
                amount: t.amount || 0,
                category: t.category || '',
                isIncome: t.isIncome || false,
                walletId: t.walletId || 0,
            }));
            setTransactions(mappedTransactions);
        } catch (error: any) {
            console.error('❌ Error fetching transactions:', error);
            // Nếu lỗi 403, có thể do backend chưa restart hoặc token không hợp lệ
            if (error.response?.status === 403) {
                console.error('403 Forbidden - Backend có thể chưa restart sau khi sửa SecurityConfig');
                alert('Lỗi 403: Backend có thể chưa restart. Vui lòng restart backend và refresh trang.');
            }
            setTransactions([]); // Set empty array on error
        } finally {
            setIsLoading(false);
        }
    };

    const fetchWallets = async () => {
        if (!currentUserId) return;
        try {
            const res = await api.get('/wallets', {
                params: { userId: currentUserId }
            });
            setWallets(res.data || []);
        } catch (error: any) {
            console.error('Error fetching wallets:', error);
            setWallets([]);
        }
    }

    const fetchBudgets = async () => {
        if (!currentUserId) return;
        try {
            const currentMonth = new Date().toISOString().slice(0, 7);
            const res = await api.get(`/budgets`, { params: { userId: currentUserId, month: currentMonth } });
            const categories = (res.data || []).map((item: any) => item.category);
            setBudgetList(categories);
        } catch (error: any) {
            console.error('Error fetching budgets:', error);
            setBudgetList([]);
        }
    }

    useEffect(() => {
        fetchTransactions();
        fetchWallets();
        fetchBudgets();
    }, [searchTerm, currentUserId]);

    const handleOpenModal = () => {
        setEditingId(null);
        setNewTransaction({
            date: new Date().toISOString().split('T')[0],
            description: '',
            amount: '',
            category: '',
            isIncome: false, // Mặc định là chi tiêu
            walletId: wallets.length > 0 ? wallets[0].id : '',
        });
        setIsModalOpen(true);
    };

    const handleCloseModal = () => setIsModalOpen(false);

    const handleEditClick = (transaction: Transaction) => {
        setEditingId(transaction.id);
        // Xử lý cả transactionDate (backend) và date (fallback)
        const dateValue = transaction.transactionDate || transaction.date || new Date().toISOString().split('T')[0];
        setNewTransaction({
            date: dateValue,
            description: transaction.description,
            amount: transaction.amount.toString(),
            category: transaction.category,
            isIncome: transaction.isIncome,
            walletId: transaction.walletId || '',
        });
        setIsModalOpen(true);
    };

    const handleChange = (e: any) => {
        const { name, value, type, checked } = e.target;
        setNewTransaction(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value,
        }));
    };

    // --- LOGIC LƯU (ĐÃ CẬP NHẬT) ---
    const handleSaveTransaction = async () => {
        // 1. Validate cơ bản (không check category ở đây nữa)
        if (!newTransaction.description || !newTransaction.amount || !newTransaction.walletId) {
            alert('Vui lòng điền đầy đủ thông tin (Mô tả, Số tiền, Ví).');
            return;
        }

        // 2. Nếu là Chi tiêu thì bắt buộc phải chọn Danh mục
        if (!newTransaction.isIncome && !newTransaction.category) {
            alert('Vui lòng chọn danh mục cho khoản chi tiêu này.');
            return;
        }

        const amountValue = parseFloat(newTransaction.amount);
        if (isNaN(amountValue) || amountValue <= 0) {
             alert('Số tiền không hợp lệ.');
             return;
        }

        // 3. Chuẩn bị dữ liệu gửi đi
        if (!currentUserId) {
            alert('Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.');
            return;
        }

        const dataToSend = {
            userId: currentUserId,
            date: newTransaction.date,
            description: newTransaction.description,
            amount: amountValue,
            isIncome: newTransaction.isIncome,
            walletId: Number(newTransaction.walletId),

            // QUAN TRỌNG: Nếu là Thu nhập -> Tự gán category là "Thu nhập"
            // Nếu là Chi tiêu -> Lấy category người dùng chọn
            category: newTransaction.isIncome ? 'Thu nhập' : newTransaction.category,
        };

        try {
            if (editingId) {
                await api.put(`/transactions/${editingId}`, dataToSend);
                alert('Cập nhật thành công!');
            } else {
                await api.post('/transactions', dataToSend);
                alert(newTransaction.isIncome ? 'Đã thêm thu nhập!' : 'Đã thêm chi tiêu!');
            }

            handleCloseModal();
            fetchTransactions();
            fetchWallets();
        } catch (error) {
            console.error('Lỗi khi lưu:', error);
            alert('Có lỗi xảy ra. Vui lòng thử lại.');
        }
    };

    const handleDelete = async (id: number) => {
        if(window.confirm("Xóa giao dịch này sẽ hoàn tiền lại vào ví. Bạn chắc chắn chưa?")) {
            try {
                await api.delete(`/transactions/${id}`);
                fetchTransactions();
                fetchWallets();
            } catch (error) { console.error(error); }
        }
    }

    if (!currentUserId) {
        return (
            <DashboardLayout>
                <Box>
                    <Typography variant="h6" color="error">
                        Không tìm thấy thông tin người dùng
                    </Typography>
                    <Typography>
                        Vui lòng đăng nhập lại để xem và quản lý giao dịch của bạn.
                    </Typography>
                </Box>
            </DashboardLayout>
        )
    }

    return (
        <DashboardLayout>
            <Box>
                <Typography variant="h5" sx={{ mb: 3, fontWeight: 600, color: '#2E5B47' }}>
                    Quản lý giao dịch
                </Typography>

                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3, gap: 2 }}>
                    <TextField
                        placeholder="Tìm kiếm..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        size="small"
                        sx={{ minWidth: '300px', "& .MuiOutlinedInput-root": { borderRadius: "25px", backgroundColor: "#fff" } }}
                        InputProps={{ startAdornment: (<InputAdornment position="start"><Search /></InputAdornment>) }}
                    />
                    <Button variant="contained" onClick={handleOpenModal} sx={{ borderRadius: '25px', backgroundColor: '#6B8E7F', '&:hover': { backgroundColor: '#2E5B47' } }}>
                        + Thêm giao dịch
                    </Button>
                </Box>

                <TableContainer component={Paper} sx={{ borderRadius: 2, boxShadow: 3 }}>
                    <Table>
                        <TableHead>
                            <TableRow sx={{ backgroundColor: '#6B8E7F' }}>
                                <TableCell sx={{ color: '#fff', fontWeight: 600 }}>Thời gian</TableCell>
                                <TableCell sx={{ color: '#fff', fontWeight: 600 }}>Nội dung</TableCell>
                                <TableCell sx={{ color: '#fff', fontWeight: 600 }}>Số tiền</TableCell>
                                <TableCell sx={{ color: '#fff', fontWeight: 600 }}>Danh mục</TableCell>
                                <TableCell sx={{ color: '#fff', fontWeight: 600 }}>Ví</TableCell>
                                <TableCell sx={{ color: '#fff', fontWeight: 600 }}>Hành động</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {isLoading ? (
                                <TableRow>
                                    <TableCell colSpan={6} align="center">
                                        <CircularProgress size={26} />
                                    </TableCell>
                                </TableRow>
                            ) : transactions.length === 0 ? (
                                <TableRow>
                                    <TableCell colSpan={6}>
                                        <EmptyState
                                            title="Chưa có giao dịch nào"
                                            description="Bạn chưa ghi nhận giao dịch nào. Bấm nút bên dưới để thêm giao dịch đầu tiên và bắt đầu theo dõi chi tiêu."
                                            actionText="+ Thêm giao dịch"
                                            onAction={handleOpenModal}
                                        />
                                    </TableCell>
                                </TableRow>
                            ) : (
                                transactions.map((transaction) => {
                                    const wallet = wallets.find(w => w.id === transaction.walletId);
                                    // Xử lý cả transactionDate (backend) và date (fallback)
                                    const displayDate = transaction.transactionDate || transaction.date || '';
                                    return (
                                        <TableRow key={transaction.id} hover>
                                            <TableCell>{formatDate(displayDate)}</TableCell>
                                            <TableCell>{transaction.description}</TableCell>
                                            <TableCell sx={{ color: transaction.isIncome ? '#4CAF50' : '#f44336', fontWeight: 600 }}>
                                                {transaction.isIncome ? '+' : '-'}{formatCurrency(transaction.amount)}
                                            </TableCell>
                                            <TableCell>{transaction.category}</TableCell>
                                            <TableCell sx={{ fontStyle: 'italic', color: '#555' }}>
                                                {wallet ? wallet.walletName : 'Ví đã xóa'}
                                            </TableCell>
                                            <TableCell>
                                                <IconButton size="small" sx={{ color: '#6B8E7F' }} onClick={() => handleEditClick(transaction)}><Edit /></IconButton>
                                                <IconButton size="small" sx={{ color: '#f44336' }} onClick={() => handleDelete(transaction.id)}><Delete /></IconButton>
                                            </TableCell>
                                        </TableRow>
                                    );
                                })
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Box>

            {/* Modal Form */}
            <Modal open={isModalOpen} onClose={handleCloseModal}>
                <Box sx={modalStyle}>
                    <Typography variant="h6" sx={{ mb: 2, color: '#2E5B47', fontWeight: 600 }}>
                        {editingId ? 'Cập nhật giao dịch' : 'Thêm giao dịch mới'}
                    </Typography>

                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>

                        <FormControlLabel
                            control={
                                <Switch
                                    checked={newTransaction.isIncome}
                                    onChange={handleChange}
                                    name="isIncome"
                                    color="success"
                                />
                            }
                            label={newTransaction.isIncome ? "Thu nhập (+)" : "Chi tiêu (-)"}
                        />

                        <FormControl fullWidth required>
                            <InputLabel id="select-wallet">Chọn Ví thanh toán *</InputLabel>
                            <Select
                                labelId="select-wallet"
                                name="walletId"
                                value={newTransaction.walletId}
                                label="Chọn Ví thanh toán *"
                                onChange={handleChange}
                            >
                                {wallets.length > 0 ? (
                                    wallets.map(w => (
                                        <MenuItem key={w.id} value={w.id}>
                                            {w.walletName} ({formatCurrency(w.balance)})
                                        </MenuItem>
                                    ))
                                ) : (
                                    <MenuItem value="" disabled>Bạn chưa có ví nào</MenuItem>
                                )}
                            </Select>
                        </FormControl>

                        {/* === ĐIỂM SỬA: CHỈ HIỆN DANH MỤC NẾU LÀ CHI TIÊU === */}
                        {!newTransaction.isIncome && (
                            <FormControl fullWidth required>
                                <InputLabel id="select-category">Danh mục *</InputLabel>
                                <Select
                                    labelId="select-category"
                                    name="category"
                                    value={newTransaction.category}
                                    label="Danh mục *"
                                    onChange={handleChange}
                                >
                                    {defaultCategories.map((cat, index) => (
                                        <MenuItem key={index} value={cat}>{cat}</MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                        )}

                        <TextField
                            label="Ngày giao dịch"
                            name="date"
                            type="date"
                            value={newTransaction.date}
                            onChange={handleChange}
                            fullWidth
                            InputLabelProps={{ shrink: true }}
                        />
                        <TextField
                            label="Mô tả / Nội dung"
                            name="description"
                            value={newTransaction.description}
                            onChange={handleChange}
                            fullWidth
                        />
                        <TextField
                            label="Số tiền (VND)"
                            name="amount"
                            type="number"
                            value={newTransaction.amount}
                            onChange={handleChange}
                            fullWidth
                        />

                        <Box sx={{ display: 'flex', justifyContent: 'end', gap: 2, mt: 2 }}>
                            <Button onClick={handleCloseModal}>Hủy</Button>
                            <Button variant="contained" onClick={handleSaveTransaction} sx={{ bgcolor: '#2E5B47' }}>
                                {editingId ? 'CẬP NHẬT' : 'LƯU'}
                            </Button>
                        </Box>
                    </Box>
                </Box>
            </Modal>
        </DashboardLayout>
    )
}

export default TransactionManagementPage