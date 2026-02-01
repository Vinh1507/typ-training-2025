import Filter from "@/components/filter/Filter";
import { TextField} from "@mui/material";
import {Grid} from "@mui/system";


const RankingTextFields = [
    {
        label: "Tìm theo tên sinh viên"
    },
    {
        label: "Tìm theo mã sinh viên"
    }
]


export default function RankingFilter(){
    return(

        <Filter>
            {
                RankingTextFields.map((item, index) => {
                    return(
                        <Grid key= {index} size={{ xs: 12, md: 12, xl: 3 }}>
                            <TextField variant="outlined" className="h-10" fullWidth size="small" label={item.label} />
                        </Grid>
                    );
                })
            }
        </Filter>

    )
}