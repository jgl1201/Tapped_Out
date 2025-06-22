import { Routes, Route, Navigate } from "react-router-dom";

import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";
import EventDashboard from "./views/event/EventDashboard";
import CreateEventForm from "./views/event/CreateEventForm";
import EventDetails from "./views/event/EventDetails";
import EventModifyForm from "./views/event/EventMoifyForm";

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/event" />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/register" element={<RegisterForm />} />
            <Route path="/event" element={<EventDashboard />} />
            <Route path="/event/add" element={<CreateEventForm />} />
            <Route path="/event/:id" element={<EventDetails />} />
            <Route path="/event/edit/:id" element={<EventModifyForm />} />
            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    );
};

export default AppRoutes;