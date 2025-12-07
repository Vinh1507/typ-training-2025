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
    data: {
        code: string,
        title: string,
        level: string,
        status: string
    }
}

export interface Column {
    label: string,
    key: (keyof Row["data"])[]
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
                                            return (
                                                <TableCell align="center" key={indexCol} sx={{ textAlign: "center" }}>
                                                    {
                                                        col.key.map((i) =>
                                                            item.data[i]
                                                        )
                                                    }
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
