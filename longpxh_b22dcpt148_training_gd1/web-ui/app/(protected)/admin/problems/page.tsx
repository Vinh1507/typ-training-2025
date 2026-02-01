"use client"

import {
    Alert,
    Button,
    Container,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle, Snackbar, SnackbarCloseReason, TextField,
    Chip
} from "@mui/material";
import { api } from "@/lib/api";
import Title from "@/components/title/Title";
import { ProblemFilter, ProblemFilterState } from "@/components/filter/ProblemFilter";
import Table, { Column } from "@/components/table/Table";
import BreadCumb from "@/components/breadcumb/BreadCumb";
import React, { useState } from "react";
import Select from "@/components/select/Select";
import { MuiFileInput } from "mui-file-input";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import RadioButtonUncheckedIcon from "@mui/icons-material/RadioButtonUnchecked";


// Status badge component (Copied and adapted from /problems)
const StatusBadge = ({ statusCode, label }: { statusCode: string; label: string }) => {
    const getStatusConfig = () => {
        switch (statusCode) {
            case "ACTIVE":
            case "COMPLETED":
                return {
                    color: "#2e7d32" as const,
                    bgColor: "#e8f5e9",
                    icon: <CheckCircleIcon sx={{ fontSize: 16 }} />,
                };
            case "NOT_COMPLETED":
            case "HIDDEN":
                return {
                    color: "#d32f2f" as const,
                    bgColor: "#ffebee",
                    icon: <RadioButtonUncheckedIcon sx={{ fontSize: 16 }} />,
                };
            default:
                return {
                    color: "#757575" as const,
                    bgColor: "#f5f5f5",
                    icon: <RadioButtonUncheckedIcon sx={{ fontSize: 16 }} />,
                };
        }
    };

    const config = getStatusConfig();

    return (
        <Chip
            icon={config.icon}
            label={label}
            size="small"
            sx={{
                backgroundColor: config.bgColor,
                color: config.color,
                fontWeight: 600,
                border: `1px solid ${config.color}`,
                "& .MuiChip-icon": {
                    color: config.color,
                },
            }}
        />
    );
};


const columns: Column[] = [
    { label: "Mã bài", key: ["code"] },
    { label: "Tên bài", key: ["title"] },
    { label: "Độ khó", key: ["level"] }, // Plain text as per /problems
    {
        label: "Trạng thái",
        key: ["status"],
        render: (value: string, row: any) => <StatusBadge statusCode={row.statusCode} label={value} />
    }
];


const ProblemFilterDefault: ProblemFilterState = {
    code: '',
    title: '',
    level: '',
    status: ''
}


const inherit = [
    {
        labelInherit: "Admin Dashboard",
        hrefInherit: "/admin"
    }
]

const primary = {
    labelPrimary: `Quản lý bài tập`,
    hrefPrimary: `/admin/problems} `
}

const ProblemSelect = [
    {
        value: "EASY",
        label: "Dễ"
    }, {
        value: "MEDIUM",
        label: "Trung bình"
    }, {
        value: "HARD",
        label: "Khó"
    },
]

const levelMap: Record<string, string> = {
    "EASY": "Dễ",
    "MEDIUM": "Trung bình",
    "HARD": "Khó"
};

