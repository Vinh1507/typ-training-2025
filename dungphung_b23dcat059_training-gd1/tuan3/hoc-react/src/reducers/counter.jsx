const counterReducer=(state=0,action)=>{
    console.log(state,action);
    switch (action.type){
        case "UP":
            return state+1;
        case "DOWN":
            return state-1;
        case "RESET":
            return state=0;
        default:
            return state;
    }
    

}
export default counterReducer;