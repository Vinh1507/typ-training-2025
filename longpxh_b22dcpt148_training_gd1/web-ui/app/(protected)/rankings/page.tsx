import Title from "@/components/title/Title";
import Table, {Column} from "@/components/table/Table";
import {Container} from "@mui/material";
import RankingFilter from "@/components/filter/RankingFilter";


const columnNames = ["STT", "Họ và tên", "Mã sinh viên", "Làm đúng", "Đã thử"];


const columns: Column[] = [
    { label: "STT", key: ["stt"] },
    { label: "Tên", key: ["name"] },
    { label: "Mã sinh viên", key: ["studentCode"] },
    { label: "Làm đúng", key: ["correct"] },
    { label: "Đã thử", key: ["try"] }
];

const datas = [
    {
        id: 1,
        data: {
            stt: "1",
            name: "Long",
            studentCode:"B22DCPT148",
            correct:"20",
            try:"30"
        }

    },

    {
        id: 2,
        data: {
            stt: "2",
            name: "Quỳnh",
            studentCode:"B22DCPT148",
            correct:"20",
            try:"30"
        }

    },

];


export default function Rankings(){
    return(
        <Container>
            <Title titlePage = "Xếp hạng"/>
            <RankingFilter/>
            <Table columns={columns} rows={datas}/>

        </Container>
    )
}