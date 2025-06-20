import { Routes, Route, Navigate } from "react-router-dom";

import LoadingSpinner from "./components/LoadingSpiner";
import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<LoadingSpinner />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/register" element={<RegisterForm />} />
            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    );
};

export default AppRoutes;