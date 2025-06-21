import { Routes, Route, Navigate } from "react-router-dom";

import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";
import EventDashboard from "./views/event/EventDashboard";
import CreateEventForm from "./views/event/CreateEventForm";

const AppRoutes = () => {
    return (
        <Routes>
            // Redirect to event dashboard
            <Route path="/" element={<Navigate to="/event" />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/register" element={<RegisterForm />} />
            <Route path="/event" element={<EventDashboard />} />
            <Route path="/event/add" element={<CreateEventForm />} />
            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    );
};

export default AppRoutes;