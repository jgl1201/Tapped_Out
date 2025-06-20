import React from 'react';
import { CircularProgress, Box, Typography } from '@mui/material';

const LoadingSpinner = ({ size = 40, text = 'Loading...', thickness = 4 }) => {
    return (
        <Box
        sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            my: 4,
            color: 'white' // Color blanco para tema oscuro
        }}
        >
        <CircularProgress
            size={size}
            thickness={thickness}
            sx={{ color: 'inherit' }}
        />
        {text && (
            <Typography variant="body1" sx={{ mt: 2, color: 'inherit' }}>
            {text}
            </Typography>
        )}
        </Box>
    );
};

export default LoadingSpinner;