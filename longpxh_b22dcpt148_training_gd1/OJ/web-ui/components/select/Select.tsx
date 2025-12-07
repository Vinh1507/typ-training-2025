"use client"

import * as React from 'react';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import SelectMui, { SelectChangeEvent } from '@mui/material/Select';
import SearchIcon from '@mui/icons-material/Search';
import {IconButton, ListSubheader, TextField} from "@mui/material";

interface Option {
    value: string,
    label: string
}

interface SelectProgs {
    options: Array<Option>,
    value: string,
    label: string,
    onChange: (value: string) => void
}

export default function Select({ options, value, label, onChange}: SelectProgs) {

    return (

        <FormControl fullWidth size="small">
            <InputLabel id="demo-simple-select-label" size="small">{label}</InputLabel>
            <SelectMui
                fullWidth
                size="small"
                labelId="demo-simple-select-label"
                id="demo-simple-select"
                value={value}
                label={label}
                onChange={event => {
                    onChange(event.target.value)
                }}
            >

                <ListSubheader>
                    <TextField
                        placeholder="Tìm kiếm"
                        size="small"
                        fullWidth
                        InputProps={{
                            endAdornment:(
                                <IconButton size="small">
                                    <SearchIcon/>
                                </IconButton>
                            )
                        }}

                    />

                </ListSubheader>

                {
                    options.map((item, index) => {
                        return (
                            <MenuItem key={index}  value={item.value}>{item.label}</MenuItem>

                        );
                    })
                }
            </SelectMui>
        </FormControl>

    )
}