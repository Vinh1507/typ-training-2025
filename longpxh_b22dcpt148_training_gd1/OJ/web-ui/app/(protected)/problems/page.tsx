"use client";


import Table, {Column} from "@/components/table/Table";
import { Container } from "@mui/material";
import Title from "@/components/title/Title";
import { ProblemFilter as ProblemFilter } from "@/components/filter/ProblemFilter";
import { useRouter } from "next/navigation";
import {Property} from "csstype";
import Columns = Property.Columns;


const columns: Column[] = [
    { label: "Mã bài", key: ["code"] },
    { label: "Tên bài", key: ["title"] },
    { label: "Độ khó", key: ["level"] },
    { label: "Trạng thái", key: ["status"] }
];

const datas = [
    {
        id: 1,
        data:{
            code: "P001",
            title: "Two Sum",
            level: "Dễ",
            status: "Chưa làm"

        }

    },
    {
        id: 2,
        data:{
            code: "P002",
            title: "Two Sum 2",
            level: "Khó",
            status: "Chưa làm"

        }
    }
];

export default function Problems() {

    const router = useRouter()

    return (
        <Container>
            <Title titlePage="Danh sách bài tập" />
            <ProblemFilter />
            <Table
                columns={columns}
                rows={datas}
                onClick={(id:number) => router.push(`problems/${id}`)}
            />

        </Container>
    )
}