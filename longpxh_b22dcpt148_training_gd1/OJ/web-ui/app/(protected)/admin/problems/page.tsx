"use client"

import {
    Alert,
    Button,
    Container,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle, Snackbar, SnackbarCloseReason, TextareaAutosize, TextField,
} from "@mui/material";
import Title from "@/components/title/Title";
import { ProblemFilter } from "@/components/filter/ProblemFilter";
import Table, { Column } from "@/components/table/Table";
import { useRouter } from "next/navigation";
import BreadCumb from "@/components/breadcumb/BreadCumb";
import React, { useState } from "react";
import CheckIcon from '@mui/icons-material/Check';
import Select from "@/components/select/Select";
import { MuiFileInput } from "mui-file-input";


const columns: Column[] = [
    { label: "Mã bài", key: ["code"] },
    { label: "Tên bài", key: ["title"] },
    { label: "Độ khó", key: ["level"] },
    { label: "Trạng thái", key: ["status"] }
];

const datas = [
    {
        id: 1,
        data: {
            code: "P001",
            title: "Two Sum",
            level: "Dễ",
            status: "Chưa làm"

        }

    },
    {
        id: 2,
        data: {
            code: "P002",
            title: "Two Sum 2",
            level: "Khó",
            status: "Chưa làm"

        }
    }
];

interface ProblemFilter {
    code: string,
    name: string,
    level: string
}

const ProblemFilterDefault = {
    code: '',
    name: '',
    level: ''
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
        value: "1",
        label: "Dễ"
    }, {
        value: "2",
        label: "Trung bình"
    }, {
        value: "3",
        label: "Khó"
    },
]

export default function AdminProblems() {

    const [open, setOpen] = React.useState(false);
    const [openSnack, setOpenSnack] = React.useState(false)
    const [filterState, setFilterState] = useState<ProblemFilter>(ProblemFilterDefault);
    const [showError, setShowError] = React.useState(false);
    const [value, setValue] = useState<File | null>(null);


    // Refs for form fields
    const titleRef = React.useRef<HTMLInputElement>(null);
    const descriptionRef = React.useRef<HTMLTextAreaElement>(null);
    const timeLimitRef = React.useRef<HTMLInputElement>(null);
    const memoryLimitRef = React.useRef<HTMLInputElement>(null);

    const handleClickOpen = () => {
        setShowError(false);
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        setShowError(false);
    };

    const handleAdd = () => {
        // Validate all fields
        const title = titleRef.current?.value || '';
        const description = descriptionRef.current?.value || '';
        const timeLimit = timeLimitRef.current?.value || '';
        const memoryLimit = memoryLimitRef.current?.value || '';
        const level = filterState.level;
        const file = value;

        if (!title.trim() || !description.trim() || !level || !timeLimit || !memoryLimit || !file) {
            setShowError(true);
            return;
        }

        setShowError(false);
        setOpenSnack(true)
        handleClose()
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

    const handleChangeTextField = (value: string, name: string) => {
        setFilterState((prev) => (
            {
                ...prev,
                [name]: value,
            }
        ))
    }

    const handleChange = (file: File | null) => {
        if (!file) return;

        const allowed = ["txt"]; // danh sách đuôi hợp lệ
        const ext = file.name.split(".").pop()?.toLowerCase();

        if (!ext || !allowed.includes(ext)) {
            alert("Chỉ cho phép file .txt");
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
            <ProblemFilter />
            <Table
                columns={columns}
                rows={datas}
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
                        <TextareaAutosize
                            aria-label="empty textarea"
                            placeholder="Nhập đề bài"
                            minRows={3}
                            maxRows={50}
                            ref={descriptionRef}
                            className="!mb-3 w-full p-2.5 text-base rounded-md border border-gray-300 resize-y box-border"
                        />
                        <Select
                            value={filterState.level}
                            label="Chọn độ khó"
                            options={ProblemSelect}
                            onChange={(value) => handleChangeTextField(value, 'level')} />
                        <TextField label="Thời gian (giây)" type="number" fullWidth className="!my-3" inputRef={timeLimitRef} />
                        <TextField label="Bộ nhớ (MB)" type="number" fullWidth className="!mb-3" inputRef={memoryLimitRef} />
                        <MuiFileInput fullWidth label="Tải file testcase (chỉ hỗ trợ dạng .txt)"
                            value={value}
                            inputProps={{
                                accept: ".txt"
                            }}
                            onChange={handleChange} />


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
        </Container>
    )
}