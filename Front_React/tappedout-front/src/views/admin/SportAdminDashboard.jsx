import { useState, useEffect } from "react";
import { toast } from "react-toastify";

import { Alert, Box, Button, CircularProgress, Container, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography, styled } from "@mui/material";
import { Add as AddIcon, Delete as DeleteIcon, Edit as EditIcon, SportsBaseball } from "@mui/icons-material";

import sportService from "../../service/sportService";

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

const SportAdminDashboard = () => {
    const [sports, setSports] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [editDialogOpen, setEditDialogOpen] = useState(false);
    const [createDialogOpen, setCreateDialogOpen] = useState(false);
    const [selectedSport, setSelectedSport] = useState(null);
    const [sportName, setSportName] = useState('');
    const [actionLoading, setActionLoading] = useState(false);

    useEffect(() => {
        fetchSports();
    }, []);

    const fetchSports = async () => {
        try {
            setLoading(true);
            setError(null);
            
            const response = await sportService.getAllSports();
            const sortedSports = Array.isArray(response) ? response.sort((a, b) => a.id - b.id) : [];

            setSports(sortedSports);
        } catch (err) {
            console.error('Error fetching sports:', err);
            setError('Error al cargar los deportes');
            
            toast.error('Error al cargar los deportes', {
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
        setSportName('');
        setCreateDialogOpen(true);
    };

    const handleCreateConfirm = async () => {
        if (!sportName.trim()) {
            toast.error('El nombre del deporte es requerido');
            return;
        }

        try {
            setActionLoading(true);
            const sportData = { name: sportName.trim() };
            await sportService.createSport(sportData);
            
            toast.success('Deporte creado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setCreateDialogOpen(false);
            setSportName('');
            fetchSports(); // Recargar la lista
        } catch (err) {
            console.error('Error creating sport:', err);
            toast.error('Error al crear el deporte', {
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

    const handleEditOpen = (sport) => {
        setSelectedSport(sport);
        setSportName(sport.name);
        setEditDialogOpen(true);
    };

    const handleEditConfirm = async () => {
        if (!sportName.trim()) {
            toast.error('El nombre del deporte es requerido');
            return;
        }

        try {
            setActionLoading(true);
            const sportData = { name: sportName.trim() };
            await sportService.updateSport(selectedSport.id, sportData);
            
            toast.success('Deporte actualizado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setEditDialogOpen(false);
            setSelectedSport(null);
            setSportName('');
            fetchSports(); // Recargar la lista
        } catch (err) {
            console.error('Error updating sport:', err);
            toast.error('Error al actualizar el deporte', {
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

    const handleDeleteOpen = (sport) => {
        setSelectedSport(sport);
        setDeleteDialogOpen(true);
    };

    const handleDeleteConfirm = async () => {
        try {
            setActionLoading(true);
            await sportService.deleteSport(selectedSport.id);
            
            toast.success('Deporte eliminado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setDeleteDialogOpen(false);
            setSelectedSport(null);
            fetchSports(); // Recargar la lista
        } catch (err) {
            console.error('Error deleting sport:', err);
            toast.error('Error al eliminar el deporte', {
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
        setSelectedSport(null);
        setSportName('');
    };

    if (loading) {
        return (
            <Container maxWidth="lg" sx={{ py: 4 }}>
                <HeaderContainer>
                    <TitleContainer>
                        <IconContainer>
                            <SportsBaseball />
                        </IconContainer>
                        <Typography 
                            variant="h3" 
                            sx={{ 
                                color: '#f1f5f9',
                                fontWeight: 'bold',
                            }}
                        >
                            Gestión de Deportes
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
                            <SportsBaseball />
                        </IconContainer>
                        <Typography 
                            variant="h3" 
                            sx={{ 
                                color: '#f1f5f9',
                                fontWeight: 'bold',
                            }}
                        >
                            Gestión de Deportes
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
                        <SportsBaseball />
                    </IconContainer>
                    <Typography 
                        variant="h3" 
                        sx={{ 
                            color: '#f1f5f9',
                            fontWeight: 'bold',
                        }}
                    >
                        Gestión de Deportes
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
                Total de deportes registrados: {sports.length}
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
                    Agregar Deporte
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
                        {sports.length === 0 ? (
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
                                    No hay deportes registrados
                                </TableCell>
                            </StyledTableRow>
                        ) : (
                            sports.map((sport) => (
                                <StyledTableRow key={sport.id}>
                                    <TableCell align="center">
                                        {sport.id}
                                    </TableCell>
                                    <TableCell align="center">
                                        {sport.name}
                                    </TableCell>
                                    <ActionCell align="center">
                                        <IconButton
                                            className="edit-button"
                                            onClick={() => handleEditOpen(sport)}
                                            size="small"
                                        >
                                            <EditIcon />
                                        </IconButton>
                                        <IconButton
                                            className="delete-button"
                                            onClick={() => handleDeleteOpen(sport)}
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

            {/* Modal para crear deporte */}
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
                    Agregar Nuevo Deporte
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Nombre del Deporte"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={sportName}
                        onChange={(e) => setSportName(e.target.value)}
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
                        disabled={actionLoading || !sportName.trim()}
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

            {/* Modal para editar deporte */}
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
                    Editar Deporte
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Nombre del Deporte"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={sportName}
                        onChange={(e) => setSportName(e.target.value)}
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
                        disabled={actionLoading || !sportName.trim()}
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

            {/* Modal para eliminar deporte */}
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
                        ¿Estás seguro de que deseas eliminar el deporte "{selectedSport?.name}"? 
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

export default SportAdminDashboard;