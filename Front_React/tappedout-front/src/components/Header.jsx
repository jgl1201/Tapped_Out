import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { AppBar, Box, Button, Toolbar, styled } from '@mui/material';
import { authService } from '../service/authService';

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

const Header = () => {
    const navigate = useNavigate();
    const isAuthenticated = authService.isAuthenticated();

    const handleLogout = () => {
        authService.logout();
        navigate('/');
    };

    return (
        <AppBar 
            position="static" 
            sx={{ 
                backgroundColor: '#161b22',
                boxShadow: 'none',
                borderBottom: '1px solid #30363d',
                display: 'flex',
                justifyContent: 'space-between',
            }}
        >
            <Toolbar sx={{ justifyContent: 'flex-end' }}>
                <Box>
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
                        <SecondaryButton 
                        onClick={handleLogout}
                        sx={{ textTransform: 'none' }}
                        >
                        Logout
                        </SecondaryButton>
                    )}
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default Header;