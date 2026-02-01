"use client";

import {Container} from "@mui/material";
import Title from "@/components/title/Title";
import {ProblemFilter} from "@/components/filter/ProblemFilter";
import Table, {Column} from "@/components/table/Table";
import {useRouter} from "next/navigation";
import BreadCumb from "@/components/breadcumb/BreadCumb";

const columns: Column[] = [
    { label: "Tên tài khoản", key: ["username"] },
    { label: "Họ và tên ", key: ["fullName"] },
    { label: "Email", key: ["email"] },
    { label: "Vai trò", key: ["role"] },
    { label: "Trạng thái", key: ["status"] },
    { label: "Hành động", key: ["action"] }
];

const datas = [
    {
        id: 1,
        data:{
            username: "longpxh",
            fullName: "Phạm Xuân Hoàng Long",
            email: "longpxh@mail.com",
            role: "Sinh viên",
            status: "ACTIVE",
            action:""

        }

    },
    {
        id: 2,
        data:{
            username: "hungnh",
            fullName: "Nguyễn Huy Hùng",
            email: "hungnh@gmail.com",
            role: "Giảng viên",
            status: "ACTIVE",
            action:""

        }

    }
];


export default function Users(){

    const router = useRouter()

    const inherit = [
        {
            labelInherit: "Admin Dashboard",
            hrefInherit: "/admin"
        }
    ]

    const primary = {
        labelPrimary: `Danh sách người dùng`,
        hrefPrimary: `/admin/users `
    }


    return(
        <Container>
            <BreadCumb inherit={inherit} primary={primary}/>
            <Title titlePage="Danh sách người dùng" />
            <ProblemFilter />
            <Table
                columns={columns}
                rows={datas}
                onClick={(id:number) => router.push(`problems/${id}`)}
            />

        </Container>
    );
}