import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";

import { Box, Button, Card, CardActions, CardContent, Chip, Container, Divider, FormControl, FormHelperText, InputLabel, InputAdornment, MenuItem, Paper, Select, TextField, Typography } from "@mui/material";
import { Category as CategoryIcon , Event as EventIcon, Person as PersonIcon} from "@mui/icons-material";

import { authService } from "../../service/authService";
import inscriptionService from "../../service/inscriptionService";
import userService from "../../service/userService";
import categoryService from "../../service/categoryService";
import eventService from "../../service/eventService";
import LoadingSpinner from "../../components/LoadingSpiner";

const InscriptionForm = () => {
    const navigate = useNavigate();
    const { id: eventId } = useParams();
    const [loading, setLoading] = useState(false);
    const [event, setEvent] = useState({});
    const [categories, setCategories] = useState([]);
    const [currentUser, setCurrentUser] = useState({});

    const { register, handleSubmit, formState: { errors }, setValue, watch } = useForm({
        defaultValues: {
            eventId: eventId || '',
            competitorId: '',
            categoryId: '',
            paymentStatus: 'PENDING'
        }
    });

    const selectedCategory = watch('categoryId');

    const calculateAge = (dateOfBirth) => {
        if (!dateOfBirth) return null;

        let birthDate;
        if (typeof dateOfBirth === 'string') birthDate = new Date(dateOfBirth);
        else if (dateOfBirth instanceof Date) birthDate = dateOfBirth;
        else return null;

        if (isNaN(birthDate.getTime())) return null;

        const today = new Date();
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
            age--;
        }
        return age;
    };

    const formatAgeRange = (minAge, maxAge) => {
        const min = minAge !== null && minAge !== undefined ? minAge : 'N/A';
        const max = maxAge !== null && maxAge !== undefined ? maxAge : 'N/A';

        return `${min} - ${max}`;
    }

    useEffect(() => {
        const loadInitialData = async () => {
            setLoading(true);
            try {
                // Obtener información del usuario actual
                const currentUserData = authService.getCurrentUser();
                if (!currentUserData) {
                    toast.error("Please log in to register for events");
                    navigate("/login");
                    return;
                }

                // Obtener userId desde localStorage como respaldo
                const userId = currentUserData.userId || localStorage.getItem('userInfo.userId');
                if (!userId) {
                    toast.error("User information not found. Please log in again.");
                    navigate("/login");
                    return;
                }

                const userData = await userService.getUserById(userId);
                
                // Process user data with correct gender and calculated age
                const processedUserData = {
                    ...userData,
                    gender: userData.genderId?.name || 'Not available',
                    age: calculateAge(userData.dateOfBirth)
                };
                
                setCurrentUser(processedUserData);
                setValue('competitorId', processedUserData.id);

                if (eventId) {
                    const eventData = await eventService.getEventById(eventId);
                    setEvent(eventData);
                    setValue('eventId', eventData.id);

                    if (eventData.sport && eventData.sport.id) {
                        await loadCategoriesBySport(eventData.sport.id);
                    } else {
                        toast.warning("Event sport information not available");
                    }
                } else {
                    toast.error("Event ID not provided");
                    navigate("/events");
                }
            } catch (error) {
                toast.error(error?.response?.data?.message || "Error loading data");
                console.error('Error loading initial data:', error);
                
                // Si hay error de autorización, redirigir al login
                if (error?.response?.status === 401 || error?.response?.status === 403) {
                    navigate("/login");
                }
            } finally {
                setLoading(false);
            }
        };

        loadInitialData();
    }, [eventId, navigate, setValue]);

    const loadCategoriesBySport = async (sportId) => {
        try {
            const response = await categoryService.getCategoriesBySport(sportId);
            console.log('CATEGORIES BY SPORT', response);
            setCategories(response);
        } catch (error) {
            toast.error(error?.response?.data?.message || "Error loading categories");
            console.error('Error loading categories:', error);
            setCategories([]); // Asegurar que categories sea un array vacío en caso de error
        }
    };

    const onSubmit = async (data) => {
        if (!data.categoryId) {
            toast.error("Please select a category");
            return;
        }

        const inscriptionData = {
            competitorId: parseInt(data.competitorId),
            eventId: parseInt(data.eventId),
            categoryId: parseInt(data.categoryId),
            paymentStatus: data.paymentStatus
        };

        setLoading(true);
        
        try {
            await inscriptionService.createInscription(inscriptionData);
            toast.success("Registration completed successfully!");
            navigate("/inscriptions");
        } catch (error) {
            toast.error(error?.response?.data?.message || "Error creating registration");
            console.error('Error creating inscription:', error);
        } finally {
            setLoading(false);
        }
    };

    const getPaymentStatusColor = (status) => {
        switch (status) {
            case 'PENDING':
                return '#ff9800';
            case 'PAID':
                return '#4caf50';
            case 'CANCELLED':
                return '#f44336';
            default:
                return '#757575';
        }
    };

    if (loading) return <LoadingSpinner />;

    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                minHeight: '100vh',
                px: 2,
                py: 4,
                backgroundColor: '#0d1117'
            }}
        >
            <Paper
                elevation={6}
                sx={{
                    p: 4,
                    width: '100%',
                    maxWidth: 800,
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
                    Event Registration
                </Typography>

                {/* Event Information Card */}
                {event && event.id && (
                    <Card
                        sx={{
                            mb: 4,
                            backgroundColor: '#21262d',
                            border: '1px solid #30363d'
                        }}
                    >
                        <CardContent>
                            <Typography variant="h6" sx={{ color: '#ffffff', mb: 2 }}>
                                Event Details
                            </Typography>
                            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                                <Typography sx={{ color: '#aaa' }}>
                                    <strong style={{ color: '#ffffff' }}>Name:</strong> {event.name || 'Not available'}
                                </Typography>
                                <Typography sx={{ color: '#aaa' }}>
                                    <strong style={{ color: '#ffffff' }}>Sport:</strong> {event.sport?.name || 'Not available'}
                                </Typography>
                                <Typography sx={{ color: '#aaa' }}>
                                    <strong style={{ color: '#ffffff' }}>Location:</strong> {event.city || 'Not available'}, {event.country || ''}
                                </Typography>
                                <Typography sx={{ color: '#aaa' }}>
                                    <strong style={{ color: '#ffffff' }}>Date:</strong> {event.startDate ? new Date(event.startDate).toLocaleDateString() : 'Not available'}
                                </Typography>
                                <Typography sx={{ color: '#aaa' }}>
                                    <strong style={{ color: '#ffffff' }}>Registration Fee:</strong> ${event.registrationFee || '0'}
                                </Typography>
                            </Box>
                        </CardContent>
                    </Card>
                )}

                {/* Competitor Information Card */}
                {currentUser && currentUser.id && (
                    <Card
                        sx={{
                            mb: 4,
                            backgroundColor: '#21262d',
                            border: '1px solid #30363d'
                        }}
                    >
                        <CardContent>
                            <Typography variant="h6" sx={{ color: '#ffffff', mb: 2 }}>
                                Competitor Information
                            </Typography>
                            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                                <Typography sx={{ color: '#aaa' }}>
                                    <strong style={{ color: '#ffffff' }}>Name:</strong> {currentUser.firstName || 'Not available'} {currentUser.lastName || ''}
                                </Typography>
                                <Typography sx={{ color: '#aaa' }}>
                                    <strong style={{ color: '#ffffff' }}>Email:</strong> {currentUser.email || 'Not available'}
                                </Typography>
                                <Typography sx={{ color: '#aaa' }}>
                                    <strong style={{ color: '#ffffff' }}>Gender:</strong> {currentUser.gender || 'Not available'}
                                </Typography>
                                <Typography sx={{ color: '#aaa' }}>
                                    <strong style={{ color: '#ffffff' }}>Age:</strong> {currentUser.age !== null ? `${currentUser.age} years` : 'Not available'}
                                </Typography>
                            </Box>
                        </CardContent>
                    </Card>
                )}

                <form onSubmit={handleSubmit(onSubmit)} noValidate>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
                        
                        {/* Hidden fields for IDs */}
                        <input type="hidden" {...register('competitorId')} />
                        <input type="hidden" {...register('eventId')} />

                        {/* Category Selection */}
                        <FormControl fullWidth>
                            <InputLabel sx={{ color: '#aaa' }}>Category</InputLabel>
                            <Select
                                label="Category"
                                value={selectedCategory || ''}
                                onChange={(e) => setValue('categoryId', e.target.value)}
                                {...register('categoryId', { required: 'Category is required' })}
                                error={!!errors.categoryId}
                                sx={{
                                    color: '#ffffff',
                                    '.MuiOutlinedInput-notchedOutline': {
                                        borderColor: '#aaa'
                                    },
                                    '&:hover .MuiOutlinedInput-notchedOutline': {
                                        borderColor: '#ffffff'
                                    },
                                    '.MuiSvgIcon-root': {
                                        color: '#aaa'
                                    },
                                    '& .MuiSelect-select': {
                                        '&:focus': {
                                            backgroundColor: 'transparent'
                                        }
                                    }
                                }}
                                MenuProps={{
                                    PaperProps: {
                                        sx: {
                                            backgroundColor: '#161b22',
                                            color: '#ffffff',
                                            '& .MuiMenuItem-root': {
                                                '&:hover': {
                                                    backgroundColor: '#30363d'
                                                }
                                            }
                                        }
                                    }
                                }}
                            >
                                {categories && categories.length > 0 ? categories.map((category) => (
                                    <MenuItem key={category.id} value={category.id}>
                                        <Box>
                                            <Typography variant="body1">
                                                {category.name}
                                            </Typography>
                                            <Typography variant="caption" sx={{ color: '#aaa' }}>
                                                Age: {formatAgeRange(category.minAge, category.maxAge)} | 
                                                Weight: {category.minWeight || 'N/A'}-{category.maxWeight || 'N/A'}kg | 
                                                Level: {category.level?.name || 'N/A'}
                                            </Typography>
                                        </Box>
                                    </MenuItem>
                                )) : (
                                    <MenuItem disabled>
                                        <Typography variant="body2" sx={{ color: '#aaa' }}>
                                            No categories available
                                        </Typography>
                                    </MenuItem>
                                )}
                            </Select>
                            {errors.categoryId && (
                                <FormHelperText error>{errors.categoryId.message}</FormHelperText>
                            )}
                            {(!categories || categories.length === 0) && (
                                <FormHelperText sx={{ color: '#aaa' }}>
                                    No categories available for this event's sport
                                </FormHelperText>
                            )}
                        </FormControl>

                        {/* Payment Status */}
                        <FormControl fullWidth>
                            <InputLabel sx={{ color: '#aaa' }}>Payment Status</InputLabel>
                            <Select
                                label="Payment Status"
                                {...register('paymentStatus')}
                                defaultValue="PENDING"
                                disabled
                                sx={{
                                    color: '#ffffff',
                                    '.MuiOutlinedInput-notchedOutline': {
                                        borderColor: '#aaa'
                                    },
                                    '&:hover .MuiOutlinedInput-notchedOutline': {
                                        borderColor: '#ffffff'
                                    },
                                    '.MuiSvgIcon-root': {
                                        color: '#aaa'
                                    }
                                }}
                            >
                                <MenuItem value="PENDING">
                                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                        <Chip
                                            label="PENDING"
                                            size="small"
                                            sx={{
                                                backgroundColor: getPaymentStatusColor('PENDING'),
                                                color: '#ffffff',
                                                fontWeight: 'bold'
                                            }}
                                        />
                                    </Box>
                                </MenuItem>
                                <MenuItem value="PAID">
                                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                        <Chip
                                            label="PAID"
                                            size="small"
                                            sx={{
                                                backgroundColor: getPaymentStatusColor('PAID'),
                                                color: '#ffffff',
                                                fontWeight: 'bold'
                                            }}
                                        />
                                    </Box>
                                </MenuItem>
                                <MenuItem value="CANCELLED">
                                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                        <Chip
                                            label="CANCELLED"
                                            size="small"
                                            sx={{
                                                backgroundColor: getPaymentStatusColor('CANCELLED'),
                                                color: '#ffffff',
                                                fontWeight: 'bold'
                                            }}
                                        />
                                    </Box>
                                </MenuItem>
                            </Select>
                        </FormControl>

                        {event && event.id && currentUser && currentUser.id && selectedCategory && (
                            <Card
                                sx={{
                                    backgroundColor: '#21262d',
                                    border: '1px solid #30363d'
                                }}
                            >
                                <CardContent>
                                    <Typography variant="h6" sx={{ color: '#ffffff', mb: 2 }}>
                                        Registration Summary
                                    </Typography>
                                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                                        <Typography sx={{ color: '#aaa' }}>
                                            <strong style={{ color: '#ffffff' }}>Event:</strong> {event.name}
                                        </Typography>
                                        <Typography sx={{ color: '#aaa' }}>
                                            <strong style={{ color: '#ffffff' }}>Competitor:</strong> {currentUser.firstName} {currentUser.lastName}
                                        </Typography>
                                        <Typography sx={{ color: '#aaa' }}>
                                            <strong style={{ color: '#ffffff' }}>Category:</strong> {
                                                categories.find(cat => cat.id === parseInt(selectedCategory))?.name || 'Selected Category'
                                            }
                                        </Typography>
                                        <Typography sx={{ color: '#aaa' }}>
                                            <strong style={{ color: '#ffffff' }}>Fee:</strong> ${event.registrationFee}
                                        </Typography>
                                        <Typography sx={{ color: '#aaa' }}>
                                            <strong style={{ color: '#ffffff' }}>Status:</strong> 
                                            <Chip
                                                label="PENDING"
                                                size="small"
                                                sx={{
                                                    ml: 1,
                                                    backgroundColor: getPaymentStatusColor('PENDING'),
                                                    color: '#ffffff',
                                                    fontWeight: 'bold'
                                                }}
                                            />
                                        </Typography>
                                    </Box>
                                </CardContent>
                            </Card>
                        )}

                        <Button
                            type="submit"
                            variant="contained"
                            size="large"
                            fullWidth
                            disabled={!selectedCategory || !categories || categories.length === 0}
                            sx={{
                                py: 1.5,
                                mt: 2,
                                background: 'linear-gradient(90deg, #6a1b9a, #8e24aa)',
                                fontWeight: 'bold',
                                '&:hover': {
                                    background: 'linear-gradient(90deg, #8e24aa, #6a1b9a)'
                                },
                                '&:disabled': {
                                    background: '#424242',
                                    color: '#757575'
                                }
                            }}
                        >
                            Complete Registration
                        </Button>

                        <Button
                            variant="outlined"
                            size="large"
                            fullWidth
                            onClick={() => navigate(-1)}
                            sx={{
                                py: 1.5,
                                color: '#bb86fc',
                                borderColor: '#bb86fc',
                                '&:hover': {
                                    borderColor: '#e2baff',
                                    backgroundColor: 'rgba(187, 134, 252, 0.1)'
                                }
                            }}
                        >
                            Cancel
                        </Button>
                    </Box>
                </form>
            </Paper>
        </Box>
    );
};

export default InscriptionForm;