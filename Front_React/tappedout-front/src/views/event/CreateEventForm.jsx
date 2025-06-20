import { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import { Box, Button, Chip, Divider, FormControl, FormHelperText, InputLabel, InputAdornment, MenuItem, Paper, Select, TextField, Typography } from "@mui/material";
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';

import sportService from "../../service/sportService";
import categoryService from "../../service/categoryService";
import eventService from "../../service/eventService";
import { authService } from '../../service/authService';
import LoadingSpinner from "../../components/LoadingSpiner";

const EventForm = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [sports, setSports] = useState([]);
    const [categories, setCategories] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [startTime, setStartTime] = useState(null);
    const [endTime, setEndTime] = useState(null);
    
    const {register, handleSubmit, watch, formState: {errors}, setValue} = useForm({
        defaultValues: {
            name: '',
            description: '',
            sportId: '',
            country: '',
            city: '',
            address: '',
            logo: '',
            registrationFee: 0
        }
    });

    const selectedSport = watch("sportId");

    // Load sports
    useEffect(() => {
        const loadSports = async () => {
            try {
                const response = await sportService.getAllSports();
                console.log('SPORTS', response);
                setSports(response);
            } catch (error) {
                toast.error(error?.response?.message || "Error loading sports");
            }
        };
        loadSports();
    }, []);

    // Load categories
    useEffect(() => {
        const loadCategories = async () => {
            if (selectedSport && selectedSport !== '') {
                try {
                    setCategories([]);
                    setSelectedCategories([]);

                    const response = await categoryService.getCategoriesBySport(selectedSport);
                    console.log('CATEGORIES', response);
                    setCategories(response);
                } catch (error) {
                    toast.error(error?.response?.message || "Error loading categories");
                }
            } else {
                setCategories([]);
                setSelectedCategories([]);
            }
        };
        loadCategories();
    }, [selectedSport]);

    const handleCategoryToggle = (categoryId) => {
        setSelectedCategories(prev =>
            prev.includes(categoryId)
                ? prev.filter(id => id !== categoryId)
                : [...prev, categoryId]
        );
    };

    const selectAllCategories = () => {
        if (categories.length === selectedCategories.length) setSelectedCategories([]);
        else setSelectedCategories(categories.map(category => category.id));
    };

    const onSubmit = async (data) => {
        if (selectedCategories.length === 0) {
            toast.error("Please select at least one category");
            return;
        }

        if (!startDate || !endDate || !startTime || !endTime) {
            toast.error("Please select a start and end date and time");
            return;
        }

        const eventData = {
            ...data,
            startDate: combineDateTime(startDate, startTime),
            endDate: combineDateTime(endDate, endTime),
            categories: selectedCategories,
            organizerId: authService.getCurrentUser()?.userId
        };

        setLoading(true);
        
        try {
            await eventService.createEvent(eventData);
            toast.success("Event created successfully");
            navigate("/event");
        } catch (error) {
            toast.error(error?.response?.message || "Error creating event");
        } finally {
            setLoading(false);
        }
    };

    const combineDateTime = (date, time) => {
        if (!date || !time) return null;
        
        const combined = new Date(date);
        combined.setHours(time.getHours());
        combined.setMinutes(time.getMinutes());
        return combined;
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
                    Create New Event
                </Typography>

                <form onSubmit={handleSubmit(onSubmit)} noValidate>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
                        <TextField
                            label="Event Name"
                            fullWidth
                            variant="outlined"
                            {...register('name', {
                                required: 'Event name is required',
                                maxLength: {
                                    value: 255,
                                    message: 'Event name is too long'
                                }
                            })}
                            error={!!errors.name}
                            helperText={errors.name?.message}
                            sx={{
                                input: { color: '#ffffff' },
                                label: { color: '#aaa' },
                                '& .MuiInputBase-input::placeholder': {
                                    color: '#666',
                                    opacity: 1
                                }
                            }}
                        />

                        <TextField
                            label="Description"
                            fullWidth
                            multiline
                            rows={3}
                            variant="outlined"
                            {...register('description')}
                            sx={{
                                textarea: { color: '#ffffff' },
                                label: { color: '#aaa' },
                                '& .MuiInputBase-input::placeholder': {
                                    color: '#666',
                                    opacity: 1
                                }
                            }}
                        />

                        <FormControl fullWidth>
                            <InputLabel sx={{ color: '#aaa' }}>Sport</InputLabel>
                            <Select
                                label="Sport"
                                value={selectedSport || ''}
                                onChange={(e) => setValue('sportId', e.target.value)}
                                {...register('sportId', { required: 'Sport is required' })}
                                error={!!errors.sportId}
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
                                {sports.map((sport) => (
                                    <MenuItem key={sport.id} value={sport.id}>
                                        {sport.name}
                                    </MenuItem>
                                ))}
                            </Select>
                            {errors.sportId && (
                                <FormHelperText error>{errors.sportId.message}</FormHelperText>
                            )}
                        </FormControl>

                        <LocalizationProvider dateAdapter={AdapterDateFns}>
                            <DatePicker
                                label="Start Date"
                                value={startDate}
                                onChange={(newValue) => setStartDate(newValue)}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        fullWidth
                                        sx={{
                                            input: { color: '#ffffff' },
                                            label: { color: '#aaa' },
                                            '& .MuiInputAdornment-root': {
                                                '& .MuiSvgIcon-root': {
                                                    color: '#aaa'
                                                },
                                                '& .MuiIconButton-root': {
                                                    color: '#aaa'
                                                }
                                            },
                                            '& .MuiInputBase-input::placeholder': {
                                                color: '#666',
                                                opacity: 1
                                            }
                                        }}
                                    />
                                )}
                            />

                            <TimePicker
                                label="Start Time"
                                value={startTime}
                                onChange={(newValue) => setStartTime(newValue)}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        fullWidth
                                        sx={{
                                            input: { color: '#ffffff' },
                                            label: { color: '#aaa' },
                                            '& .MuiInputAdornment-root': {
                                                '& .MuiSvgIcon-root': {
                                                    color: '#aaa'
                                                },
                                                '& .MuiIconButton-root': {
                                                    color: '#aaa'
                                                }
                                            },
                                            '& .MuiInputBase-input::placeholder': {
                                                color: '#666',
                                                opacity: 1
                                            }
                                        }}
                                    />
                                )}
                            />

                            <DatePicker
                                label="End Date"
                                value={endDate}
                                onChange={(newValue) => setEndDate(newValue)}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        fullWidth
                                        sx={{
                                            input: { color: '#ffffff' },
                                            label: { color: '#aaa' },
                                            '& .MuiInputAdornment-root': {
                                                '& .MuiSvgIcon-root': {
                                                    color: '#aaa'
                                                },
                                                '& .MuiIconButton-root': {
                                                    color: '#aaa'
                                                }
                                            },
                                            '& .MuiInputBase-input::placeholder': {
                                                color: '#666',
                                                opacity: 1
                                            }
                                        }}
                                    />
                                )}
                            />

                            <TimePicker
                                label="End Time"
                                value={endTime}
                                onChange={(newValue) => setEndTime(newValue)}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        fullWidth
                                        sx={{
                                            input: { color: '#ffffff' },
                                            label: { color: '#aaa' },
                                            '& .MuiInputAdornment-root': {
                                                '& .MuiSvgIcon-root': {
                                                    color: '#aaa'
                                                },
                                                '& .MuiIconButton-root': {
                                                    color: '#aaa'
                                                }
                                            },
                                            '& .MuiInputBase-input::placeholder': {
                                                color: '#666',
                                                opacity: 1
                                            }
                                        }}
                                    />
                                )}
                            />
                        </LocalizationProvider>

                        <TextField
                            label="Country"
                            fullWidth
                            variant="outlined"
                            {...register('country', {
                                required: 'Country is required',
                                maxLength: {
                                    value: 100,
                                    message: 'Country name is too long'
                                }
                            })}
                            error={!!errors.country}
                            helperText={errors.country?.message}
                            sx={{
                                input: { color: '#ffffff' },
                                label: { color: '#aaa' },
                                '& .MuiInputBase-input::placeholder': {
                                    color: '#666',
                                    opacity: 1
                                }
                            }}
                        />

                        <TextField
                            label="City"
                            fullWidth
                            variant="outlined"
                            {...register('city', {
                                required: 'City is required',
                                maxLength: {
                                    value: 100,
                                    message: 'City name is too long'
                                }
                            })}
                            error={!!errors.city}
                            helperText={errors.city?.message}
                            sx={{
                                input: { color: '#ffffff' },
                                label: { color: '#aaa' },
                                '& .MuiInputBase-input::placeholder': {
                                    color: '#666',
                                    opacity: 1
                                }
                            }}
                        />

                        <TextField
                            label="Address (Optional)"
                            fullWidth
                            variant="outlined"
                            {...register('address', {
                                maxLength: {
                                    value: 255,
                                    message: 'Address is too long'
                                }
                            })}
                            error={!!errors.address}
                            helperText={errors.address?.message}
                            sx={{
                                input: { color: '#ffffff' },
                                label: { color: '#aaa' },
                                '& .MuiInputBase-input::placeholder': {
                                    color: '#666',
                                    opacity: 1
                                }
                            }}
                        />

                        <Divider sx={{ borderColor: '#30363d', my: 1 }} />

                        <Typography variant="h6" sx={{ color: '#ffffff' }}>
                            Select Categories
                        </Typography>
                    
                        {selectedSport ? (
                            <>
                                <Button
                                    variant="outlined"
                                    onClick={selectAllCategories}
                                    sx={{
                                        color: '#bb86fc',
                                        borderColor: '#bb86fc',
                                        alignSelf: 'flex-start',
                                        '&:hover': {
                                            borderColor: '#e2baff'
                                        }
                                    }}
                                >
                                    {categories.length === selectedCategories.length 
                                        ? 'Deselect All' 
                                        : 'Select All'}
                                </Button>

                                <Box
                                    sx={{
                                        display: 'flex',
                                        flexWrap: 'wrap',
                                        gap: 1
                                    }}
                                >
                                    {categories.map((category) => (
                                        <Chip
                                            key={category.id}
                                            label={category.name || `Category ${category.id}`}
                                            clickable
                                            onClick={() => handleCategoryToggle(category.id)}
                                            variant={selectedCategories.includes(category.id) ? 'filled' : 'outlined'}
                                            sx={{
                                                color: selectedCategories.includes(category.id) ? '#ffffff' : '#bb86fc',
                                                backgroundColor: selectedCategories.includes(category.id) ? '#6a1b9a' : 'transparent',
                                                borderColor: '#bb86fc',
                                                '&:hover': {
                                                    backgroundColor: selectedCategories.includes(category.id) ? '#8e24aa' : '#30363d'
                                                }
                                            }}
                                        />
                                    ))}
                                </Box>
                            </>
                        ) : (
                            <Typography sx={{ color: '#aaa' }}>
                                Please select a sport to see available categories
                            </Typography>
                        )}

                        <TextField
                            label="Logo URL (Optional)"
                            fullWidth
                            variant="outlined"
                            {...register('logo', {
                                pattern: {
                                    value: /^https?:\/\/.+/,
                                    message: 'Please enter a valid URL'
                                }
                            })}
                            error={!!errors.logo}
                            helperText={errors.logo?.message}
                            sx={{
                                input: { color: '#ffffff' },
                                label: { color: '#aaa' },
                                '& .MuiInputBase-input::placeholder': {
                                    color: '#666',
                                    opacity: 1
                                }
                            }}
                        />

                        <TextField
                            label="Registration Fee"
                            fullWidth
                            type="number"
                            variant="outlined"
                            {...register('registrationFee', {
                                min: {
                                    value: 0,
                                    message: 'Fee cannot be negative'
                                }
                            })}
                            error={!!errors.registrationFee}
                            helperText={errors.registrationFee?.message}
                            sx={{
                                input: { color: '#ffffff' },
                                label: { color: '#aaa' },
                                '& .MuiInputAdornment-root': {
                                    color: '#aaa',
                                    '& .MuiTypography-root': {
                                        color: '#aaa'
                                    }
                                },
                                '& .MuiInputBase-input::placeholder': {
                                    color: '#666',
                                    opacity: 1
                                }
                            }}
                            InputProps={{
                                startAdornment: <InputAdornment position="start">$</InputAdornment>,
                            }}
                        />

                        <Button
                            type="submit"
                            variant="contained"
                            size="large"
                            fullWidth
                            sx={{
                                py: 1.5,
                                mt: 2,
                                background: 'linear-gradient(90deg, #6a1b9a, #8e24aa)',
                                fontWeight: 'bold',
                                '&:hover': {
                                    background: 'linear-gradient(90deg, #8e24aa, #6a1b9a)'
                                }
                            }}
                        >
                            Create Event
                        </Button>
                    </Box>
                </form>
            </Paper>
        </Box>
    );
};

export default EventForm