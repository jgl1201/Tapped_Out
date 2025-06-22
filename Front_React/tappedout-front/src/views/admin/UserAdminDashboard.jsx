import { useState, useEffect } from "react";
import { toast } from "react-toastify";

import { 
    Alert, Box, Button, CircularProgress, Container, Dialog, DialogActions, 
    DialogContent, DialogContentText, DialogTitle, IconButton, Paper, Table, 
    TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, 
    Typography, styled, FormControl, InputLabel, Select, MenuItem, Chip,
    Avatar, Grid
} from "@mui/material";
import { 
    Add as AddIcon, Delete as DeleteIcon, Edit as EditIcon, Person as PersonIcon,
    Visibility as ViewIcon, Security as SecurityIcon
} from "@mui/icons-material";

import userService from "../../service/userService";

const StyledTableContainer = styled(TableContainer)({
    background: 'linear-gradient(135deg, #1e293b 0%, #334155 100%)',
    border: '1px solid #475569',
    borderRadius: '8px',
    '& .MuiTable-root': {
        minWidth: '800px'
    }
});

const StyledTableHead = styled(TableHead)({
    '& .MuiTableCell-head': {
        backgroundColor: '#1e293b',
        color: '#f1f5f9',
        fontWeight: 'bold',
        fontSize: '1.1rem',
        borderBottom: '2px solid #475569',
        border: '1px solid #f1f5f9',
    },
});

const StyledTableRow = styled(TableRow)({
    '&:nth-of-type(odd)': {
        backgroundColor: 'rgba(71, 85, 105, 0.3)',
    },
    '&:nth-of-type(even)': {
        backgroundColor: 'rgba(30, 41, 59, 0.5)',
    },
    '&:hover': {
        backgroundColor: 'rgba(139, 92, 246, 0.1)',
        cursor: 'pointer',
    },
    '& .MuiTableCell-body': {
        color: '#f1f5f9',
        fontSize: '0.9rem',
        borderBottom: '1px solid #475569',
    },
});

const HeaderContainer = styled(Box)({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: '32px',
    gap: '16px',
});

const TitleContainer = styled(Box)({
    display: 'flex',
    alignItems: 'center',
    gap: '16px',
});

const IconContainer = styled(Box)({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    '& .MuiSvgIcon-root': {
        fontSize: '3rem',
        color: '#8b5cf6'
    },
});

const LoadingContainer = styled(Box)({
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    minHeight: '200px',
});

const ActionCell = styled(TableCell)({
    '& .MuiIconButton-root': {
        margin: '0 2px',
        transition: 'all 0.2s ease-in-out',
    },
    '& .view-button': {
        color: '#3b82f6',
        '&:hover': {
            backgroundColor: 'rgba(59, 130, 246, 0.1)',
            transform: 'scale(1.1)',
        },
    },
    '& .edit-button': {
        color: '#8b5cf6',
        '&:hover': {
            backgroundColor: 'rgba(139, 92, 246, 0.1)',
            transform: 'scale(1.1)',
        },
    },
    '& .security-button': {
        color: '#f59e0b',
        '&:hover': {
            backgroundColor: 'rgba(245, 158, 11, 0.1)',
            transform: 'scale(1.1)',
        },
    },
    '& .delete-button': {
        color: '#ef4444',
        '&:hover': {
            backgroundColor: 'rgba(239, 68, 68, 0.1)',
            transform: 'scale(1.1)',
        },
    },
});

const StyledFormControl = styled(FormControl)({
    '& .MuiOutlinedInput-root': {
        color: '#f1f5f9',
        '& fieldset': {
            borderColor: '#475569',
        },
        '&:hover fieldset': {
            borderColor: '#8b5cf6',
        },
        '&.Mui-focused fieldset': {
            borderColor: '#8b5cf6',
        },
    },
    '& .MuiInputLabel-root': {
        color: '#94a3b8',
        '&.Mui-focused': {
            color: '#8b5cf6',
        },
    },
    '& .MuiSelect-icon': {
        color: '#94a3b8',
    },
});

const StyledTextField = styled(TextField)({
    '& .MuiOutlinedInput-root': {
        color: '#f1f5f9',
        '& fieldset': {
            borderColor: '#475569',
        },
        '&:hover fieldset': {
            borderColor: '#8b5cf6',
        },
        '&.Mui-focused fieldset': {
            borderColor: '#8b5cf6',
        },
    },
    '& .MuiInputLabel-root': {
        color: '#94a3b8',
        '&.Mui-focused': {
            color: '#8b5cf6',
        },
    },
});