export default function AdminProblems() {

    const [rows, setRows] = useState<any[]>([]);
    const [open, setOpen] = React.useState(false);
    const [openSnack, setOpenSnack] = React.useState(false)
    const [filterState, setFilterState] = useState<ProblemFilterState>(ProblemFilterDefault);
    const [showError, setShowError] = React.useState(false);
    const [value, setValue] = useState<File | null>(null);

    // Form state for adding new problem
    const [newProblemLevel, setNewProblemLevel] = useState<string>('');
    const titleRef = React.useRef<HTMLInputElement>(null);

    const fetchProblems = async () => {
        try {
            // Fetch user submissions to determine status
            let submittedProblemIds = new Set<number>();
            let acceptedProblemIds = new Set<number>();
            try {
                const submissionsData: any = await api('/api/submissions');
                const submissions = submissionsData.content || submissionsData || [];
                submissions.forEach((s: any) => {
                    if (s.problemId) {
                        submittedProblemIds.add(s.problemId);
                        if (s.judgeResult === 'ACCEPTED' || s.status === 'ACCEPTED' || s.judgeResult === 'AC') {
                            acceptedProblemIds.add(s.problemId);
                        }
                    }
                });
            } catch (subError) {
                console.warn("Could not fetch submissions for status", subError);
            }

            const queryParams = new URLSearchParams();
            if (filterState.code) queryParams.append('code', filterState.code);
            if (filterState.title) queryParams.append('title', filterState.title);
            if (filterState.level) queryParams.append('level', filterState.level);
            if (filterState.status) queryParams.append('status', filterState.status);

            const response: any = await api(`/api/problems?${queryParams.toString()}`);


            let formattedRows = response.map((p: any) => {
                let status = "Chưa AC";
                let statusCode = "NOT_COMPLETED";
                if (acceptedProblemIds.has(p.id)) {
                    status = "Đã AC";
                    statusCode = "COMPLETED";
                }
                return {
                    id: p.id,
                    statusCode: statusCode,
                    data: {
                        code: p.problemCode,
                        title: p.title,
                        level: levelMap[p.level] || p.level,
                        status: status
                    }
                };
            });

            // Filter by status if specified in local state (since backend might not filter by user specific status for all endpoints)
            if (filterState.status) {
                formattedRows = formattedRows.filter((row: any) => row.statusCode === filterState.status);
            }

            setRows(formattedRows);
        } catch (error) {
            console.error("Failed to fetch problems", error);
        }
    };

    React.useEffect(() => {
        fetchProblems();
    }, [filterState]);


    const handleClickOpen = () => {
        setShowError(false);
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        setShowError(false);
        setNewProblemLevel(''); // Reset form
        setValue(null);
    };

    const handleAdd = async () => {
        const title = titleRef.current?.value || '';
        const level = newProblemLevel;
        const file = value;

        if (!title.trim() || !level || !file) {
            setShowError(true);
            return;
        }

        try {
            const formData = new FormData();
            const problemData = {
                title,
                content: "From Zip",
                level,
                timeLimit: 1,
                memoryLimit: 1,
                classId: null
            };
            formData.append("data", new Blob([JSON.stringify(problemData)], { type: "application/json" }));
            formData.append("file", file);

            await api('/api/problems', {
                method: 'POST',
                body: formData
            });

            setShowError(false);
            setOpenSnack(true);
            await fetchProblems(); // Refresh list
            handleClose();
        } catch (error: any) {
            console.error(error);
            alert("Thêm bài tập thất bại: " + error.message);
        }
    }

    const handleCloseSnack = (
        event?: React.SyntheticEvent | Event,
        reason?: SnackbarCloseReason,
    ) => {
        if (reason === 'clickaway') {
            return;
        }

        setOpenSnack(false);
    };

    const handleChangeFile = (file: File | null) => {
        if (!file) return;

        const allowed = ["zip"];
        const ext = file.name.split(".").pop()?.toLowerCase();

        if (!ext || !allowed.includes(ext)) {
            alert("Chỉ cho phép file .zip chứa testcase");
            setValue(null);
            return;
        }

        setValue(file);
    };


    return (
        <Container>
            <BreadCumb inherit={inherit} primary={primary} />
            <div className="flex justify-between">
                <Title titlePage="Quản lý bài tập" />
                <Button onClick={handleClickOpen} variant="contained">Thêm bài tập</Button>
            </div>
            <ProblemFilter onSearch={setFilterState} />
            <Table
                columns={columns}
                rows={rows}
            />

            <Dialog
                open={open}
                onClose={handleClose}
            >
                <DialogTitle>
                    Thêm bài tập
                </DialogTitle>
                <DialogContent>
                    {showError && (
                        <Alert severity="error" className="!mb-3">
                            Vui lòng điền đủ các trường!
                        </Alert>
                    )}
                    <DialogContentText id="alert-dialog-description">
                        <TextField label="Tên bài" fullWidth className="!mb-3" inputRef={titleRef} />

                        <Select
                            value={newProblemLevel}
                            label="Chọn độ khó"
                            options={ProblemSelect}
                            onChange={(value) => setNewProblemLevel(value)} />

                        <MuiFileInput fullWidth label="Tải file testcase (.zip chứa .xml/.in/.out)"
                            value={value}
                            inputProps={{
                                accept: ".zip"
                            }}
                            onChange={handleChangeFile} />
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleAdd}>Thêm</Button>
                    <Button onClick={handleClose} autoFocus>
                        Hủy
                    </Button>
                </DialogActions>
            </Dialog>

            <Snackbar open={openSnack} autoHideDuration={6000} onClose={handleCloseSnack}>
                <Alert
                    onClose={handleCloseSnack}
                    severity="success"
                    variant="filled"
                    sx={{ width: '100%' }}
                >
                    Thêm bài tập thành công!
                </Alert>
            </Snackbar>
        </Container >
    )
}