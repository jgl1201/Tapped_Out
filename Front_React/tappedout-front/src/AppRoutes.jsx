import { Routes, Route, Navigate } from "react-router-dom";

import ConditionalRedirect from "./components/ConditionalRedirect";
import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";

import CreateEventForm from "./views/event/CreateEventForm";
import EventDashboard from "./views/event/EventDashboard";
import EventDetails from "./views/event/EventDetails";
import EventModifyForm from "./views/event/EventMoifyForm";

import InscriptionsDashboard from "./views/inscription/InscriptionDashboard";
import InscriptionForm from "./views/inscription/InscriptionForm";

import AdminDashboard from "./views/admin/AdminDashboard";
import GenderAdminDashboard from "./views/admin/GenderAdminDashboard";

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/" element={<ConditionalRedirect />} />

            <Route path="/login" element={<LoginForm />} />
            <Route path="/register" element={<RegisterForm />} />

            <Route path="/event" element={<EventDashboard />} />
            <Route path="/event/add" element={<CreateEventForm />} />
            <Route path="/event/:id" element={<EventDetails />} />
            <Route path="/event/edit/:id" element={<EventModifyForm />} />
            <Route path="/event/:id/register" element={<InscriptionForm />} />

            <Route path="/inscription" element={<InscriptionsDashboard />} />

            <Route path="/admin" element={<AdminDashboard />} />
            <Route path="/admin/genders" element={<GenderAdminDashboard />} />

            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    );
};

export default AppRoutes;