import DashboardTemplate from "@/components/layout/DashboardTemplate";


const itemDrawerList = [
    {
        label:"Quản lý người dùng",
        href:"/admin/users"
    },
    {
        label:"Quản lý bài tập",
        href:"/admin/problems"
    }
]
const namePage = "Trang Quản trị"

export default function AdminDashboard(){
    return(
      <DashboardTemplate itemDrawerList={itemDrawerList} namePage={namePage}  />
    );
}