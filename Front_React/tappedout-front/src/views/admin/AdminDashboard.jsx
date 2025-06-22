import React from 'react'
import { useNavigate } from "react-router-dom";

import { Box, Card, CardContent, Container, Grid, Typography, styled } from '@mui/material';
import { EmojiEvents, Event, FitnessCenter, HowToReg, People, SportsKabaddi, SportsMma, VerifiedUser, Wc } from '@mui/icons-material';

const DashboardCard = styled(Card) ({
    cursor: 'pointer',
    transition: 'all 0.3s ease',
    background: 'linear-gradient(135deg, #1e293b 0%, #334155 100%)',
    border: '1px solid #475569',
    borderRadius: '8px',
    '&:hover': {
        transform: 'translateY(-4px)',
        boxShadow: '0 8px 25px rgba(0, 0, 0, 0.3)',
        background: 'linear-gradient(135deg, #334155 0%, #475569 100%)',
    },
});

const IconContainer = styled(Box) ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: '16px',
    '& .MuiSvgIcon-root': {
        fontSize: '3rem',
        color: '#8b5cf6'
    },
});

const CardTitle = styled(Typography) ({
    color: '#f1f5f9',
    fontSize: '1.5rem',
    fontWeight: 'bold',
    textAlign: 'center',
});

const AdminDashboard = () => {
    const navigate = useNavigate();

    const adminSections = [
        {
            title: 'Genders',
            icon: <Wc />,
            route: '/admin/genders',
            description: 'Manage genders'
        },
        {
            title: 'User Types',
            icon: <VerifiedUser />,
            route: '/admin/user-types',
            description: 'Manage user types'
        },
        {
            title: 'Users',
            icon: <People />,
            route: '/admin/users',
            description: 'Manage users'
        },
        {
            title: 'Sports',
            icon: <SportsMma />,
            route: '/admin/sports',
            description: 'Manage sports'
        },
        {
            title: 'Sport Levels',
            icon: <SportsKabaddi />,
            route: '/admin/sport-levels',
            description: 'Manage sport levels (belts in BJJ or Judo...)'
        },
        {
            title: 'Categories',
            icon: <FitnessCenter />,
            route: '/admin/categories',
            description: 'Manage categories (weights, age, gender...)'
        },
        {
            title: 'Events',
            icon: <Event />,
            route: '/admin/events',
            description: 'Manage events'
        },
        {
            title: 'Inscriptions',
            icon: <HowToReg />,
            route: '/admin/inscriptions',
            description: 'Manage inscriptions'
        },
        {
            title: 'Results',
            icon: <EmojiEvents />,
            route: '/admin/results',
            description: 'Manage results'
        }
    ];

    const handleCardClick = (route) => {
        navigate(route);
    };

    return (
        <Container maxWidth="lg" sx={{ py: 4 }}>
            <Box sx={{ mb: 4 }}>
                <Typography 
                    variant="h3" 
                    sx={{ 
                        color: '#f1f5f9',
                        fontWeight: 'bold',
                        textAlign: 'center',
                        mb: 2
                    }}
                >
                    Admin Dashboard
                </Typography>
                <Typography 
                    variant="h6" 
                    sx={{ 
                        color: '#94a3b8',
                        textAlign: 'center'
                    }}
                >
                    Manage your application entities
                </Typography>
            </Box>

            <Grid container spacing={3} sx={{ 
                display: 'flex',
                justifyContent: 'center' // Centra el contenido cuando no hay suficientes elementos para llenar la fila
            }}>
                {adminSections.map((section, index) => (
                    <Grid 
                        item 
                        xs={12} 
                        sm={6} 
                        md={4} 
                        lg={4} 
                        key={index}
                        sx={{
                            display: 'flex',
                            maxWidth: { md: '33.333333%' }, // Fuerza exactamente 3 columnas en md+
                            flexBasis: { md: '33.333333%' }
                        }}
                    >
                        <DashboardCard 
                            onClick={() => handleCardClick(section.route)}
                            sx={{ 
                                width: '100%',
                                minHeight: '200px' // Altura mínima uniforme
                            }}
                        >
                            <CardContent sx={{ 
                                textAlign: 'center',
                                height: '100%',
                                display: 'flex',
                                flexDirection: 'column',
                                justifyContent: 'center',
                                py: 3
                            }}>
                                <IconContainer>
                                    {section.icon}
                                </IconContainer>
                                
                                <CardTitle variant="h6">
                                    {section.title}
                                </CardTitle>
                                
                                <Typography 
                                    variant="body2" 
                                    sx={{ 
                                        color: '#cbd5e1',
                                        mt: 1,
                                        fontSize: '0.9rem'
                                    }}
                                >
                                    {section.description}
                                </Typography>
                            </CardContent>
                        </DashboardCard>
                    </Grid>
                ))}
            </Grid>
        </Container>
    );
};

export default AdminDashboard