import { useDispatch, useSelector } from "react-redux";
import { up } from "../../actions/counter";
import counterReducer from "../../reducers/counter";
function Counter(){
    const  counter=useSelector(state=>state.counter);
    const dispatch=useDispatch();
    console.log(counter)
    return(
        <>
        <div>Counter: {counter}</div>
        <button onClick={()=>dispatch(up())}>UP</button>
        <button onClick={()=>dispatch(up())}>DOWN</button>
        <button onClick={()=>dispatch(up())}>RESET</button>
        </>
    )
}
export default Counter;