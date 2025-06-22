import { useState, useEffect, useCallback } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";

import { Box, Button, Card, CardContent, Chip, Container, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Divider, Grid, Typography } from "@mui/material";
import { LocationOn as LocationIcon, Event as CalendarIcon,Schedule as TimeIcon,AttachMoney as MoneyIcon,PersonAdd as RegisterIcon,Edit as EditIcon, Delete as DeleteIcon, Info as InfoIcon } from "@mui/icons-material";

import { authService } from "../../service/authService";
import eventService from "../../service/eventService";
import LoadingSpinner from "../../components/LoadingSpiner";

const EventDetails = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [event, setEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState(null);
    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);

    // Usar useCallback para evitar el warning
    const loadEventDetails = useCallback(async () => {
        try {
            setLoading(true);
            const response = await eventService.getEventById(id);
            console.log('EVENT', response);
            console.log('EVENT ORGANIZER ID:', response.organizer.id); // Debug
            setEvent(response);
        } catch (error) {
            toast.error(error?.response?.message || "Error loading event details");
            navigate("/event");
        } finally {
            setLoading(false);
        }
    }, [id, navigate]);

    useEffect(() => {
        const currentUser = authService.getCurrentUser();
        console.log('CURRENT USER:', currentUser); // Debug
        setUser(currentUser);
        loadEventDetails();
    }, [loadEventDetails]);

    const handleRegister = () => {
        toast.info("Registration functionality coming soon.");
    };

    const handleEdit = () => {
        navigate(`/event/edit/${id}`);
    };

    const handleDeleteConfirm = async () => {
        try {
            await eventService.deleteEvent(id);
            toast.success("Event deleted successfully");
            navigate("/event");
        } catch (error) {
            toast.error(error?.response?.message || "Error deleting event");
        } finally {
            setDeleteDialogOpen(false);
        }
    };

    const formatDate = (dateString) => {
        try {
            const date = new Date(dateString);
            return date.toLocaleDateString('en-US', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
        } catch {
            return 'Date TBD';
        }
    };

    const formatTime = (dateString) => {
        try {
            const date = new Date(dateString);
            return date.toLocaleTimeString('en-US', {
                hour: '2-digit',
                minute: '2-digit'
            });
        } catch {
            return 'Time TBD';
        }
    };

    const formatFee = (fee) => {
        if (fee === 0 || fee === null || fee === undefined) {
            return 'Free';
        }
        return `$${parseFloat(fee).toFixed(2)}`;
    };

    // Lógica de permisos corregida
    const canRegister = user && authService.isCompetitor();
    
    // Verificar si el usuario es el organizador del evento
    const isEventOwner = user && event && (
        // Comparar IDs como strings y números
        String(user.userId) === String(event.organizer.id) ||
        user.userId === event.organizer.id
    );
    
    const canEdit = user && (
        authService.isAdmin() || 
        (authService.isOrganizer() && isEventOwner)
    );
    
    const canDelete = canEdit;

    // Debug logs
    console.log('Permission Debug:', {
        user,
        event: event ? { id: event.id, organizer: event.organizer.id } : null,
        userRole: authService.getCurrentUserRole(),
        isAdmin: authService.isAdmin(),
        isOrganizer: authService.isOrganizer(),
        isCompetitor: authService.isCompetitor(),
        isEventOwner,
        canRegister,
        canEdit,
        canDelete
    });

    if (loading) return <LoadingSpinner />;

    if (!event) {
        return (
            <Box
                sx={{
                    minHeight: '100vh',
                    backgroundColor: '#0d1117',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                }}
            >
                <Typography variant="h5" sx={{ color: '#aaa' }}>
                    Event not found
                </Typography>
            </Box>
        );
    }

    return (
        <Box
            sx={{
                minHeight: '100vh',
                backgroundColor: '#0d1117',
                py: 4
            }}
        >
            <Container maxWidth="lg">
                <Box sx={{ mb: 4 }}>
                    <Button
                        variant="outlined"
                        onClick={() => navigate('/event')}
                        sx={{
                            color: '#bb86fc',
                            borderColor: '#bb86fc',
                            mb: 3,
                            '&:hover': {
                                borderColor: '#e2baff',
                                backgroundColor: 'rgba(187, 134, 252, 0.1)'
                            }
                        }}
                    >
                        ← Back to Events
                    </Button>

                    <Typography
                        variant="h3"
                        sx={{
                            color: '#ffffff',
                            fontWeight: 'bold',
                            mb: 2
                        }}
                    >
                        {event.name}
                    </Typography>

                    {event.sport && (
                        <Chip
                            label={event.sport.name}
                            sx={{
                                backgroundColor: '#30363d',
                                color: '#bb86fc',
                                fontSize: '1rem',
                                height: 40,
                                mb: 2
                            }}
                        />
                    )}
                </Box>

                <Grid container spacing={4}>
                    <Grid item xs={12} md={8}>
                        <Card
                            sx={{
                                backgroundColor: '#161b22',
                                borderRadius: 3,
                                mb: 3
                            }}
                        >
                            <CardContent sx={{ p: 4 }}>
                                {event.logo && (
                                    <Box sx={{ textAlign: 'center', mb: 4 }}>
                                        <img
                                            src={event.logo}
                                            alt={`${event.name} logo`}
                                            style={{
                                                maxWidth: '200px',
                                                maxHeight: '200px',
                                                borderRadius: '8px'
                                            }}
                                            onError={(e) => {
                                                e.target.style.display = 'none';
                                            }}
                                        />
                                    </Box>
                                )}

                                {event.description && (
                                    <>
                                        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                                            <InfoIcon sx={{ color: '#bb86fc', mr: 1 }} />
                                            <Typography variant="h6" sx={{ color: '#ffffff' }}>
                                                Description
                                            </Typography>
                                        </Box>
                                        <Typography
                                            variant="body1"
                                            sx={{
                                                color: '#aaa',
                                                mb: 4,
                                                lineHeight: 1.6,
                                                whiteSpace: 'pre-wrap'
                                            }}
                                        >
                                            {event.description}
                                        </Typography>
                                        <Divider sx={{ borderColor: '#30363d', mb: 4 }} />
                                    </>
                                )}

                                <Grid container spacing={3}>
                                    <Grid item xs={12} sm={6}>
                                        <Box sx={{ display: 'flex', alignItems: 'flex-start', mb: 3 }}>
                                            <LocationIcon sx={{ color: '#bb86fc', mr: 2, mt: 0.5 }} />
                                            <Box>
                                                <Typography variant="h6" sx={{ color: '#ffffff', mb: 1 }}>
                                                    Location
                                                </Typography>
                                                <Typography variant="body1" sx={{ color: '#aaa', mb: 0.5 }}>
                                                    {event.city}, {event.country}
                                                </Typography>
                                                {event.address && (
                                                    <Typography variant="body2" sx={{ color: '#666' }}>
                                                        {event.address}
                                                    </Typography>
                                                )}
                                            </Box>
                                        </Box>
                                    </Grid>

                                    <Grid item xs={12} sm={6}>
                                        <Box sx={{ display: 'flex', alignItems: 'flex-start', mb: 3 }}>
                                            <MoneyIcon sx={{ color: '#bb86fc', mr: 2, mt: 0.5 }} />
                                            <Box>
                                                <Typography variant="h6" sx={{ color: '#ffffff', mb: 1 }}>
                                                    Registration Fee
                                                </Typography>
                                                <Typography
                                                    variant="h4"
                                                    sx={{
                                                        color: event.registrationFee === 0 ? '#4caf50' : '#bb86fc',
                                                        fontWeight: 'bold'
                                                    }}
                                                >
                                                    {formatFee(event.registrationFee)}
                                                </Typography>
                                            </Box>
                                        </Box>
                                    </Grid>

                                    <Grid item xs={12} sm={6}>
                                        <Box sx={{ display: 'flex', alignItems: 'flex-start', mb: 3 }}>
                                            <CalendarIcon sx={{ color: '#bb86fc', mr: 2, mt: 0.5 }} />
                                            <Box>
                                                <Typography variant="h6" sx={{ color: '#ffffff', mb: 1 }}>
                                                    Start Date
                                                </Typography>
                                                <Typography variant="body1" sx={{ color: '#aaa', mb: 0.5 }}>
                                                    {formatDate(event.startDate)}
                                                </Typography>
                                                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                                    <TimeIcon sx={{ color: '#666', mr: 1, fontSize: 16 }} />
                                                    <Typography variant="body2" sx={{ color: '#666' }}>
                                                        {formatTime(event.startDate)}
                                                    </Typography>
                                                </Box>
                                            </Box>
                                        </Box>
                                    </Grid>

                                    <Grid item xs={12} sm={6}>
                                        <Box sx={{ display: 'flex', alignItems: 'flex-start', mb: 3 }}>
                                            <CalendarIcon sx={{ color: '#bb86fc', mr: 2, mt: 0.5 }} />
                                            <Box>
                                                <Typography variant="h6" sx={{ color: '#ffffff', mb: 1 }}>
                                                    End Date
                                                </Typography>
                                                <Typography variant="body1" sx={{ color: '#aaa', mb: 0.5 }}>
                                                    {formatDate(event.endDate)}
                                                </Typography>
                                                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                                    <TimeIcon sx={{ color: '#666', mr: 1, fontSize: 16 }} />
                                                    <Typography variant="body2" sx={{ color: '#666' }}>
                                                        {formatTime(event.endDate)}
                                                    </Typography>
                                                </Box>
                                            </Box>
                                        </Box>
                                    </Grid>
                                </Grid>
                            </CardContent>
                        </Card>
                    </Grid>

                    <Grid item xs={12} md={4}>
                        <Card
                            sx={{
                                backgroundColor: '#161b22',
                                borderRadius: 3,
                                mb: 3
                            }}
                        >
                            <CardContent sx={{ p: 3 }}>
                                <Typography variant="h6" sx={{ color: '#ffffff', mb: 2 }}>
                                    Event Organizer
                                </Typography>
                                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                    <Box
                                        sx={{
                                            width: 48,
                                            height: 48,
                                            borderRadius: '50%',
                                            backgroundColor: '#bb86fc',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            mr: 2
                                        }}
                                    >
                                        <Typography variant="h6" sx={{ color: '#ffffff' }}>
                                            {event.organizer?.name?.charAt(0) || 'O'}
                                        </Typography>
                                    </Box>
                                    <Box>
                                        <Typography variant="subtitle1" sx={{ color: '#ffffff' }}>
                                            {event.organizer?.name || 'Unknown Organizer'}
                                        </Typography>
                                        <Typography variant="body2" sx={{ color: '#aaa' }}>
                                            {event.organizer?.email || ''}
                                        </Typography>
                                    </Box>
                                </Box>
                            </CardContent>
                        </Card>

                        <Card
                            sx={{
                                backgroundColor: '#2d1b69',
                                borderRadius: 3,
                                mb: 3
                            }}
                        >
                            <CardContent sx={{ p: 3 }}>
                                <Typography variant="h6" sx={{ color: '#ffffff', mb: 2 }}>
                                    Debug Info
                                </Typography>
                                <Typography variant="body2" sx={{ color: '#aaa', mb: 1 }}>
                                    User ID: {user?.userId}
                                </Typography>
                                <Typography variant="body2" sx={{ color: '#aaa', mb: 1 }}>
                                    User Role: {authService.getCurrentUserRole()}
                                </Typography>
                                <Typography variant="body2" sx={{ color: '#aaa', mb: 1 }}>
                                    Event Organizer ID: {event?.organizer.id}
                                </Typography>
                                <Typography variant="body2" sx={{ color: '#aaa', mb: 1 }}>
                                    Is Event Owner: {isEventOwner ? 'Yes' : 'No'}
                                </Typography>
                                <Typography variant="body2" sx={{ color: '#aaa', mb: 1 }}>
                                    Can Edit: {canEdit ? 'Yes' : 'No'}
                                </Typography>
                            </CardContent>
                        </Card>
                        <Card
                            sx={{
                                backgroundColor: '#161b22',
                                borderRadius: 3
                            }}
                        >
                            <CardContent sx={{ p: 3 }}>
                                <Typography variant="h6" sx={{ color: '#ffffff', mb: 3 }}>
                                    Actions
                                </Typography>

                                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                                    {canRegister && (
                                        <Button
                                            variant="contained"
                                            size="large"
                                            startIcon={<RegisterIcon />}
                                            onClick={handleRegister}
                                            sx={{
                                                background: 'linear-gradient(90deg, #4caf50, #66bb6a)',
                                                fontWeight: 'bold',
                                                '&:hover': {
                                                    background: 'linear-gradient(90deg, #66bb6a, #4caf50)'
                                                }
                                            }}
                                        >
                                            Register for Event
                                        </Button>
                                    )}

                                    {canEdit && (
                                        <Button
                                            variant="outlined"
                                            size="large"
                                            startIcon={<EditIcon />}
                                            onClick={handleEdit}
                                            sx={{
                                                color: '#bb86fc',
                                                borderColor: '#bb86fc',
                                                fontWeight: 'bold',
                                                '&:hover': {
                                                    borderColor: '#e2baff',
                                                    backgroundColor: 'rgba(187, 134, 252, 0.1)'
                                                }
                                            }}
                                        >
                                            Edit Event
                                        </Button>
                                    )}

                                    {canDelete && (
                                        <Button
                                            variant="outlined"
                                            size="large"
                                            startIcon={<DeleteIcon />}
                                            onClick={() => setDeleteDialogOpen(true)}
                                            sx={{
                                                color: '#f44336',
                                                borderColor: '#f44336',
                                                fontWeight: 'bold',
                                                '&:hover': {
                                                    borderColor: '#ff1744',
                                                    backgroundColor: 'rgba(244, 67, 54, 0.1)'
                                                }
                                            }}
                                        >
                                            Delete Event
                                        </Button>
                                    )}
                                </Box>

                                {!canRegister && !canEdit && !canDelete && (
                                    <Typography
                                        variant="body2"
                                        sx={{
                                            color: '#666',
                                            textAlign: 'center',
                                            fontStyle: 'italic'
                                        }}
                                    >
                                        No actions available for your role
                                    </Typography>
                                )}
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </Container>

            <Dialog
                open={deleteDialogOpen}
                onClose={() => setDeleteDialogOpen(false)}
                PaperProps={{
                    sx: {
                        backgroundColor: '#161b22',
                        color: '#ffffff'
                    }
                }}
            >
                <DialogTitle sx={{ color: '#ffffff' }}>
                    Confirm Event Deletion
                </DialogTitle>
                <DialogContent>
                    <DialogContentText sx={{ color: '#aaa' }}>
                        Are you sure you want to delete "{event.name}"? This action cannot be undone.
                        All registrations and associated data will be permanently removed.
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={() => setDeleteDialogOpen(false)}
                        sx={{ color: '#aaa' }}
                    >
                        Cancel
                    </Button>
                    <Button
                        onClick={handleDeleteConfirm}
                        variant="contained"
                        sx={{
                            backgroundColor: '#f44336',
                            '&:hover': {
                                backgroundColor: '#d32f2f'
                            }
                        }}
                    >
                        Delete Event
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default EventDetails;