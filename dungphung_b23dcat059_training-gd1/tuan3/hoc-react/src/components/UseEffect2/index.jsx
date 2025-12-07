import { useEffect, useState } from "react";

function UseEffect2(){
    //useEffect(callback,[]) 
    //Khi render lại giao diện (tức render lần 2 trở đi), thì callback của useEffect không được gọi lại.
    // Thường áp dụng cho gọi API 1 lần để lấy dữ liệu.
    // Ví dụ: Lấy data từ api và render ra giao diện.
    const [data,setData]=useState([]);
    useEffect(()=>{
        fetch("https://dummyjson.com/products")
        .then(res=>res.json())
        .then(data=>{
            //console.log(data.products);
            setData(data.products);
        })
    },[])
    console.log(data)
    return(
        <>
            hi
        </>
    )
}
export default UseEffect2;