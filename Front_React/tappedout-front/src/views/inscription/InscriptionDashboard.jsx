/* eslint-disable no-unused-vars */
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import { Box, Button, Card, CardActions, CardContent, Chip, Container, Grid, Typography } from "@mui/material";
import { Event as EventIcon, LocationOn as LocationIcon, Payment as PaymentIcon } from "@mui/icons-material";

import { authService } from "../../service/authService";
import inscriptionService from "../../service/inscriptionService";
import LoadingSpinner from "../../components/LoadingSpiner";

const InscriptionsDashboard = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [inscriptions, setInscriptions] = useState([]);
    const [user, setUser] = useState(null);

    useEffect(() => {
        const currentUser = authService.getCurrentUser();
        if (!currentUser) {
            navigate('/login');
            return;
        }
        setUser(currentUser);
        loadInscriptions();
    }, [navigate]);

    const loadInscriptions = async () => {
        try {
            setLoading(true);
            const response = await inscriptionService.getInscriptionsByCompetitor(authService.getCurrentUser().userId);
            console.log('INSCRIPTIONS', response);
            console.log('CURRENT USER', authService.getCurrentUser());
            
            const userInscriptions = response;
            
            const sortedInscriptions = [...userInscriptions].sort((a, b) => {
                const dateA = new Date(a.event.startDate).getTime();
                const dateB = new Date(b.event.startDate).getTime();
                return dateA - dateB;
            });
            
            setInscriptions(sortedInscriptions);
        } catch (error) {
            toast.error(error?.response?.message || "Error loading your inscriptions");
        } finally {
            setLoading(false);
        }
    };

    const handleViewEventDetails = (eventId) => {
        navigate(`/event/${eventId}`);
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

    const getPaymentStatusColor = (status) => {
        switch (status) {
            case 'PAID':
                return '#4caf50';
            case 'PENDING':
                return '#ff9800';
            case 'CANCELLED':
                return '#f44336';
            default:
                return '#666';
        }
    };

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
                            My Inscriptions
                        </Typography>
                    </Box>

                    <Typography
                        variant="h6"
                        sx={{ color: '#aaa', mb: 1 }}
                    >
                        View and manage your event participations
                    </Typography>
                </Box>

                <Box sx={{ backgroundColor: '#161b22', borderRadius: 3, p: 3 }}>
                    {inscriptions.length === 0 ? (
                        <Box
                            sx={{
                                textAlign: 'center',
                                py: 8
                            }}
                        >
                            <EventIcon sx={{ fontSize: 80, color: '#30363d', mb: 2 }} />
                            <Typography variant="h5" sx={{ color: '#aaa', mb: 1 }}>
                                No inscriptions found
                            </Typography>
                            <Typography variant="body1" sx={{ color: '#666' }}>
                                You haven't registered for any events yet.
                            </Typography>
                        </Box>
                    ) : (
                        <Grid container spacing={3}>
                            {inscriptions.map((inscription) => (
                                <Grid 
                                    item 
                                    key={inscription.id}
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
                                            minHeight: '320px',
                                            maxWidth: '100%',
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
                                                    minHeight: '3em',
                                                    lineHeight: '1.5em'
                                                }}
                                            >
                                                {inscription.event.name}
                                            </Typography>

                                            <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                                                <PaymentIcon sx={{ 
                                                    color: getPaymentStatusColor(inscription.paymentStatus),
                                                    mr: 1, 
                                                    fontSize: 20 
                                                }} />
                                                <Typography
                                                    variant="body2"
                                                    sx={{ 
                                                        color: getPaymentStatusColor(inscription.paymentStatus),
                                                        textTransform: 'capitalize'
                                                    }}
                                                >
                                                    {inscription.paymentStatus.toLowerCase()}
                                                </Typography>
                                            </Box>

                                            {inscription.category && (
                                                <Box sx={{ mb: 2 }}>
                                                    <Chip
                                                        label={`Category: ${inscription.category.name}`}
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

                                            <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
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
                                                    {inscription.event.city}, {inscription.event.country}
                                                </Typography>
                                            </Box>

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
                                                    {formatDate(inscription.event.startDate)}
                                                </Typography>
                                            </Box>

                                            <Typography
                                                variant="body2"
                                                sx={{ color: '#666', mb: 1 }}
                                            >
                                                Registered on: {formatDate(inscription.registerDate)}
                                            </Typography>

                                            <Box sx={{ textAlign: 'center', mb: 2 }}>
                                                <Typography
                                                    variant="h4"
                                                    sx={{
                                                        color: inscription.event.registrationFee === 0 ? '#4caf50' : '#bb86fc',
                                                        fontWeight: 'bold'
                                                    }}
                                                >
                                                    {formatFee(inscription.event.registrationFee)}
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
                                                onClick={() => handleViewEventDetails(inscription.event.id)}
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
                                                View Event Details
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

export default InscriptionsDashboard;