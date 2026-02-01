"use client";

import TableMUI from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';


interface Row {
    id: number | null,
    data: Record<string, any>
}

export interface Column {
    label: string,
    key: string[],
    render?: (value: any, row: any) => React.ReactNode
}

interface TableProgs {
    columns: Column[],
    rows: Row[],
    onClick?: (id: number) => void
}

function Table({ columns, rows, onClick }: TableProgs) {
    return (
        <TableContainer component={Paper} sx={{ display: "inline-table" }}>
            <TableMUI aria-label="simple table">
                <TableHead>
                    <TableRow>

                        {
                            columns.map((item, index) => {
                                return (
                                    <TableCell align="center" key={index} sx={{ whiteSpace: "nowrap", textAlign: "center" }}>{item.label}</TableCell>
                                );
                            })
                        }

                    </TableRow>
                </TableHead>
                <TableBody>

                    {
                        rows.map((item, index) => {
                            return (

                                <TableRow key={index}
                                    sx={{
                                        "&:hover": {
                                            backgroundColor: "#f0f0f0",
                                            cursor: "pointer"
                                        }
                                    }}
                                    onClick={() => onClick?.(item.id!)}
                                >
                                    {
                                        columns.map((col, indexCol) => {
                                            const cellValue = col.key.map((i) => item.data[i]).join("");
                                            return (
                                                <TableCell align="center" key={indexCol} sx={{ textAlign: "center" }}>
                                                    {col.render ? col.render(cellValue, item) : cellValue}
                                                </TableCell>
                                            );
                                        })
                                    }

                                </TableRow>


                            );
                        })
                    }

                </TableBody>
            </TableMUI>
        </TableContainer>
    );
}

export default Table
