"use client";

import Title from "@/components/title/Title";
import { Alert, Button, Card, CircularProgress, Container, Snackbar, TextareaAutosize, Typography, Dialog, DialogTitle, DialogContent, IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import VisibilityIcon from "@mui/icons-material/Visibility";
import { Grid } from "@mui/system";
import BreadCumb from "@/components/breadcumb/BreadCumb";
import { useParams, useRouter } from "next/navigation";
import Table, { Column } from "@/components/table/Table";
import React, { useEffect, useState } from 'react';
import { api } from "@/lib/api";

interface Problem {
    id: number;
    problemCode: string;
    title: string;
    content: string;
    level: string;
    timeLimit: number;
    memoryLimit: number;
}

interface Submission {
    id: number;
    status: string;
    judgeResult: string | null;
    executionTime: number | null;
    submittedAt: string;
    code: string;
}

export default function ProblemDetail() {
    const params = useParams();
    const id = params.id as string;
    const router = useRouter();

    // States
    const [problem, setProblem] = useState<Problem | null>(null);
    const [code, setCode] = useState('');
    const [submissions, setSubmissions] = useState<any[]>([]);
    const [testCases, setTestCases] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const [submitting, setSubmitting] = useState(false);
    const [snackbar, setSnackbar] = useState<{ open: boolean; message: string; severity: 'success' | 'error' }>({
        open: false,
        message: '',
        severity: 'success'
    });
    const [viewCode, setViewCode] = useState<string | null>(null);
    const [openModal, setOpenModal] = useState(false);

    // Breadcrumb config
    const inherit = [
        {
            labelInherit: "Danh sách bài tập",
            hrefInherit: "/problems"
        }
    ];

    const primary = {
        labelPrimary: problem?.title || `Bài ${id}`,
        hrefPrimary: `/problems/${id}`
    };

    // Fetch problem details
    useEffect(() => {
        const fetchProblem = async () => {
            setLoading(true);
            try {
                const data = await api<Problem>(`/api/problems/${id}`);
                setProblem(data);
            } catch (error) {
                console.error("Failed to fetch problem:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchProblem();
    }, [id]);

    // Fetch test cases for examples
    useEffect(() => {
        const fetchTestCases = async () => {
            try {
                const data = await api<any>(`/api/problems/${id}/test-cases`);
                const formattedTestCases = (data || []).slice(0, 3).map((tc: any, index: number) => ({
                    id: index + 1,
                    data: {
                        input: tc.input,
                        output: tc.expectedOutput
                    }
                }));
                setTestCases(formattedTestCases);
            } catch (error) {
                console.error("Failed to fetch test cases:", error);
                setTestCases([]);
            }
        };

        fetchTestCases();
    }, [id]);

    // Fetch user submissions for this problem
    const fetchSubmissions = async () => {
        try {
            const data = await api<any>(`/api/submissions?problemId=${id}`);
            const formattedSubmissions = (data.content || data || []).map((s: Submission) => ({
                id: s.id,
                data: {
                    createdAt: new Date(s.submittedAt).toLocaleString('vi-VN'),
                    result: s.judgeResult || s.status,
                    code: s.code,
                    originalStatus: s.status, // Keep original status for logic
                    originalResult: s.judgeResult // Keep original result for logic
                }
            }));
            setSubmissions(formattedSubmissions);
            return formattedSubmissions;
        } catch (error) {
            console.error("Failed to fetch submissions:", error);
            return [];
        }
    };

    useEffect(() => {
        fetchSubmissions();
    }, [id]);

    // Polling for pending submissions
    useEffect(() => {
        let intervalId: NodeJS.Timeout;

        const checkPending = async () => {
            const hasPending = submissions.some(s =>
                ['PENDING', 'JUDGING', 'QUEUED'].includes(s.data.originalResult) ||
                ['PENDING', 'JUDGING', 'QUEUED'].includes(s.data.originalStatus)
            );

            if (hasPending) {
                await fetchSubmissions();
            }
        };

        if (submissions.some(s =>
            ['PENDING', 'JUDGING', 'QUEUED'].includes(s.data.originalResult) ||
            ['PENDING', 'JUDGING', 'QUEUED'].includes(s.data.originalStatus)
        )) {
            intervalId = setInterval(checkPending, 2000);
        }

        return () => {
            if (intervalId) clearInterval(intervalId);
        };
    }, [submissions, id]);

    // Handle submit code
    const handleSubmit = async () => {
        if (!code.trim()) {
            setSnackbar({ open: true, message: 'Vui lòng nhập code!', severity: 'error' });
            return;
        }

        setSubmitting(true);
        try {
            await api('/api/submissions', {
                method: 'POST',
                body: JSON.stringify({
                    problemId: parseInt(id),
                    code: code,
                    language: 'CPP'
                })
            });

            setSnackbar({ open: true, message: 'Nộp bài thành công!', severity: 'success' });

            // Refresh submissions list
            const data = await api<any>(`/api/submissions?problemId=${id}`);
            const formattedSubmissions = (data.content || data || []).map((s: Submission) => ({
                id: s.id,
                data: {
                    createdAt: new Date(s.submittedAt).toLocaleString('vi-VN'),
                    result: s.judgeResult || s.status,
                    code: s.code
                }
            }));
            setSubmissions(formattedSubmissions);

        } catch (error: any) {
            setSnackbar({ open: true, message: error.message || 'Nộp bài thất bại!', severity: 'error' });
        } finally {
            setSubmitting(false);
        }
    };

    const handleOnClick = (url: string) => {
        router.push(url);
    };

    const columns: Column[] = [
        { label: "Input", key: ["input"] },
        { label: "Output", key: ["output"] }
    ];

    const columnKq: Column[] = [
        { label: "Thời gian nộp", key: ["createdAt"] },
        {
            label: "Kết quả",
            key: ["result"],
            render: (value: string, row: any) => {
                const isPending = ['PENDING', 'JUDGING', 'QUEUED'].includes(row.data.originalResult) ||
                    ['PENDING', 'JUDGING', 'QUEUED'].includes(row.data.originalStatus) ||
                    value === 'PENDING' || value === 'JUDGING' || value === 'QUEUED';

                if (isPending) {
                    return (
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                            <CircularProgress size={16} />
                            <span>{value || "Đang chấm..."}</span>
                        </div>
                    );
                }

                let color = '#757575'; // default
                if (value === 'ACCEPTED' || value === 'AC') color = '#2e7d32'; // green
                else if (value === 'WRONG_ANSWER' || value === 'WA') color = '#d32f2f'; // red
                else if (value === 'COMPILATION_ERROR' || value === 'CE') color = '#ed6c02'; // orange
                else if (value === 'TIME_LIMIT_EXCEEDED' || value === 'TLE') color = '#ef5350'; // light red
                else if (value === 'RUNTIME_ERROR' || value === 'RTE' || value === 'RE') color = '#ff9800'; // dark orange
                else if (value === 'MEMORY_LIMIT_EXCEEDED' || value === 'MLE') color = '#9c27b0'; // purple

                return (
                    <span style={{ color: color, fontWeight: 600 }}>
                        {value}
                    </span>
                );
            }
        },
        {
            label: "Thao tác", key: ["action"], render: (_: any, row: any) => (
                <IconButton
                    size="small"
                    onClick={(e) => {
                        e.stopPropagation();
                        setViewCode(row.data.code);
                        setOpenModal(true);
                    }}
                >
                    <VisibilityIcon fontSize="small" />
                </IconButton>
            )
        }
    ];

    // Mock example data (TODO: get from API)
    const exampleData = [
        { id: 1, data: { input: "5 3", output: "8" } },
        { id: 2, data: { input: "10 20", output: "30" } }
    ];

    if (loading) {
        return (
            <Container sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}>
                <CircularProgress />
            </Container>
        );
    }

    return (
        <Container>
            <BreadCumb
                inherit={inherit}
                primary={primary}
                onClickBreadCumb={handleOnClick} />
            <Title titlePage={problem?.title || `Bài ${id}`} />

            <Grid container spacing={2}>
                {/* Đề bài */}
                <Grid size={{ xs: 12, md: 6 }}>
                    <Card sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 2, height: '100%' }}>
                        <Typography variant="h5" align="center" fontWeight="bold">Đề bài</Typography>

                        <Typography
                            component="pre"
                            sx={{
                                whiteSpace: 'pre-wrap',
                                fontFamily: 'inherit',
                                fontSize: '0.875rem',
                                margin: 0
                            }}
                        >
                            {problem?.content || 'Đang tải...'}
                        </Typography>

                        <Typography variant="body2" color="text.secondary">
                            <strong>Giới hạn:</strong> {problem?.timeLimit}s / {problem?.memoryLimit}MB
                        </Typography>

                        <Typography variant="h6" align="center" fontWeight="bold">Ví dụ</Typography>

                        {testCases.length > 0 ? (
                            <Table columns={columns} rows={testCases} />
                        ) : (
                            <Typography align="center" color="text.secondary">
                                Không có test case mẫu
                            </Typography>
                        )}
                    </Card>
                </Grid>

                {/* Trả lời */}
                <Grid size={{ xs: 12, md: 6 }}>
                    <Card sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 2, height: '100%' }}>
                        <Typography variant="h5" align="center" fontWeight="bold">Code của bạn (C++)</Typography>

                        <TextareaAutosize
                            aria-label="code input"
                            placeholder="#include <iostream>&#10;using namespace std;&#10;&#10;int main() {&#10;    // Nhập code của bạn&#10;    return 0;&#10;}"
                            minRows={15}
                            maxRows={50}
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            className="w-full p-2.5 text-base rounded-md border border-gray-300 resize-y box-border flex-1 font-mono"
                        />

                        <Button
                            variant="contained"
                            className="h-10"
                            fullWidth
                            onClick={handleSubmit}
                            disabled={submitting}
                        >
                            {submitting ? <CircularProgress size={24} /> : 'Nộp bài'}
                        </Button>
                    </Card>
                </Grid>
            </Grid>

            {/* Kết quả */}
            <Card className="mt-5" sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 2, height: '100%' }}>
                <Typography variant="h6" align="center" fontWeight="bold">Lịch sử nộp bài</Typography>

                {submissions.length > 0 ? (
                    <Table columns={columnKq} rows={submissions} />
                ) : (
                    <Typography align="center" color="text.secondary">
                        Chưa có bài nộp nào
                    </Typography>
                )}
            </Card>

            <Snackbar
                open={snackbar.open}
                autoHideDuration={4000}
                onClose={() => setSnackbar(prev => ({ ...prev, open: false }))}
            >
                <Alert severity={snackbar.severity} variant="filled">
                    {snackbar.message}
                </Alert>
            </Snackbar>

            <Dialog open={openModal} onClose={() => setOpenModal(false)} maxWidth="md" fullWidth>
                <DialogTitle sx={{ m: 0, p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    Chi tiết bài nộp
                    <IconButton
                        aria-label="close"
                        onClick={() => setOpenModal(false)}
                        sx={{
                            color: (theme) => theme.palette.grey[500],
                        }}
                    >
                        <CloseIcon />
                    </IconButton>
                </DialogTitle>
                <DialogContent dividers>
                    <Typography component="pre" sx={{ whiteSpace: 'pre-wrap', fontFamily: 'monospace' }}>
                        {viewCode || "Không có nội dung code"}
                    </Typography>
                </DialogContent>
            </Dialog>
        </Container>
    );
}