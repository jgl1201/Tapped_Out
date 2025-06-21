import { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';

import { TextField, Button, Box, Typography, Paper, Link, Grid, IconButton, InputAdornment, InputLabel, MenuItem, Select, FormControl } from '@mui/material';
import { Visibility, VisibilityOff, PersonAdd as RegisterIcon } from '@mui/icons-material';

import { authService } from '../service/authService';
import LoadingSpinner from './LoadingSpiner';

const RegisterForm = ({ onSuccessRedirect = "/login"}) => {
    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();

    const { register, handleSubmit, formState: { errors } } = useForm({
        defaultValues: {
            email: '',
            password: '',
            firstName: '',
            lastName: '',
            dni: '',
            phone: '',
            dateOfBirth: '',
            country: '',
            city: '',
            userType: 'COMPETITOR',
            genderId: '' 
        }
    });

    const onSubmit = async (data) => {
        setLoading(true);
        try {
            await authService.register(data);
            toast.success('Registration successful, please Login!', {
                position: 'top-center',
                autoClose: 3000,
                hideProgressBar: true,
                closeOnClick: false,
                pauseOnHover: false,
                draggable: false
            });
            navigate(onSuccessRedirect);
        } catch (error) {
            toast.error(error?.response?.data?.message || 'Registration failed. Please try again.', {
                position: 'top-center',
                autoClose: 3000,
                hideProgressBar: true,
                closeOnClick: false,
                pauseOnHover: false,
                draggable: false
            });
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
                    maxWidth: 500,
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
                    Create Account
                </Typography>

                <form onSubmit={handleSubmit(onSubmit)} noValidate>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
                        <Grid container spacing={2}>
                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="First Name"
                                    fullWidth
                                    variant="outlined"
                                    {...register('firstName', {
                                        required: 'First name is required',
                                        minLength: {
                                            value: 2,
                                            message: 'Minimum 2 characters'
                                        }
                                    })}
                                    error={!!errors.firstName}
                                    helperText={errors.firstName?.message}
                                    sx={{
                                        input: { color: '#ffffff' },
                                        label: { color: '#aaa' }
                                    }}
                                />
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="Last Name"
                                    fullWidth
                                    variant="outlined"
                                    {...register('lastName', {
                                        required: 'Last name is required',
                                        minLength: {
                                            value: 2,
                                            message: 'Minimum 2 characters'
                                        }
                                    })}
                                    error={!!errors.lastName}
                                    helperText={errors.lastName?.message}
                                    sx={{
                                        input: { color: '#ffffff' },
                                        label: { color: '#aaa' }
                                    }}
                                />
                            </Grid>

                            <Grid item xs={12}>
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
                            </Grid>

                            <Grid item xs={12}>
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
                                        },
                                        pattern: {
                                            value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/,
                                            message: '1 upper, 1 lower, 1 number'
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
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="ID Document"
                                    fullWidth
                                    variant="outlined"
                                    {...register('dni', {
                                        required: 'ID Document is required',
                                        pattern: {
                                            value: /^[a-zA-Z0-9-.]{4,20}$/,
                                            message: 'Invalid document format'
                                        }
                                    })}
                                    error={!!errors.dni}
                                    helperText={errors.dni?.message}
                                    sx={{
                                        input: { color: '#ffffff' },
                                        label: { color: '#aaa' }
                                    }}
                                />
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="Phone (optional)"
                                    fullWidth
                                    variant="outlined"
                                    type="tel"
                                    {...register('phone')}
                                    sx={{
                                        input: { color: '#ffffff' },
                                        label: { color: '#aaa' }
                                    }}
                                />
                            </Grid>

                            {/* Date of Birth */}
                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="Date of Birth"
                                    type="date"
                                    fullWidth
                                    variant="outlined"
                                    InputLabelProps={{ shrink: true }}
                                    {...register('dateOfBirth', {
                                        required: 'Date of birth is required'
                                    })}
                                    error={!!errors.dateOfBirth}
                                    helperText={errors.dateOfBirth?.message}
                                    sx={{
                                        input: { color: '#ffffff' },
                                        label: { color: '#aaa' }
                                    }}
                                />
                            </Grid>

                            {/* User Type */}
                            <Grid item xs={12} md={6}>
                                <FormControl fullWidth>
                                    <InputLabel sx={{ color: '#aaa' }}>User Type</InputLabel>
                                    <Select
                                        label="User Type"
                                        {...register('userType')}
                                        defaultValue="COMPETITOR"
                                        sx={{ 
                                            color: '#ffffff',
                                            '.MuiOutlinedInput-notchedOutline': {
                                                borderColor: '#444'
                                            },
                                            '&:hover .MuiOutlinedInput-notchedOutline': {
                                                borderColor: '#666'
                                            }
                                        }}
                                    >
                                        <MenuItem value="COMPETITOR">Competitor</MenuItem>
                                        <MenuItem value="ORGANIZER">Organizer</MenuItem>
                                    </Select>
                                </FormControl>
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="Country"
                                    fullWidth
                                    variant="outlined"
                                    {...register('country', {
                                        required: 'Country is required'
                                    })}
                                    error={!!errors.country}
                                    helperText={errors.country?.message}
                                    sx={{
                                        input: { color: '#ffffff' },
                                        label: { color: '#aaa' }
                                    }}
                                />
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="City"
                                    fullWidth
                                    variant="outlined"
                                    {...register('city', {
                                        required: 'City is required'
                                    })}
                                    error={!!errors.city}
                                    helperText={errors.city?.message}
                                    sx={{
                                        input: { color: '#ffffff' },
                                        label: { color: '#aaa' }
                                    }}
                                />
                            </Grid>
                        </Grid>

                        <Button
                            type="submit"
                            variant="contained"
                            size="large"
                            startIcon={<RegisterIcon />}
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
                            Register
                        </Button>

                        <Typography align="center" sx={{ color: '#aaa', mt: 2 }}>
                            Already have an account?{' '}
                            <Link
                                component={RouterLink}
                                to="/login"
                                sx={{
                                    color: '#bb86fc',
                                    fontWeight: 500,
                                    '&:hover': {
                                        textDecoration: 'underline',
                                        color: '#e2baff'
                                    }
                                }}
                            >
                                Login here
                            </Link>
                        </Typography>
                    </Box>
                </form>
            </Paper>
        </Box>
    );
};

export default RegisterForm;