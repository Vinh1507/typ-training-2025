import {Typography} from "@mui/material";

interface TitleTypeProp{
    titlePage: string
}

export default function Title({titlePage}:TitleTypeProp) {
    return(
        <Typography variant="h4">{titlePage}</Typography>
    );
}