const UserAdminDashboard = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [editDialogOpen, setEditDialogOpen] = useState(false);
    const [createDialogOpen, setCreateDialogOpen] = useState(false);
    const [viewDialogOpen, setViewDialogOpen] = useState(false);
    const [securityDialogOpen, setSecurityDialogOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const [actionLoading, setActionLoading] = useState(false);

    // Estados para el formulario de creación
    const [createForm, setCreateForm] = useState({
        dni: '',
        typeId: '',
        email: '',
        password: '',
        firstName: '',
        lastName: '',
        dateOfBirth: '',
        genderId: '',
        country: '',
        city: '',
        phone: '',
    });

    // Estados para el formulario de edición
    const [updateForm, setUpdateForm] = useState({
        firstName: '',
        lastName: '',
        dateOfBirth: '',
        country: '',
        city: '',
        phone: '',
        isVerified: false
    });

    // Estados para el formulario de seguridad
    const [securityForm, setSecurityForm] = useState({
        email: '',
        newPassword: ''
    });

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            setLoading(true);
            setError(null);
            
            const response = await userService.getAllUsers();
            const sortedUsers = Array.isArray(response) ? response.sort((a, b) => a.id - b.id) : [];

            setUsers(sortedUsers);
        } catch (err) {
            console.error('Error fetching users:', err);
            setError('Error al cargar los usuarios');
            
            toast.error('Error al cargar los usuarios', {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });
        } finally {
            setLoading(false);
        }
    };

    const handleCreateOpen = () => {
        setCreateForm({
            dni: '',
            typeId: '',
            email: '',
            password: '',
            firstName: '',
            lastName: '',
            dateOfBirth: '',
            genderId: '',
            country: '',
            city: '',
            phone: '',
        });
        setCreateDialogOpen(true);
    };

    const handleCreateConfirm = async () => {
        // Validaciones básicas mejoradas
        const requiredFields = {
            dni: 'DNI',
            email: 'Email',
            password: 'Contraseña',
            firstName: 'Nombre',
            lastName: 'Apellido',
            dateOfBirth: 'Fecha de nacimiento',
            genderId: 'Género',
            country: 'País',
            city: 'Ciudad',
            typeId: 'Tipo de usuario'
        };

        const missingFields = [];
        for (const [field, label] of Object.entries(requiredFields)) {
            if (!createForm[field] || (typeof createForm[field] === 'string' && !createForm[field].trim())) {
                missingFields.push(label);
            }
        }

        if (missingFields.length > 0) {
            toast.error(`Los siguientes campos son obligatorios: ${missingFields.join(', ')}`);
            return;
        }

        // Validación de email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(createForm.email)) {
            toast.error('Por favor ingrese un email válido');
            return;
        }

        // Validación de contraseña
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/;
        if (!passwordRegex.test(createForm.password)) {
            toast.error('La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número');
            return;
        }

        // Validación de fecha de nacimiento
        const birthDate = new Date(createForm.dateOfBirth);
        const today = new Date();
        const age = today.getFullYear() - birthDate.getFullYear();
        
        if (age < 13 || age > 120) {
            toast.error('La edad debe estar entre 13 y 120 años');
            return;
        }

        // Validación de teléfono (si se proporciona)
        if (createForm.phone && createForm.phone.trim() !== '') {
            const phoneNum = parseInt(createForm.phone);
            if (isNaN(phoneNum) || phoneNum <= 0) {
                toast.error('Por favor ingrese un número de teléfono válido');
                return;
            }
        }

        try {
            setActionLoading(true);
            
            // Preparar datos del usuario
            const userData = {
                dni: createForm.dni.trim(),
                typeId: parseInt(createForm.typeId),
                email: createForm.email.trim().toLowerCase(),
                password: createForm.password,
                firstName: createForm.firstName.trim(),
                lastName: createForm.lastName.trim(),
                dateOfBirth: createForm.dateOfBirth,
                genderId: parseInt(createForm.genderId),
                country: createForm.country.trim(),
                city: createForm.city.trim(),
                phone: createForm.phone && createForm.phone.trim() !== '' ? parseInt(createForm.phone) : null,
            };
            
            console.log('Sending user data:', userData);
            console.log('Data types:', {
                dni: typeof userData.dni,
                typeId: typeof userData.typeId,
                email: typeof userData.email,
                password: typeof userData.password,
                firstName: typeof userData.firstName,
                lastName: typeof userData.lastName,
                dateOfBirth: typeof userData.dateOfBirth,
                genderId: typeof userData.genderId,
                country: typeof userData.country,
                city: typeof userData.city,
                phone: typeof userData.phone,
            });
            
            await userService.createUser(userData);
            
            toast.success('Usuario creado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setCreateDialogOpen(false);
            
            // Limpiar formulario
            setCreateForm({
                dni: '',
                typeId: '',
                email: '',
                password: '',
                firstName: '',
                lastName: '',
                dateOfBirth: '',
                genderId: '',
                country: '',
                city: '',
                phone: '',
            });
            
            fetchUsers();
        } catch (err) {
            console.error('Error creating user:', err);
            
            let errorMessage = 'Error al crear el usuario';
            
            if (err.response?.data) {
                if (typeof err.response.data === 'string') {
                    errorMessage += `: ${err.response.data}`;
                } else if (err.response.data.message) {
                    errorMessage += `: ${err.response.data.message}`;
                } else if (err.response.data.detail) {
                    errorMessage += `: ${err.response.data.detail}`;
                } else if (err.response.data.errors) {
                    const errors = Array.isArray(err.response.data.errors) 
                        ? err.response.data.errors.join(', ')
                        : Object.values(err.response.data.errors).flat().join(', ');
                    errorMessage += `: ${errors}`;
                }
            } else if (err.message) {
                errorMessage += `: ${err.message}`;
            }
            
            // Casos específicos de error
            if (err.response?.status === 409) {
                errorMessage = 'Ya existe un usuario con este DNI o email';
            } else if (err.response?.status === 422) {
                errorMessage = 'Los datos proporcionados no son válidos. Verifique todos los campos';
            } else if (err.response?.status === 500) {
                errorMessage = 'Error interno del servidor. Contacte al administrador';
            }
            
            toast.error(errorMessage, {
                position: "top-right",
                autoClose: 8000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });
        } finally {
            setActionLoading(false);
        }
    };

    const handleViewOpen = (user) => {
        setSelectedUser(user);
        setViewDialogOpen(true);
    };

    const handleEditOpen = (user) => {
        setSelectedUser(user);
        setUpdateForm({
            firstName: user.firstName,
            lastName: user.lastName,
            dateOfBirth: user.dateOfBirth,
            country: user.country,
            city: user.city,
            phone: user.phone ? user.phone.toString() : '',
            isVerified: user.isVerified
        });
        setEditDialogOpen(true);
    };

    const handleEditConfirm = async () => {
        if (!updateForm.firstName.trim() || !updateForm.lastName.trim() || 
            !updateForm.dateOfBirth || !updateForm.country.trim() || !updateForm.city.trim()) {
            toast.error('Los campos obligatorios deben ser completados');
            return;
        }

        try {
            setActionLoading(true);
            const userData = {
                ...updateForm,
                phone: updateForm.phone ? parseInt(updateForm.phone) : null
            };
            await userService.updateUser(selectedUser.id, userData);
            
            toast.success('Usuario actualizado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setEditDialogOpen(false);
            setSelectedUser(null);
            fetchUsers();
        } catch (err) {
            console.error('Error updating user:', err);
            toast.error('Error al actualizar el usuario', {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });
        } finally {
            setActionLoading(false);
        }
    };

    const handleSecurityOpen = (user) => {
        setSelectedUser(user);
        setSecurityForm({
            email: user.email,
            newPassword: ''
        });
        setSecurityDialogOpen(true);
    };

    const handleSecurityConfirm = async () => {
        if (!securityForm.email.trim() || !securityForm.newPassword.trim()) {
            toast.error('Email y nueva contraseña son requeridos');
            return;
        }

        try {
            setActionLoading(true);

            const securityData = {
                email: securityForm.email.trim(),
                newPassword: securityForm.newPassword.trim()
            };
            console.log('Sending security data:', securityData);

            await userService.updateUserSecurity(selectedUser.id, securityData);
            
            toast.success('Seguridad del usuario actualizada exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setSecurityDialogOpen(false);
            setSelectedUser(null);
            fetchUsers();
        } catch (err) {
            console.error('Error updating user security:', err);
            toast.error('Error al actualizar la seguridad del usuario', {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });
        } finally {
            setActionLoading(false);
        }
    };

    const handleDeleteOpen = (user) => {
        setSelectedUser(user);
        setDeleteDialogOpen(true);
    };

    const handleDeleteConfirm = async () => {
        try {
            setActionLoading(true);
            await userService.deleteUser(selectedUser.id);
            
            toast.success('Usuario eliminado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setDeleteDialogOpen(false);
            setSelectedUser(null);
            fetchUsers();
        } catch (err) {
            console.error('Error deleting user:', err);
            toast.error('Error al eliminar el usuario', {
                position: "top-right",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });
        } finally {
            setActionLoading(false);
        }
    };

    const handleCloseDialogs = () => {
        setDeleteDialogOpen(false);
        setEditDialogOpen(false);
        setCreateDialogOpen(false);
        setViewDialogOpen(false);
        setSecurityDialogOpen(false);
        setSelectedUser(null);
    };

    const formatDate = (dateString) => {
        if (!dateString) return '-';
        return new Date(dateString).toLocaleDateString('es-ES');
    };

    if (loading) {
        return (
            <Container maxWidth="xl" sx={{ py: 4 }}>
                <HeaderContainer>
                    <TitleContainer>
                        <IconContainer>
                            <PersonIcon />
                        </IconContainer>
                        <Typography 
                            variant="h3" 
                            sx={{ 
                                color: '#f1f5f9',
                                fontWeight: 'bold',
                            }}
                        >
                            Gestión de Usuarios
                        </Typography>
                    </TitleContainer>
                </HeaderContainer>
                
                <LoadingContainer>
                    <CircularProgress 
                        size={60}
                        sx={{ color: '#8b5cf6' }}
                    />
                </LoadingContainer>
            </Container>
        );
    }

    if (error) {
        return (
            <Container maxWidth="xl" sx={{ py: 4 }}>
                <HeaderContainer>
                    <TitleContainer>
                        <IconContainer>
                            <PersonIcon />
                        </IconContainer>
                        <Typography 
                            variant="h3" 
                            sx={{ 
                                color: '#f1f5f9',
                                fontWeight: 'bold',
                            }}
                        >
                            Gestión de Usuarios
                        </Typography>
                    </TitleContainer>
                </HeaderContainer>
                
                <Alert 
                    severity="error" 
                    sx={{ 
                        backgroundColor: 'rgba(239, 68, 68, 0.1)',
                        color: '#f87171',
                        border: '1px solid #ef4444',
                        '& .MuiAlert-icon': {
                            color: '#ef4444'
                        }
                    }}
                >
                    {error}
                </Alert>
            </Container>
        );
    }

    return (
        <Container maxWidth="xl" sx={{ py: 4 }}>
            <HeaderContainer>
                <TitleContainer>
                    <IconContainer>
                        <PersonIcon />
                    </IconContainer>
                    <Typography 
                        variant="h3" 
                        sx={{ 
                            color: '#f1f5f9',
                            fontWeight: 'bold',
                        }}
                    >
                        Gestión de Usuarios
                    </Typography>
                </TitleContainer>
            </HeaderContainer>

            <Typography 
                variant="h6" 
                sx={{ 
                    color: '#94a3b8',
                    textAlign: 'center',
                    mb: 4
                }}
            >
                Total de usuarios registrados: {users.length}
            </Typography>

            <Container sx={{ mb: 4, display: 'flex', justifyContent: 'flex-end'}}>
                <Button
                    variant="contained"
                    startIcon={<AddIcon />}
                    onClick={handleCreateOpen}
                    sx={{
                        background: 'linear-gradient(90deg, #4caf50, #66bb6a)',
                        fontWeight: 'bold',
                        px: 3,
                        py: 1.5,
                        '&:hover': {
                            background: 'linear-gradient(90deg, #66bb6a, #4caf50)',
                            transform: 'translateY(-2px)',
                            boxShadow: '0 6px 20px rgba(76, 175, 80, 0.3)',
                        },
                        transition: 'all 0.2s ease-in-out',
                    }}
                >
                    Agregar Usuario
                </Button>
            </Container>

            <StyledTableContainer component={Paper}>
                <Table>
                    <StyledTableHead>
                        <TableRow>
                            <TableCell align="center">ID</TableCell>
                            <TableCell align="center">Avatar</TableCell>
                            <TableCell align="center">DNI</TableCell>
                            <TableCell align="center">Nombre</TableCell>
                            <TableCell align="center">Email</TableCell>
                            <TableCell align="center">País</TableCell>
                            <TableCell align="center">Estado</TableCell>
                            <TableCell align="center" sx={{ width: '200px' }}>Acciones</TableCell>
                        </TableRow>
                    </StyledTableHead>
                    <TableBody>
                        {users.length === 0 ? (
                            <StyledTableRow>
                                <TableCell 
                                    colSpan={8} 
                                    align="center"
                                    sx={{ 
                                        py: 4,
                                        color: '#94a3b8',
                                        fontStyle: 'italic'
                                    }}
                                >
                                    No hay usuarios registrados
                                </TableCell>
                            </StyledTableRow>
                        ) : (
                            users.map((user) => (
                                <StyledTableRow key={user.id}>
                                    <TableCell align="center">
                                        {user.id}
                                    </TableCell>
                                    <TableCell align="center">
                                        <Avatar
                                            src={user.avatar}
                                            alt={`${user.firstName} ${user.lastName}`}
                                            sx={{ 
                                                width: 40, 
                                                height: 40, 
                                                margin: '0 auto',
                                                backgroundColor: '#8b5cf6'
                                            }}
                                        >
                                            {user.firstName?.[0]}{user.lastName?.[0]}
                                        </Avatar>
                                    </TableCell>
                                    <TableCell align="center">
                                        {user.dni}
                                    </TableCell>
                                    <TableCell align="center">
                                        {user.firstName} {user.lastName}
                                    </TableCell>
                                    <TableCell align="center">
                                        {user.email}
                                    </TableCell>
                                    <TableCell align="center">
                                        {user.country}
                                    </TableCell>
                                    <TableCell align="center">
                                        <Chip
                                            label={user.isVerified ? 'Verificado' : 'No Verificado'}
                                            color={user.isVerified ? 'success' : 'warning'}
                                            size="small"
                                            sx={{
                                                backgroundColor: user.isVerified ? '#10b981' : '#f59e0b',
                                                color: '#fff',
                                                fontWeight: 'bold'
                                            }}
                                        />
                                    </TableCell>
                                    <ActionCell align="center">
                                        <IconButton
                                            className="view-button"
                                            onClick={() => handleViewOpen(user)}
                                            size="small"
                                            title="Ver detalles"
                                        >
                                            <ViewIcon />
                                        </IconButton>
                                        <IconButton
                                            className="edit-button"
                                            onClick={() => handleEditOpen(user)}
                                            size="small"
                                            title="Editar usuario"
                                        >
                                            <EditIcon />
                                        </IconButton>
                                        <IconButton
                                            className="security-button"
                                            onClick={() => handleSecurityOpen(user)}
                                            size="small"
                                            title="Cambiar credenciales"
                                        >
                                            <SecurityIcon />
                                        </IconButton>
                                        <IconButton
                                            className="delete-button"
                                            onClick={() => handleDeleteOpen(user)}
                                            size="small"
                                            title="Eliminar usuario"
                                        >
                                            <DeleteIcon />
                                        </IconButton>
                                    </ActionCell>
                                </StyledTableRow>
                            ))
                        )}
                    </TableBody>
                </Table>
            </StyledTableContainer>

            {/* Modal para crear usuario */}
            <Dialog
                open={createDialogOpen}
                onClose={handleCloseDialogs}
                maxWidth="md"
                fullWidth
                PaperProps={{
                    sx: {
                        backgroundColor: '#1e293b',
                        color: '#f1f5f9',
                        border: '1px solid #475569',
                    }
                }}
            >
                <DialogTitle sx={{ color: '#f1f5f9', borderBottom: '1px solid #475569' }}>
                    Agregar Nuevo Usuario
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="DNI *"
                                fullWidth
                                variant="outlined"
                                value={createForm.dni}
                                onChange={(e) => setCreateForm({...createForm, dni: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledFormControl fullWidth margin="dense">
                                <InputLabel>Tipo de Usuario *</InputLabel>
                                <Select
                                    value={createForm.typeId}
                                    onChange={(e) => setCreateForm({...createForm, typeId: e.target.value})}
                                    disabled={actionLoading}
                                >
                                    <MenuItem value={1}>COMPETITOR</MenuItem>
                                    <MenuItem value={2}>ORGANIZER</MenuItem>
                                    <MenuItem value={3}>ADMIN</MenuItem>
                                </Select>
                            </StyledFormControl>
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="Nombre *"
                                fullWidth
                                variant="outlined"
                                value={createForm.firstName}
                                onChange={(e) => setCreateForm({...createForm, firstName: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="Apellido *"
                                fullWidth
                                variant="outlined"
                                value={createForm.lastName}
                                onChange={(e) => setCreateForm({...createForm, lastName: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <StyledTextField
                                margin="dense"
                                label="Email *"
                                type="email"
                                fullWidth
                                variant="outlined"
                                value={createForm.email}
                                onChange={(e) => setCreateForm({...createForm, email: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <StyledTextField
                                margin="dense"
                                label="Contraseña *"
                                type="password"
                                fullWidth
                                variant="outlined"
                                value={createForm.password}
                                onChange={(e) => setCreateForm({...createForm, password: e.target.value})}
                                disabled={actionLoading}
                                helperText="Mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula y 1 número"
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="Fecha de Nacimiento *"
                                type="date"
                                fullWidth
                                variant="outlined"
                                value={createForm.dateOfBirth}
                                onChange={(e) => setCreateForm({...createForm, dateOfBirth: e.target.value})}
                                disabled={actionLoading}
                                InputLabelProps={{ shrink: true }}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledFormControl fullWidth margin="dense">
                                <InputLabel>Género *</InputLabel>
                                <Select
                                    value={createForm.genderId}
                                    onChange={(e) => setCreateForm({...createForm, genderId: e.target.value})}
                                    disabled={actionLoading}
                                >
                                    <MenuItem value={1}>Masculino</MenuItem>
                                    <MenuItem value={2}>Femenino</MenuItem>
                                </Select>
                            </StyledFormControl>
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="País *"
                                fullWidth
                                variant="outlined"
                                value={createForm.country}
                                onChange={(e) => setCreateForm({...createForm, country: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="Ciudad *"
                                fullWidth
                                variant="outlined"
                                value={createForm.city}
                                onChange={(e) => setCreateForm({...createForm, city: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="Teléfono"
                                type="number"
                                fullWidth
                                variant="outlined"
                                value={createForm.phone}
                                onChange={(e) => setCreateForm({...createForm, phone: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                    </Grid>
                </DialogContent>
                <DialogActions sx={{ p: 3, borderTop: '1px solid #475569' }}>
                    <Button 
                        onClick={handleCloseDialogs}
                        disabled={actionLoading}
                        sx={{ color: '#94a3b8' }}
                    >
                        Cancelar
                    </Button>
                    <Button
                        onClick={handleCreateConfirm}
                        variant="contained"
                        disabled={actionLoading}
                        sx={{
                            background: 'linear-gradient(90deg, #4caf50, #66bb6a)',
                            '&:hover': {
                                background: 'linear-gradient(90deg, #66bb6a, #4caf50)',
                            },
                            '&:disabled': {
                                backgroundColor: '#475569',
                                color: '#94a3b8',
                            },
                        }}
                    >
                        {actionLoading ? <CircularProgress size={20} sx={{ color: '#fff' }} /> : 'Crear Usuario'}
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Modal para ver detalles del usuario */}
            <Dialog
                open={viewDialogOpen}
                onClose={handleCloseDialogs}
                maxWidth="md"
                fullWidth
                PaperProps={{
                    sx: {
                        backgroundColor: '#1e293b',
                        color: '#f1f5f9',
                        border: '1px solid #475569',
                    }
                }}
            >
                <DialogTitle sx={{ color: '#f1f5f9', borderBottom: '1px solid #475569' }}>
                    Detalles del Usuario
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    {selectedUser && (
                        <Grid container spacing={3}>
                            <Grid item xs={12} display="flex" justifyContent="center" mb={2}>
                                <Avatar
                                    src={selectedUser.avatar}
                                    alt={`${selectedUser.firstName} ${selectedUser.lastName}`}
                                    sx={{ 
                                        width: 100, 
                                        height: 100, 
                                        backgroundColor: '#8b5cf6',
                                        fontSize: '2rem'
                                    }}
                                >
                                    {selectedUser.firstName?.[0]}{selectedUser.lastName?.[0]}
                                </Avatar>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">ID</Typography>
                                <Typography variant="body1">{selectedUser.id}</Typography>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">DNI</Typography>
                                <Typography variant="body1">{selectedUser.dni}</Typography>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">Nombre Completo</Typography>
                                <Typography variant="body1">{selectedUser.firstName} {selectedUser.lastName}</Typography>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">Email</Typography>
                                <Typography variant="body1">{selectedUser.email}</Typography>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">Fecha de Nacimiento</Typography>
                                <Typography variant="body1">{formatDate(selectedUser.dateOfBirth)}</Typography>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">Género</Typography>
                                <Typography variant="body1">{selectedUser.genderId?.name || 'No especificado'}</Typography>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">País</Typography>
                                <Typography variant="body1">{selectedUser.country}</Typography>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">Ciudad</Typography>
                                <Typography variant="body1">{selectedUser.city}</Typography>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">Teléfono</Typography>
                                <Typography variant="body1">{selectedUser.phone || 'No especificado'}</Typography>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">Tipo de Usuario</Typography>
                                <Typography variant="body1">{selectedUser.type?.name || 'No especificado'}</Typography>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">Estado</Typography>
                                <Chip
                                    label={selectedUser.isVerified ? 'Verificado' : 'No Verificado'}
                                    color={selectedUser.isVerified ? 'success' : 'warning'}
                                    size="small"
                                    sx={{
                                        backgroundColor: selectedUser.isVerified ? '#10b981' : '#f59e0b',
                                        color: '#fff',
                                        fontWeight: 'bold'
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <Typography variant="body2" color="#94a3b8">Fecha de Registro</Typography>
                                <Typography variant="body1">{formatDate(selectedUser.createdAt)}</Typography>
                            </Grid>
                        </Grid>
                    )}
                </DialogContent>
                <DialogActions sx={{ p: 3, borderTop: '1px solid #475569' }}>
                    <Button 
                        onClick={handleCloseDialogs}
                        sx={{ color: '#94a3b8' }}
                    >
                        Cerrar
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Modal para editar usuario */}
            <Dialog
                open={editDialogOpen}
                onClose={handleCloseDialogs}
                maxWidth="md"
                fullWidth
                PaperProps={{
                    sx: {
                        backgroundColor: '#1e293b',
                        color: '#f1f5f9',
                        border: '1px solid #475569',
                    }
                }}
            >
                <DialogTitle sx={{ color: '#f1f5f9', borderBottom: '1px solid #475569' }}>
                    Editar Usuario
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="Nombre *"
                                fullWidth
                                variant="outlined"
                                value={updateForm.firstName}
                                onChange={(e) => setUpdateForm({...updateForm, firstName: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="Apellido *"
                                fullWidth
                                variant="outlined"
                                value={updateForm.lastName}
                                onChange={(e) => setUpdateForm({...updateForm, lastName: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="Fecha de Nacimiento *"
                                type="date"
                                fullWidth
                                variant="outlined"
                                value={updateForm.dateOfBirth}
                                onChange={(e) => setUpdateForm({...updateForm, dateOfBirth: e.target.value})}
                                disabled={actionLoading}
                                InputLabelProps={{ shrink: true }}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="País *"
                                fullWidth
                                variant="outlined"
                                value={updateForm.country}
                                onChange={(e) => setUpdateForm({...updateForm, country: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="Ciudad *"
                                fullWidth
                                variant="outlined"
                                value={updateForm.city}
                                onChange={(e) => setUpdateForm({...updateForm, city: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <StyledTextField
                                margin="dense"
                                label="Teléfono"
                                type="number"
                                fullWidth
                                variant="outlined"
                                value={updateForm.phone}
                                onChange={(e) => setUpdateForm({...updateForm, phone: e.target.value})}
                                disabled={actionLoading}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <StyledFormControl fullWidth margin="dense">
                                <InputLabel>Estado de Verificación</InputLabel>
                                <Select
                                    value={updateForm.isVerified}
                                    onChange={(e) => setUpdateForm({...updateForm, isVerified: e.target.value})}
                                    disabled={actionLoading}
                                >
                                    <MenuItem value={true}>Verificado</MenuItem>
                                    <MenuItem value={false}>No Verificado</MenuItem>
                                </Select>
                            </StyledFormControl>
                        </Grid>
                    </Grid>
                </DialogContent>
                <DialogActions sx={{ p: 3, borderTop: '1px solid #475569' }}>
                    <Button 
                        onClick={handleCloseDialogs}
                        disabled={actionLoading}
                        sx={{ color: '#94a3b8' }}
                    >
                        Cancelar
                    </Button>
                    <Button
                        onClick={handleEditConfirm}
                        variant="contained"
                        disabled={actionLoading}
                        sx={{
                            backgroundColor: '#8b5cf6',
                            '&:hover': {
                                backgroundColor: '#a855f7',
                            },
                            '&:disabled': {
                                backgroundColor: '#475569',
                                color: '#94a3b8',
                            },
                        }}
                    >
                        {actionLoading ? <CircularProgress size={20} sx={{ color: '#fff' }} /> : 'Actualizar'}
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Modal para cambiar credenciales */}
            <Dialog
                open={securityDialogOpen}
                onClose={handleCloseDialogs}
                maxWidth="sm"
                fullWidth
                PaperProps={{
                    sx: {
                        backgroundColor: '#1e293b',
                        color: '#f1f5f9',
                        border: '1px solid #475569',
                    }
                }}
            >
                <DialogTitle sx={{ color: '#f1f5f9', borderBottom: '1px solid #475569' }}>
                    Cambiar Credenciales de Seguridad
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <StyledTextField
                        margin="dense"
                        label="Email *"
                        type="email"
                        fullWidth
                        variant="outlined"
                        value={securityForm.email}
                        onChange={(e) => setSecurityForm({...securityForm, email: e.target.value})}
                        disabled={actionLoading}
                    />
                    <StyledTextField
                        margin="dense"
                        label="Nueva Contraseña *"
                        type="password"
                        fullWidth
                        variant="outlined"
                        value={securityForm.newPassword}
                        onChange={(e) => setSecurityForm({...securityForm, newPassword: e.target.value})}
                        disabled={actionLoading}
                        helperText="Mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula y 1 número"
                    />
                </DialogContent>
                <DialogActions sx={{ p: 3, borderTop: '1px solid #475569' }}>
                    <Button 
                        onClick={handleCloseDialogs}
                        disabled={actionLoading}
                        sx={{ color: '#94a3b8' }}
                    >
                        Cancelar
                    </Button>
                    <Button
                        onClick={handleSecurityConfirm}
                        variant="contained"
                        disabled={actionLoading}
                        sx={{
                            backgroundColor: '#f59e0b',
                            '&:hover': {
                                backgroundColor: '#d97706',
                            },
                            '&:disabled': {
                                backgroundColor: '#475569',
                                color: '#94a3b8',
                            },
                        }}
                    >
                        {actionLoading ? <CircularProgress size={20} sx={{ color: '#fff' }} /> : 'Actualizar Credenciales'}
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Modal para eliminar usuario */}
            <Dialog
                open={deleteDialogOpen}
                onClose={handleCloseDialogs}
                maxWidth="sm"
                fullWidth
                PaperProps={{
                    sx: {
                        backgroundColor: '#1e293b',
                        color: '#f1f5f9',
                        border: '1px solid #475569',
                    }
                }}
            >
                <DialogTitle sx={{ color: '#f1f5f9', borderBottom: '1px solid #475569' }}>
                    Confirmar Eliminación
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <DialogContentText sx={{ color: '#94a3b8' }}>
                        ¿Estás seguro de que deseas eliminar al usuario "{selectedUser?.firstName} {selectedUser?.lastName}"? 
                        Esta acción no se puede deshacer y eliminará todos los datos asociados.
                    </DialogContentText>
                </DialogContent>
                <DialogActions sx={{ p: 3, borderTop: '1px solid #475569' }}>
                    <Button 
                        onClick={handleCloseDialogs}
                        disabled={actionLoading}
                        sx={{ color: '#94a3b8' }}
                    >
                        Cancelar
                    </Button>
                    <Button
                        onClick={handleDeleteConfirm}
                        variant="contained"
                        disabled={actionLoading}
                        sx={{
                            backgroundColor: '#ef4444',
                            '&:hover': {
                                backgroundColor: '#dc2626',
                            },
                            '&:disabled': {
                                backgroundColor: '#475569',
                                color: '#94a3b8',
                            },
                        }}
                    >
                        {actionLoading ? <CircularProgress size={20} sx={{ color: '#fff' }} /> : 'Eliminar'}
                    </Button>
                </DialogActions>
            </Dialog>
        </Container>
    );
};

export default UserAdminDashboard;