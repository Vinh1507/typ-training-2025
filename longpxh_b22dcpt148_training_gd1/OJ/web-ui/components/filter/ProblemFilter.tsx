"use client";

import {Grid, TextField} from "@mui/material";
import Filter from "./Filter";
import {useState} from "react";
import Select from "@/components/select/Select";



const ProblemTextFields = [
    {label:"Tìm theo mã bài", name:'code'},
    {label:"Tìm theo tên bài", name:'name'},
]

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

interface ProblemFilter{
    code: string,
    name: string,
    level: string
}

const ProblemFilterDefault = {
    code:'',
    name:'',
    level:''
}


export function ProblemFilter() {

    const [filterState, setFilterState] = useState<ProblemFilter>(ProblemFilterDefault);
    const handleChangeTextField = (value: string, name:string) =>{
       setFilterState((prev)=>(
           {
               ...prev,
               [name]: value,
           }
       ))
    }

    return (
        <Filter>
            {
                ProblemTextFields.map((item, index) => {
                    return (
                        <Grid key={index} size={{ xs: 12, md: 12, xl: 2 }}>
                            <TextField
                                fullWidth
                                size="small"
                                key={index}
                                label={item.label}
                                value={filterState[item.name as keyof ProblemFilter ]}
                                onChange={(e)=>handleChangeTextField(e.target.value, item.name)}
                            />
                        </Grid>
                    );
                })
            }

            <Grid size={{ xs: 12, md: 12, xl: 2 }}>
                <Select
                    value={filterState.level}
                    label="Tìm theo độ khó"
                    options={ProblemSelect}
                    onChange={(value)=>handleChangeTextField(value, 'level')}/>
            </Grid>


        </Filter>
    )
}