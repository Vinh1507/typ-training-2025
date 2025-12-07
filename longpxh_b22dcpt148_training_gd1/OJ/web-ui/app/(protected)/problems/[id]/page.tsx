"use client";

import Title from "@/components/title/Title";
import { Button, Card, Container, TextareaAutosize, TextField, Typography } from "@mui/material";
import { Grid } from "@mui/system";
import BreadCumb from "@/components/breadcumb/BreadCumb";
import { useParams, useRouter } from "next/navigation";
import Table, { Column } from "@/components/table/Table";
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';

interface ProblemDetailProg {
    code: string,
    title: string,
    level: string,
    status: string
}

export default function ProblemDetail({ code, title }: ProblemDetailProg) {

    const params = useParams();
    const id = params.id as string;

    const inherit = [
        {
            labelInherit: "Danh sách bài tập",
            hrefInherit: "/problems"
        }
    ]

    const primary = {
        labelPrimary: `Bài`,
        hrefPrimary: `/problems/${id} `
    }

    const titleProblem = `Bài ${id} `;

    const router = useRouter()

    const handleOnClick = (url: string) => {
        router.push(url)
    }


    const columns: Column[] = [
        { label: "Input", key: ["input"] },
        { label: "Output", key: ["output"] }
    ];

    const columnKq: Column[] = [
        { label: "Thời gian nộp", key: ["createdAt"] },
        { label: "Kết quả", key: ["result"] }
    ];

    const datas = [
        {
            id: 1,
            data: {
                input: "P001",
                output: "Two Sum"
            }

        },
        {
            id: 2,
            data: {
                input: "P002",
                output: "Two Sum 2"

            }
        }
    ];

    const results = [
        {
            id: 1,
            data: {
                createdAt: "21:35:19 01/12/2025",
                result: "AC"
            }

        },
        {
            id: 2,
            data: {
                createdAt: "21:35:19 01/12/2025",
                result: "RTE"

            }
        }
    ];

    const problemDescription = `This is a simple challenge to help you practice printing to stdout. You may also want to complete Solve Me First in C++ before attempting this challenge.
We're starting out by printing the most famous computing phrase of all time! In the editor below, use either printf or cout to print the string to stdout.
The more popular command form is cout. It has the following basic form:

cout << value_to_print << value_to_print;

Any number of values can be printed using one command as shown.

The printf command comes from C language. It accepts an optional format specification and a list of variables. Two examples for printing a string are:

printf("%s", string);
printf(string);

Note that neither method adds a newline. It only prints what you tell it to.`;


    return (
        <Container>
            {
                <BreadCumb
                    inherit={inherit}
                    primary={primary}
                    onClickBreadCumb={handleOnClick} />
            }
            <Title titlePage={titleProblem} />

            <Grid container spacing={2}>
                {/*Đề bài*/}
                <Grid size={{ xs: 12, md: 6 }}>
                    <Card sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 2, height: '100%' }}>
                        <Typography variant="h5" align="center" fontWeight="bold">Đề bài</Typography>

                        <Typography
                            component="pre"
                            sx={{
                                whiteSpace: 'pre-wrap',
                                fontFamily: 'inherit',
                                fontSize: '0.875rem',
                                margin: 0
                            }}
                        >
                            {problemDescription}
                        </Typography>

                        <Typography variant="h6" align="center" fontWeight="bold">Ví dụ</Typography>

                        <Table columns={columns} rows={datas} />
                    </Card>
                </Grid>

                {/* trả lời */}
                <Grid size={{ xs: 12, md: 6 }}>
                    <Card sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 2, height: '100%' }}>
                        <Typography variant="h5" align="center" fontWeight="bold">Trả lời</Typography>

                        <TextareaAutosize
                            aria-label="empty textarea"
                            placeholder="Nhập câu trả lời của bạn"
                            minRows={15}
                            maxRows={50}
                            className="w-full p-2.5 text-base rounded-md border border-gray-300 resize-y box-border flex-1"
                        />

                        <Button variant="outlined" className="h-10" fullWidth>Tìm kiếm</Button>

                    </Card>
                </Grid>
            </Grid>

            {/*kết quả*/}
            <Card className="mt-5" sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 2, height: '100%' }}>
                <Typography variant="h6" align="center" fontWeight="bold">Kết quả</Typography>

                <Table columns={columnKq} rows={results} />
            </Card>

        </Container>
    )
}