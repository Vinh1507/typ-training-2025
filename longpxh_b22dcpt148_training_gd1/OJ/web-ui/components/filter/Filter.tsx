"use client";

import { Button, Container, TextField } from "@mui/material";
import { ReactNode } from "react";
import { Grid } from "@mui/system";

interface FilterProgs {
    children: ReactNode
}

export default function Filter({ children }: FilterProgs) {

    return (
        <Grid container gap={3} className="!my-4" alignItems="flex-end">
            {children}
            <Grid size={{ xs: 12, md: 12, xl: 3 }}><Button variant="outlined" className="h-10" fullWidth>Tìm kiếm</Button></Grid>
        </Grid>
    )
}