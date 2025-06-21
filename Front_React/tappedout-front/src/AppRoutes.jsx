import { Routes, Route, Navigate } from "react-router-dom";

import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";
import CreateEventForm from "./views/event/CreateEventForm";

import Test from "./views/Test";

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<Test />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/register" element={<RegisterForm />} />
            <Route path="/event/add" element={<CreateEventForm />} />
            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    );
};

export default AppRoutes;