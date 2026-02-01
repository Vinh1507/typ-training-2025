"use client";

import { TextField } from "@mui/material";
import { Grid } from "@mui/system";
import Filter from "./Filter";
import { useState } from "react";
import Select from "@/components/select/Select";



const ProblemTextFields = [
    { label: "Tìm theo mã bài", name: 'code' },
    { label: "Tìm theo tên bài", name: 'title' },
]

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

const StatusSelect = [
    {
        value: "COMPLETED",
        label: "Đã AC"
    }, {
        value: "NOT_COMPLETED",
        label: "Chưa AC"
    },
]

export interface ProblemFilterState {
    code: string,
    title: string,
    level: string,
    status: string
}

const ProblemFilterDefault: ProblemFilterState = {
    code: '',
    title: '',
    level: '',
    status: ''
}

interface ProblemFilterProps {
    onSearch: (state: ProblemFilterState) => void;
}

export function ProblemFilter({ onSearch }: ProblemFilterProps) {

    const [filterState, setFilterState] = useState<ProblemFilterState>(ProblemFilterDefault);
    const handleChangeTextField = (value: string, name: string) => {
        setFilterState((prev) => (
            {
                ...prev,
                [name]: value,
            }
        ))
    }

    return (
        <Filter onSearch={() => onSearch(filterState)}>
            {
                ProblemTextFields.map((item, index) => {
                    return (
                        <Grid key={index} size={{ xs: 12, md: 6, xl: 2 }}>
                            <TextField
                                fullWidth
                                size="small"
                                key={index}
                                label={item.label}
                                value={filterState[item.name as keyof ProblemFilterState]}
                                onChange={(e) => handleChangeTextField(e.target.value, item.name)}
                            />
                        </Grid>
                    );
                })
            }

            <Grid size={{ xs: 12, md: 6, xl: 2 }}>
                <Select
                    value={filterState.level}
                    label="Tìm theo độ khó"
                    options={ProblemSelect}
                    onChange={(value) => handleChangeTextField(value, 'level')} />
            </Grid>

            <Grid size={{ xs: 12, md: 6, xl: 2 }}>
                <Select
                    value={filterState.status}
                    label="Trạng thái"
                    options={StatusSelect}
                    onChange={(value) => handleChangeTextField(value, 'status')} />
            </Grid>


        </Filter>
    )
}