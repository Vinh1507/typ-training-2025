import { Route, BrowserRouter, Routes } from "react-router-dom";
import Login from "./Login";
import Confirm from "./Confirm";

// This is your main App component
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="confirm/:txId" element={<Confirm />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App;