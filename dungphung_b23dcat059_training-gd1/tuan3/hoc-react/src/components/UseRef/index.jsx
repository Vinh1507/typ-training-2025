import { useRef, useState } from "react";

function UseRef(){
//     useRef trả về một object với thuộc tính current được khởi tạo thông qua tham số truyền vào.
// Object được trả về không bị khởi tạo lại khi component render lại.
// Giá trị trong object thay đổi nhưng component không bị render lại (useState thay đổi thì làm component render lại).
// Cú pháp: const tenBien = useRef(initialValue);
// Trong đó:
// initialValue: là giá trị khởi tạo.
// useRef được sử dụng để truy cập được các phần tử trong DOM.

    const [counter,setCounter]=useState(0);
    const counterRef=useRef(0);
    const counterObj={
        current:0
    }
    const handleClink=()=>{
        setCounter(counter+1);
        counterRef.current=counterRef.current+1;
        counterObj.current=counterRef.current+1;
    }
    console.log(counter)
    console.log(counterRef);
    console.log(counterObj)
    return(
        <>
            <button onClick={handleClink}>Counter</button>
        </>
    )
}
export default UseRef;