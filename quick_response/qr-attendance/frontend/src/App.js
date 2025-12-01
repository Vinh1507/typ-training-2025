
import { Routes, Route } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import SuccessPage from './components/SuccessPage';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/success" element={<SuccessPage />} />
    </Routes>
  );
}

export default App;