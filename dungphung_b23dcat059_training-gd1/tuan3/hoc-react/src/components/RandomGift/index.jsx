import { useRef, useState } from "react";

function RandomGift(){
    const gifts=["dien thoai","maytinh","xemay","oto"];
    const [result,setResult]=useState("");
    const counterRef=useRef(0);
    const handleRandom=()=>{
        if(counterRef.current<4){
            const random = Math.floor(Math.random()*gifts.length);
        setResult(gifts[random]);
        counterRef.current=counterRef.current+1;
        }
        else{
            alert("Ban da het luot")
        }
        
    }
    return(
        <>
        <button onClick={handleRandom}>Random</button>
        <div>{result}</div>
        </>
    )
}
export default RandomGift