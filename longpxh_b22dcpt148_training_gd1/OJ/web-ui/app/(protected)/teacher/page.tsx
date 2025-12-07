import DashboardTemplate from "@/components/layout/DashboardTemplate";


const itemDrawerList = [
    {
        label:"Xem danh sách lớp",
        href:`/teacher/[id]/classes`
    },
    {
        label:"Xem danh sách bài tập",
        href:"/teacher/[id]/problems"
    }
]
const namePage = "Trang quản lý cho Giáo viên"

export default function TeacherDashboard(){
    return(
        <DashboardTemplate itemDrawerList={itemDrawerList} namePage={namePage}  />
    );
}