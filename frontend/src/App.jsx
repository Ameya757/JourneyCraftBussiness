import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginSignup from './Components/LoginSignup';
import Guide from './Components/Guide';
import Restaurant from './Components/Restaurant';
import LandingPage from './Components/LandingPage';
import ProtectedRoute from './Components/ProtectedRoute';
import { ToastContainer } from "react-toastify";
import { useSelector } from 'react-redux';

function App() {
  const {role , isAuthenticated} = useSelector((state) => state.auth);  
  return (
    <Router>
      <ToastContainer position="top-right" autoClose={3000} />
      <Routes>
      
        <Route path="/" element={<LandingPage />} />
        {!role && (
          <Route path="/loginsignup" element={<LoginSignup />} />)}
      
       {role === "GUIDE" && (
        <>
         <Route
          path="/guide"
          element={
            <ProtectedRoute>
              <Guide />
            </ProtectedRoute>
          }
        />
        </>
       )}
       {role === "RESTAURANT" && (
        <>
         <Route
          path="/restaurant"
          element={
            <ProtectedRoute>
              <Restaurant />
            </ProtectedRoute>
          }
        />
        </>
       )}
         <Route path='*' element={<Navigate to={isAuthenticated? "/" :"/loginsignup"} />} />
      </Routes>
    </Router>
  );
}

export default App;
