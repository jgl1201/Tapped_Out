import { Navigate } from "react-router-dom";
import { authService } from "../service/authService";

const ConditionalRedirect = () => {
    const isAuthenticated = authService.isAuthenticated();

    if (!isAuthenticated) return <Navigate to="/event" replace />;

    const isAdmin = authService.isAdmin();
    
    return isAdmin ? <Navigate to="/admin" replace /> : <Navigate to="/event" replace />
};

export default ConditionalRedirect;