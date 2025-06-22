import { useState, useEffect } from "react";
import { toast } from "react-toastify";

import { Alert, Box, Button, CircularProgress, Container, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography, styled } from "@mui/material";
import { Add as AddIcon, Delete as DeleteIcon, Edit as EditIcon, Wc } from "@mui/icons-material";

import genderService from "../../service/genderService";

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

const GenderAdminDashboard = () => {
    const [genders, setGenders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [editDialogOpen, setEditDialogOpen] = useState(false);
    const [createDialogOpen, setCreateDialogOpen] = useState(false);
    const [selectedGender, setSelectedGender] = useState(null);
    const [genderName, setGenderName] = useState('');
    const [actionLoading, setActionLoading] = useState(false);

    useEffect(() => {
        fetchGenders();
    }, []);

    const fetchGenders = async () => {
        try {
            setLoading(true);
            setError(null);
            
            const response = await genderService.getAllGenders();
            const sortedGenders = Array.isArray(response) ? response.sort((a, b) => a.id - b.id) : [];

            setGenders(sortedGenders);
        } catch (err) {
            console.error('Error fetching genders:', err);
            setError('Error al cargar los géneros');
            
            toast.error('Error al cargar los géneros', {
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
        setGenderName('');
        setCreateDialogOpen(true);
    };

    const handleCreateConfirm = async () => {
        if (!genderName.trim()) {
            toast.error('El nombre del género es requerido');
            return;
        }

        try {
            setActionLoading(true);
            const genderData = { name: genderName.trim() };
            await genderService.createGender(genderData);
            
            toast.success('Género creado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setCreateDialogOpen(false);
            setGenderName('');
            fetchGenders(); // Recargar la lista
        } catch (err) {
            console.error('Error creating gender:', err);
            toast.error('Error al crear el género', {
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

    const handleEditOpen = (gender) => {
        setSelectedGender(gender);
        setGenderName(gender.name);
        setEditDialogOpen(true);
    };

    const handleEditConfirm = async () => {
        if (!genderName.trim()) {
            toast.error('El nombre del género es requerido');
            return;
        }

        try {
            setActionLoading(true);
            const genderData = { name: genderName.trim() };
            await genderService.updateGender(selectedGender.id, genderData);
            
            toast.success('Género actualizado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setEditDialogOpen(false);
            setSelectedGender(null);
            setGenderName('');
            fetchGenders(); // Recargar la lista
        } catch (err) {
            console.error('Error updating gender:', err);
            toast.error('Error al actualizar el género', {
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

    const handleDeleteOpen = (gender) => {
        setSelectedGender(gender);
        setDeleteDialogOpen(true);
    };

    const handleDeleteConfirm = async () => {
        try {
            setActionLoading(true);
            await genderService.deleteGender(selectedGender.id);
            
            toast.success('Género eliminado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setDeleteDialogOpen(false);
            setSelectedGender(null);
            fetchGenders(); // Recargar la lista
        } catch (err) {
            console.error('Error deleting gender:', err);
            toast.error('Error al eliminar el género', {
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
        setSelectedGender(null);
        setGenderName('');
    };

    if (loading) {
        return (
            <Container maxWidth="lg" sx={{ py: 4 }}>
                <HeaderContainer>
                    <TitleContainer>
                        <IconContainer>
                            <Wc />
                        </IconContainer>
                        <Typography 
                            variant="h3" 
                            sx={{ 
                                color: '#f1f5f9',
                                fontWeight: 'bold',
                            }}
                        >
                            Gestión de Géneros
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
                            <Wc />
                        </IconContainer>
                        <Typography 
                            variant="h3" 
                            sx={{ 
                                color: '#f1f5f9',
                                fontWeight: 'bold',
                            }}
                        >
                            Gestión de Géneros
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
                        <Wc />
                    </IconContainer>
                    <Typography 
                        variant="h3" 
                        sx={{ 
                            color: '#f1f5f9',
                            fontWeight: 'bold',
                        }}
                    >
                        Gestión de Géneros
                    </Typography>
                </TitleContainer>
                
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
                    Agregar Género
                </Button>
            </HeaderContainer>

            <Typography 
                variant="h6" 
                sx={{ 
                    color: '#94a3b8',
                    textAlign: 'center',
                    mb: 4
                }}
            >
                Total de géneros registrados: {genders.length}
            </Typography>

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
                        {genders.length === 0 ? (
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
                                    No hay géneros registrados
                                </TableCell>
                            </StyledTableRow>
                        ) : (
                            genders.map((gender) => (
                                <StyledTableRow key={gender.id}>
                                    <TableCell align="center">
                                        {gender.id}
                                    </TableCell>
                                    <TableCell align="center">
                                        {gender.name}
                                    </TableCell>
                                    <ActionCell align="center">
                                        <IconButton
                                            className="edit-button"
                                            onClick={() => handleEditOpen(gender)}
                                            size="small"
                                        >
                                            <EditIcon />
                                        </IconButton>
                                        <IconButton
                                            className="delete-button"
                                            onClick={() => handleDeleteOpen(gender)}
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

            {/* Modal para crear género */}
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
                    Agregar Nuevo Género
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Nombre del Género"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={genderName}
                        onChange={(e) => setGenderName(e.target.value)}
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
                        disabled={actionLoading || !genderName.trim()}
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

            {/* Modal para editar género */}
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
                    Editar Género
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Nombre del Género"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={genderName}
                        onChange={(e) => setGenderName(e.target.value)}
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
                        disabled={actionLoading || !genderName.trim()}
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

            {/* Modal para eliminar género */}
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
                        ¿Estás seguro de que deseas eliminar el género "{selectedGender?.name}"? 
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

export default GenderAdminDashboard;