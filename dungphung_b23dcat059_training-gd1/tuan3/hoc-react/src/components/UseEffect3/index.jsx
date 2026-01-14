import { useEffect, useState } from "react";
import "./style.css"
function UseEffect3(){
    // Khi render lại giao diện (tức render lần 2 trở đi), thì callback của useEffect được gọi lại khi dependency thay đổi.
    // Ví dụ: Làm pagination (phân trang).
    // 	Api dữ liệu mẫu: https://dummyjson.com/products?skip=0&limit=30

    const limit=10;
    const [data,setData]=useState([]);
    const [pageActive, setPageActive]=useState(0);
    const [quantityPage,setQuantityPage]=useState(0);
    useEffect(()=>{
        fetch(`https://dummyjson.com/products?skip=${pageActive*limit}&limit=${limit}`)
        .then(res=>res.json())
        .then(data=>{
            //console.log(data.products);
            setData(data.products);
            setQuantityPage(Math.ceil(data.total/limit))
        })
    },[pageActive])
    console.log(quantityPage)

    const handleClink=(e)=>{
        setPageActive(e);
    }
    return(
        <>
            <div className="listProduct">
                {data.map(item=>(
                    <div className="product_item">
                        <div className="product_image">
                            <img src={item.thumbnail} alt="" />
                        </div>
                        <h3 className="product_title">{item.title}</h3>
                        <div className="product_price">{item.price}</div>
                    </div>
                ))}
            </div>
            <ul className="pagination">
                {[...Array(quantityPage)].map((item,index)=>(
                     <li onClick={()=>handleClink(index)}>{index+1}</li>
                ))}
               
                
            </ul>
        </>
    )
}
export default UseEffect3;