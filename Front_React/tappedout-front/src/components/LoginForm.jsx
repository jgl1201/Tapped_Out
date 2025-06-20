import React, { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { toast } from 'react-hot-toast';

import { TextField, Button, Box, Typography, Link, Paper, IconButton, InputAdornment } from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';

import { authService } from '../service/authService';
import LoadingSpinner from './LoadingSpiner';

const LoginForm = ({ onSuccessRedirect = "/"}) => {
    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();

    const { register, handleSubmit, formState: { errors } } = useForm({
        defaultValues: {
            email: '',
            password: ''
        }
    });

    const onSubmit = async (data) => {
        setLoading(true);
        try {
            await authService.login(data);
            toast.success('Login successful!');
            navigate(onSuccessRedirect);
        } catch (error) {
            toast.error(error?.response?.data?.message || 'Login failed. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    if (loading)
        return <LoadingSpinner />;

    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                minHeight: '100vh',
                px: 2
            }}
        >
            <Paper
                elevation={6}
                sx={{
                    p: 4,
                    width: '100%',
                    maxWidth: 400,
                    borderRadius: 3,
                    backgroundColor: '#161b22'
                }}
            >
                <Typography
                    variant="h4"
                    align="center"
                    gutterBottom
                    sx={{ color: '#ffffff', mb: 4 }}
                >
                    Sign In
                </Typography>

                <form onSubmit={handleSubmit(onSubmit)} noValidate>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
                        <TextField
                            label="Email"
                            fullWidth
                            variant="outlined"
                            {...register('email', {
                                required: 'Email is required',
                                pattern: {
                                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                    message: 'Invalid email address'
                                }
                            })}
                            error={!!errors.email}
                            helperText={errors.email?.message}
                            sx={{
                                input: { color: '#ffffff' },
                                label: { color: '#aaa' }
                            }}
                        />

                        <TextField
                            label="Password"
                            type={showPassword ? 'text' : 'password'}
                            fullWidth
                            variant="outlined"
                            {...register('password', {
                                required: 'Password is required',
                                minLength: {
                                    value: 8,
                                    message: 'Min 8 characters'
                                }
                            })}
                            error={!!errors.password}
                            helperText={errors.password?.message}
                            sx={{
                                input: { color: '#ffffff' },
                                label: { color: '#aaa' }
                            }}
                            InputProps={{
                                endAdornment: (
                                    <InputAdornment position="end">
                                        <IconButton
                                            onClick={() => setShowPassword(!showPassword)}
                                            edge="end"
                                            sx={{ color: '#ccc' }}
                                        >
                                            {showPassword ? <VisibilityOff /> : <Visibility />}
                                        </IconButton>
                                    </InputAdornment>
                                )
                            }}
                        />

                        <Button
                            type="submit"
                            variant="contained"
                            size="large"
                            sx={{
                                mt: 1,
                                py: 1.5,
                                background: 'linear-gradient(90deg, #6a1b9a, #8e24aa)',
                                fontWeight: 'bold',
                                '&:hover': {
                                    background: 'linear-gradient(90deg, #8e24aa, #6a1b9a)'
                                }
                            }}
                        >
                            Login
                        </Button>

                        <Typography align="center" sx={{ color: '#aaa', mt: 2 }}>
                            Don't have an account?{' '}
                            <Link
                                component={RouterLink}
                                to="/register"
                                sx={{
                                    color: '#bb86fc',
                                    fontWeight: 500,
                                    '&:hover': {
                                        textDecoration: 'underline',
                                        color: '#e2baff'
                                    }
                                }}
                            >
                                Register here
                            </Link>
                        </Typography>
                    </Box>
                </form>
            </Paper>
        </Box>
    );
};

export default LoginForm;