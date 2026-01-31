import Login from './pages/Login.jsx'
import {Route, Routes} from "react-router-dom";
import Home from "./pages/Home.jsx";
import ProductDetail from "./pages/ProductDetail.jsx";
import MainLayout from "./layouts/MainLayout.jsx";

function App() {

  return (
    <Routes>
        <Route path="/login" element={<Login />} />
        <Route element={ <MainLayout /> } >
            <Route path="/" element={<Home />} />
            <Route path="/product/:id" element={<ProductDetail />} />
        </Route>
    </Routes>
  )
}

export default App;
