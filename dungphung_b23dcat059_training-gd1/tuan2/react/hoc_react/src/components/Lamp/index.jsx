import { useState } from "react";

function Lamp(){
    //let status=false;
    const [status,setStatus]=useState(false);
    const handleClink=()=>{       
        setStatus(!status);
        console.log(status)
    }
    return(
        <>
        <button onClick={handleClink}>Bat/tat</button>
        <div>{status? "den bat":"den tat"}</div>
        
        </>
    )
}
export default Lamp;