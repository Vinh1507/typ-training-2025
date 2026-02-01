"use client";


import Table, { Column } from "@/components/table/Table";
import { Container, Chip } from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";

import RadioButtonUncheckedIcon from "@mui/icons-material/RadioButtonUnchecked";
import Title from "@/components/title/Title";
import { ProblemFilter, ProblemFilterState } from "@/components/filter/ProblemFilter";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { api } from "@/lib/api";

// Status badge component
const StatusBadge = ({ statusCode, label }: { statusCode: string; label: string }) => {
    const getStatusConfig = () => {
        switch (statusCode) {
            case "COMPLETED":
                return {
                    color: "#2e7d32" as const,
                    bgColor: "#e8f5e9",
                    icon: <CheckCircleIcon sx={{ fontSize: 16 }} />,
                };
            case "NOT_COMPLETED":
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
    { label: "Độ khó", key: ["level"] },
    { label: "Trạng thái", key: ["status"], render: (value: string, row: any) => <StatusBadge statusCode={row.statusCode} label={value} /> }
];

const levelMap: Record<string, string> = {
    "EASY": "Dễ",
    "MEDIUM": "Trung bình",
    "HARD": "Khó"
};

export default function Problems() {

    const router = useRouter();
    const [rows, setRows] = useState<any[]>([]);

    const fetchProblems = async (filterState?: ProblemFilterState) => {
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

            const params = new URLSearchParams();
            if (filterState?.code) params.append('code', filterState.code);
            if (filterState?.title) params.append('title', filterState.title);
            if (filterState?.level) params.append('level', filterState.level);

            const queryString = params.toString();
            const url = `/api/problems${queryString ? `?${queryString}` : ''}`;

            const response: any = await api(url);
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

            // Filter by status if specified
            if (filterState?.status) {
                formattedRows = formattedRows.filter((row: any) => row.statusCode === filterState.status);
            }

            setRows(formattedRows);
        } catch (error) {
            console.error("Failed to fetch problems", error);
        }
    };

    useEffect(() => {
        fetchProblems();
    }, []);

    const handleSearch = (filterState: ProblemFilterState) => {
        fetchProblems(filterState);
    };

    return (
        <Container>
            <Title titlePage="Danh sách bài tập" />
            <ProblemFilter onSearch={handleSearch} />
            <Table
                columns={columns}
                rows={rows}
                onClick={(id: number) => router.push(`problems/${id}`)}
            />

        </Container>
    )
}