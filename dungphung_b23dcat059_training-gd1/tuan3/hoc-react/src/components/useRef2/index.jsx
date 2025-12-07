import { useState } from "react"
import { useRef } from "react";
function UseRef2(){
    //useRef dung de dem so lan render lai component
    const [inputValue,setInputValue]=useState("");
    const counterRef=useRef(0);
    const handleChange=(e)=>{
        setInputValue(e.target.value);
        counterRef.current=counterRef.current+1;
    }
    console.log(counterRef.current)
    return(
        <>
            <input type="text" value={inputValue} onChange={handleChange} />
        </>
    )
}
export default UseRef2