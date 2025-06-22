import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import { Box, Button, Card, CardActions, CardContent, Chip, Container, Grid, Typography } from "@mui/material";
import { Add as AddIcon, LocationOn as LocationIcon, Event as EventIcon } from "@mui/icons-material";

import { authService } from "../../service/authService";
import eventService from "../../service/eventService";
import LoadingSpinner from "../../components/LoadingSpiner";

const EventDashboard = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [events, setEvents] = useState([]);
    const [user, setUser] = useState(null);

    useEffect(() => {
        const currentUser = authService.getCurrentUser();
        setUser(currentUser);
        loadEvents();
    }, []);

    const loadEvents = async () => {
        try {
            setLoading(true);
            const response = await eventService.getAllEvents();
            console.log('EVENTS', response);
            
            // Sort events by start date (nearest first)
            const sortedEvents = [...response].sort((a, b) => {
                const dateA = new Date(a.startDate).getTime();
                const dateB = new Date(b.startDate).getTime();
                return dateA - dateB;
            });
            
            setEvents(sortedEvents);
        } catch (error) {
            toast.error(error?.response?.message || "Error loading events");
        } finally {
            setLoading(false);
        }
    };

    const handleViewDetails = (eventId) => {
        navigate(`/event/${eventId}`);
    };

    const handleAddEvent = () => {
        navigate('/event/add');
    };

    const formatDate = (dateString) => {
        try {
            const date = new Date(dateString);
            return date.toLocaleDateString('en-US', {
                year: 'numeric',
                month: 'short',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
        } catch {
            return 'Date TBD';
        }
    };

    const formatFee = (fee) => {
        if (fee === 0 || fee === null || fee === undefined) {
            return 'Free';
        }
        return `$${parseFloat(fee).toFixed(2)}`;
    };

    const canAddEvent = user && (user.isOrganizer || user.isAdmin);

    if (loading) return <LoadingSpinner />;

    return (
        <Box
            sx={{
                minHeight: '100vh',
                backgroundColor: '#0d1117',
                py: 4
            }}
        >
            <Container maxWidth="xl">
                <Box sx={{ mb: 4 }}>
                    <Box 
                        sx={{ 
                            display: 'flex', 
                            justifyContent: 'space-between', 
                            alignItems: 'center',
                            mb: 2
                        }}
                    >
                        <Typography
                            variant="h3"
                            sx={{ 
                                color: '#ffffff', 
                                fontWeight: 'bold',
                                display: 'flex',
                                alignItems: 'center',
                                gap: 2
                            }}
                        >
                            <EventIcon sx={{ fontSize: 40 }} />
                            Events Dashboard
                        </Typography>

                        {authService.isAdmin() || authService.isOrganizer() && (
                            <Button
                                variant="contained"
                                startIcon={<AddIcon />}
                                onClick={handleAddEvent}
                                sx={{
                                    background: 'linear-gradient(90deg, #6a1b9a, #8e24aa)',
                                    fontWeight: 'bold',
                                    px: 3,
                                    py: 1.5,
                                    '&:hover': {
                                        background: 'linear-gradient(90deg, #8e24aa, #6a1b9a)'
                                    }
                                }}
                            >
                                Add New Event
                            </Button>
                        )}
                    </Box>

                    <Typography
                        variant="h6"
                        sx={{ color: '#aaa', mb: 1 }}
                    >
                        Discover and join exciting events
                    </Typography>
                </Box>

                <Box sx={{ backgroundColor: '#161b22', borderRadius: 3, p: 3 }}>
                    {events.length === 0 ? (
                        <Box
                            sx={{
                                textAlign: 'center',
                                py: 8
                            }}
                        >
                            <EventIcon sx={{ fontSize: 80, color: '#30363d', mb: 2 }} />
                            <Typography variant="h5" sx={{ color: '#aaa', mb: 1 }}>
                                No events available
                            </Typography>
                            <Typography variant="body1" sx={{ color: '#666' }}>
                                {canAddEvent 
                                    ? "Create the first event to get started!" 
                                    : "Check back later for upcoming events."}
                            </Typography>
                        </Box>
                    ) : (
                        <Grid container spacing={3}>
                            {events.map((event) => (
                                <Grid 
                                    item 
                                    key={event.id}
                                    xs={12}
                                    sm={6}
                                    lg={4}
                                    sx={{
                                        display: 'flex',
                                        flexDirection: 'column'
                                    }}
                                >
                                    <Card
                                        sx={{
                                            backgroundColor: '#21262d',
                                            borderRadius: 2,
                                            border: '1px solid #30363d',
                                            height: '100%',
                                            display: 'flex',
                                            flexDirection: 'column',
                                            transition: 'all 0.3s ease',
                                            minHeight: '320px', // Altura mínima fija
                                            maxWidth: '100%', // Asegura que no exceda el ancho del grid
                                            '&:hover': {
                                                transform: 'translateY(-4px)',
                                                boxShadow: '0 8px 25px rgba(0,0,0,0.3)',
                                                borderColor: '#bb86fc'
                                            }
                                        }}
                                    >
                                        <CardContent sx={{ flexGrow: 1, pb: 1 }}>
                                            <Typography
                                                variant="h6"
                                                sx={{
                                                    color: '#ffffff',
                                                    fontWeight: 'bold',
                                                    mb: 2,
                                                    overflow: 'hidden',
                                                    textOverflow: 'ellipsis',
                                                    display: '-webkit-box',
                                                    WebkitLineClamp: 2,
                                                    WebkitBoxOrient: 'vertical',
                                                    minHeight: '3em', // Altura fija para 2 líneas
                                                    lineHeight: '1.5em'
                                                }}
                                            >
                                                {event.name}
                                            </Typography>

                                            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                                                <LocationIcon sx={{ color: '#bb86fc', mr: 1, fontSize: 20 }} />
                                                <Typography
                                                    variant="body2"
                                                    sx={{ 
                                                        color: '#aaa',
                                                        overflow: 'hidden',
                                                        textOverflow: 'ellipsis',
                                                        whiteSpace: 'nowrap'
                                                    }}
                                                >
                                                    {event.city}, {event.country}
                                                </Typography>
                                            </Box>

                                            {event.address && (
                                                <Typography
                                                    variant="body2"
                                                    sx={{
                                                        color: '#666',
                                                        mb: 2,
                                                        overflow: 'hidden',
                                                        textOverflow: 'ellipsis',
                                                        whiteSpace: 'nowrap'
                                                    }}
                                                >
                                                    {event.address}
                                                </Typography>
                                            )}

                                            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                                                <EventIcon sx={{ color: '#bb86fc', mr: 1, fontSize: 20 }} />
                                                <Typography
                                                    variant="body2"
                                                    sx={{
                                                        color: '#aaa',
                                                        overflow: 'hidden',
                                                        textOverflow: 'ellipsis',
                                                        whiteSpace: 'nowrap'
                                                    }}
                                                >
                                                    {formatDate(event.startDate)}
                                                </Typography>
                                            </Box>

                                            {event.sport && (
                                                <Box sx={{ mb: 2 }}>
                                                    <Chip
                                                        label={event.sport.name}
                                                        size="small"
                                                        sx={{
                                                            backgroundColor: '#30363d',
                                                            color: '#bb86fc',
                                                            maxWidth: '100%',
                                                            '& .MuiChip-label': {
                                                                overflow: 'hidden',
                                                                textOverflow: 'ellipsis',
                                                                whiteSpace: 'nowrap'
                                                            }
                                                        }}
                                                    />
                                                </Box>
                                            )}

                                            <Box sx={{ textAlign: 'center', mb: 2 }}>
                                                <Typography
                                                    variant="h4"
                                                    sx={{
                                                        color: event.registrationFee === 0 ? '#4caf50' : '#bb86fc',
                                                        fontWeight: 'bold'
                                                    }}
                                                >
                                                    {formatFee(event.registrationFee)}
                                                </Typography>
                                                <Typography
                                                    variant="caption"
                                                    sx={{ color: '#666' }}
                                                >
                                                    Registration Fee
                                                </Typography>
                                            </Box>
                                        </CardContent>

                                        <CardActions sx={{ p: 2, pt: 0 }}>
                                            <Button
                                                variant="outlined"
                                                fullWidth
                                                onClick={() => handleViewDetails(event.id)}
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
                                                View Details
                                            </Button>
                                        </CardActions>
                                    </Card>
                                </Grid>
                            ))}
                        </Grid>
                    )}
                </Box>
            </Container>
        </Box>
    );
};

export default EventDashboard;