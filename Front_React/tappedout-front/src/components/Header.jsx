import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { AppBar, Box, Button, Toolbar, Typography, styled } from '@mui/material';

import { authService } from '../service/authService';
import logo from '../assets/TappedOut_Full-logo.png';

const PrimaryButton = styled(Button)({
    background: 'linear-gradient(90deg, #6a1b9a, #8e24aa)',
    fontWeight: 'bold',
    color: 'white',
    padding: '8px 20px',
    borderRadius: '4px',
    '&:hover': {
        background: 'linear-gradient(90deg, #8e24aa, #6a1b9a)',
    }
});

const SecondaryButton = styled(Button)({
    background: 'linear-gradient(90deg, #555555, #bbbbbb)',
    fontWeight: 'bold',
    color: 'white',
    padding: '8px 20px',
    borderRadius: '4px',
    marginLeft: '16px',
    '&:hover': {
        background: 'linear-gradient(90deg, #bbbbbb, #555555)',
    }
});

const Logo = styled('img')({
    height: '80px',
    width: 'auto',
    cursor: 'pointer'
});

const NavLink = styled(Button)({
    color: '#fff',
    fontSize: '16px',
    fontWeight: 'bold',
    textTransform: 'none',
    padding: '8px 16px',
    borderRadius: '4px',
    '&:hover': {
        background: 'rgba(255, 255, 255, 0.1)',
    }
});

const Header = () => {
    const navigate = useNavigate();
    const isAuthenticated = authService.isAuthenticated();

    const handleLogout = () => {
        authService.logout();
        navigate('/');
    };

    const handleLogoClick = () => {
        navigate('/');
    };

    return (
        <AppBar 
            position="static" 
            sx={{ 
                backgroundColor: '#161b22',
                boxShadow: 'none',
                borderBottom: '1px solid #30363d',
            }}
        >
            <Toolbar sx={{ 
                display: 'grid', 
                gridTemplateColumns: '1fr auto 1fr',
                alignItems: 'center'
            }}>
                <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-start' }}>
                    <Logo 
                        src={logo}
                        alt="Logo"
                        onClick={handleLogoClick}
                    />
                </Box>

                <Box sx={{ 
                    display: 'flex', 
                    alignItems: 'center',
                    gap: 2,
                    justifyContent: 'center'
                }}>
                    <NavLink 
                        component={RouterLink} 
                        to="/event"
                    >
                        Events
                    </NavLink>

                    <NavLink 
                        component={RouterLink} 
                        to="/inscription"
                    >
                        Inscriptions
                    </NavLink>
                </Box>

                <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
                    {!isAuthenticated ? (
                        <>
                        <PrimaryButton 
                            component={RouterLink} 
                            to="/login"
                            sx={{ textTransform: 'none' }}
                        >
                            Sign In
                        </PrimaryButton>
                        <SecondaryButton 
                            component={RouterLink} 
                            to="/register"
                            sx={{ textTransform: 'none' }}
                        >
                            Register
                        </SecondaryButton>
                        </>
                    ) : (
                        <>
                            <Typography variant="body1" sx={{ mr: 2 }}>{authService.getCurrentUser().firstName}</Typography>
                            <SecondaryButton 
                                onClick={handleLogout}
                                sx={{ textTransform: 'none' }}
                            >
                            Logout
                            </SecondaryButton>
                        </>
                    )}
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default Header;