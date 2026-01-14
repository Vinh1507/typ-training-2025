import { useEffect } from "react";

function UseEffect(){
    //useEffect dùng để xử lý logic nào đó khi data (dữ liệu) được thay đổi.
    //Component sau khi được render ra giao diện lần đầu, thì sẽ gọi tới hàm callback của useEffect (vì chúng ta ưu tiên việc render ra giao diện trước, xử lý logic sau nên callback của useEffect sẽ chạy sau khi component được render ra).
//1.useEffect(callback)
    //Khi render lại giao diện (tức render lần 2 trở đi), thì callback của useEffect vẫn được gọi lại.
    // Ví dụ: querySelectorAll các item trong DOM.
    //dung useeffect thi khi chay component nay thi se render ra giao dien truoc roi moi chay den ham queryselector.all 
    //cu phap useEffect(ham callback);

    useEffect(()=>{
        let listItem=document.querySelectorAll("ul li");
        console.log(listItem);
    })
    return(
        <>
            <ul>
                <li>1</li>
                <li>2</li>
                <li>3</li>
            </ul>
        </>
    )
}
export default UseEffect;