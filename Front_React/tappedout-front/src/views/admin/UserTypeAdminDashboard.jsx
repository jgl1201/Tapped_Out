import { useState, useEffect } from "react";
import { toast } from "react-toastify";

import { Alert, Box, Button, CircularProgress, Container, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography, styled } from "@mui/material";
import { Add as AddIcon, Delete as DeleteIcon, Edit as EditIcon, VerifiedUser } from "@mui/icons-material";

import userTypeService from "../../service/userTypeService";

const StyledTableContainer = styled(TableContainer)({
    background: 'linear-gradient(135deg, #1e293b 0%, #334155 100%)',
    border: '1px solid #475569',
    borderRadius: '8px',
    '& .MuiTable-root': {
        minWidth: '400px'
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
        fontSize: '1rem',
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
        margin: '0 4px',
        transition: 'all 0.2s ease-in-out',
    },
    '& .edit-button': {
        color: '#8b5cf6',
        '&:hover': {
            backgroundColor: 'rgba(139, 92, 246, 0.1)',
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

const UserTypeAdminDashboard = () => {
    const [userTypes, setUserTypes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [editDialogOpen, setEditDialogOpen] = useState(false);
    const [createDialogOpen, setCreateDialogOpen] = useState(false);
    const [selectedUserType, setSelectedUserType] = useState(null);
    const [userTypeName, setUserTypeName] = useState('');
    const [actionLoading, setActionLoading] = useState(false);

    useEffect(() => {
        fetchUserTypes();
    }, []);

    const fetchUserTypes = async () => {
        try {
            setLoading(true);
            setError(null);
            
            const response = await userTypeService.getAllUserTypes();
            const sortedUserTypes = Array.isArray(response) ? response.sort((a, b) => a.id - b.id) : [];

            setUserTypes(sortedUserTypes);
        } catch (err) {
            console.error('Error fetching user types:', err);
            setError('Error al cargar los tipos de usuario');
            
            toast.error('Error al cargar los tipos de usuario', {
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
        setUserTypeName('');
        setCreateDialogOpen(true);
    };

    const handleCreateConfirm = async () => {
        if (!userTypeName.trim()) {
            toast.error('El nombre del tipo de usuario es requerido');
            return;
        }

        try {
            setActionLoading(true);
            const userTypeData = { name: userTypeName.trim() };
            await userTypeService.createUserType(userTypeData);
            
            toast.success('Tipo de usuario creado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setCreateDialogOpen(false);
            setUserTypeName('');
            fetchUserTypes(); // Recargar la lista
        } catch (err) {
            console.error('Error creating user type:', err);
            toast.error('Error al crear el tipo de usuario', {
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

    const handleEditOpen = (userType) => {
        setSelectedUserType(userType);
        setUserTypeName(userType.name);
        setEditDialogOpen(true);
    };

    const handleEditConfirm = async () => {
        if (!userTypeName.trim()) {
            toast.error('El nombre del tipo de usuario es requerido');
            return;
        }

        try {
            setActionLoading(true);
            const userTypeData = { name: userTypeName.trim() };
            await userTypeService.updateUserType(selectedUserType.id, userTypeData);
            
            toast.success('Tipo de usuario actualizado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setEditDialogOpen(false);
            setSelectedUserType(null);
            setUserTypeName('');
            fetchUserTypes(); // Recargar la lista
        } catch (err) {
            console.error('Error updating user type:', err);
            toast.error('Error al actualizar el tipo de usuario', {
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

    const handleDeleteOpen = (userType) => {
        setSelectedUserType(userType);
        setDeleteDialogOpen(true);
    };

    const handleDeleteConfirm = async () => {
        try {
            setActionLoading(true);
            await userTypeService.deleteUserType(selectedUserType.id);
            
            toast.success('Tipo de usuario eliminado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setDeleteDialogOpen(false);
            setSelectedUserType(null);
            fetchUserTypes(); // Recargar la lista
        } catch (err) {
            console.error('Error deleting user type:', err);
            toast.error('Error al eliminar el tipo de usuario', {
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
        setSelectedUserType(null);
        setUserTypeName('');
    };

    if (loading) {
        return (
            <Container maxWidth="lg" sx={{ py: 4 }}>
                <HeaderContainer>
                    <TitleContainer>
                        <IconContainer>
                            <VerifiedUser />
                        </IconContainer>
                        <Typography 
                            variant="h3" 
                            sx={{ 
                                color: '#f1f5f9',
                                fontWeight: 'bold',
                            }}
                        >
                            Gestión de Tipos de Usuario
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
            <Container maxWidth="lg" sx={{ py: 4 }}>
                <HeaderContainer>
                    <TitleContainer>
                        <IconContainer>
                            <VerifiedUser />
                        </IconContainer>
                        <Typography 
                            variant="h3" 
                            sx={{ 
                                color: '#f1f5f9',
                                fontWeight: 'bold',
                            }}
                        >
                            Gestión de Tipos de Usuario
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
        <Container maxWidth="lg" sx={{ py: 4 }}>
            <HeaderContainer>
                <TitleContainer>
                    <IconContainer>
                        <VerifiedUser />
                    </IconContainer>
                    <Typography 
                        variant="h3" 
                        sx={{ 
                            color: '#f1f5f9',
                            fontWeight: 'bold',
                        }}
                    >
                        Gestión de Tipos de Usuario
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
                Total de tipos de usuario registrados: {userTypes.length}
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
                    Agregar Tipo de Usuario
                </Button>
            </Container>

            <StyledTableContainer component={Paper}>
                <Table>
                    <StyledTableHead>
                        <TableRow>
                            <TableCell align="center">ID</TableCell>
                            <TableCell align="center">Nombre</TableCell>
                            <TableCell align="center" sx={{ width: '150px' }}>Acciones</TableCell>
                        </TableRow>
                    </StyledTableHead>
                    <TableBody>
                        {userTypes.length === 0 ? (
                            <StyledTableRow>
                                <TableCell 
                                    colSpan={3} 
                                    align="center"
                                    sx={{ 
                                        py: 4,
                                        color: '#94a3b8',
                                        fontStyle: 'italic'
                                    }}
                                >
                                    No hay tipos de usuario registrados
                                </TableCell>
                            </StyledTableRow>
                        ) : (
                            userTypes.map((userType) => (
                                <StyledTableRow key={userType.id}>
                                    <TableCell align="center">
                                        {userType.id}
                                    </TableCell>
                                    <TableCell align="center">
                                        {userType.name}
                                    </TableCell>
                                    <ActionCell align="center">
                                        <IconButton
                                            className="edit-button"
                                            onClick={() => handleEditOpen(userType)}
                                            size="small"
                                        >
                                            <EditIcon />
                                        </IconButton>
                                        <IconButton
                                            className="delete-button"
                                            onClick={() => handleDeleteOpen(userType)}
                                            size="small"
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

            {/* Modal para crear tipo de usuario */}
            <Dialog
                open={createDialogOpen}
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
                    Agregar Nuevo Tipo de Usuario
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Nombre del Tipo de Usuario"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={userTypeName}
                        onChange={(e) => setUserTypeName(e.target.value)}
                        disabled={actionLoading}
                        sx={{
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
                        }}
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
                        onClick={handleCreateConfirm}
                        variant="contained"
                        disabled={actionLoading || !userTypeName.trim()}
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
                        {actionLoading ? <CircularProgress size={20} sx={{ color: '#fff' }} /> : 'Crear'}
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Modal para editar tipo de usuario */}
            <Dialog
                open={editDialogOpen}
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
                    Editar Tipo de Usuario
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Nombre del Tipo de Usuario"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={userTypeName}
                        onChange={(e) => setUserTypeName(e.target.value)}
                        disabled={actionLoading}
                        sx={{
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
                        }}
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
                        onClick={handleEditConfirm}
                        variant="contained"
                        disabled={actionLoading || !userTypeName.trim()}
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

            {/* Modal para eliminar tipo de usuario */}
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
                        ¿Estás seguro de que deseas eliminar el tipo de usuario "{selectedUserType?.name}"? 
                        Esta acción no se puede deshacer.
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

export default UserTypeAdminDashboard;