import { useState, useEffect } from "react";
import { toast } from "react-toastify";

import { Alert, Box, Button, CircularProgress, Container, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, FormControl,IconButton, InputLabel,MenuItem,Paper, Select,Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography, styled } from "@mui/material";
import { Add as AddIcon, Delete as DeleteIcon, Edit as EditIcon, EmojiEvents } from "@mui/icons-material";

import sportLevelService from "../../service/sportLevelService";
import sportService from "../../service/sportService";

const StyledTableContainer = styled(TableContainer)({
    background: 'linear-gradient(135deg, #1e293b 0%, #334155 100%)',
    border: '1px solid #475569',
    borderRadius: '8px',
    '& .MuiTable-root': {
        minWidth: '600px'
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

const SportLevelAdminDashboard = () => {
    const [sportLevels, setSportLevels] = useState([]);
    const [sports, setSports] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [editDialogOpen, setEditDialogOpen] = useState(false);
    const [createDialogOpen, setCreateDialogOpen] = useState(false);
    const [selectedSportLevel, setSelectedSportLevel] = useState(null);
    const [sportLevelName, setSportLevelName] = useState('');
    const [selectedSportId, setSelectedSportId] = useState('');
    const [actionLoading, setActionLoading] = useState(false);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            setLoading(true);
            setError(null);
            
            const [sportLevelsResponse, sportsResponse] = await Promise.all([
                sportLevelService.getAllSportLevels(),
                sportService.getAllSports()
            ]);
            
            const sortedSportLevels = Array.isArray(sportLevelsResponse) 
                ? sportLevelsResponse.sort((a, b) => a.id - b.id) 
                : [];
            const sortedSports = Array.isArray(sportsResponse) 
                ? sportsResponse.sort((a, b) => a.name.localeCompare(b.name)) 
                : [];

            setSportLevels(sortedSportLevels);
            setSports(sortedSports);
        } catch (err) {
            console.error('Error fetching data:', err);
            setError('Error al cargar los niveles deportivos');
            
            toast.error('Error al cargar los niveles deportivos', {
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
        setSportLevelName('');
        setSelectedSportId('');
        setCreateDialogOpen(true);
    };

    const handleCreateConfirm = async () => {
        if (!sportLevelName.trim()) {
            toast.error('El nombre del nivel deportivo es requerido');
            return;
        }

        if (!selectedSportId) {
            toast.error('Debe seleccionar un deporte');
            return;
        }

        try {
            setActionLoading(true);
            const sportLevelData = { 
                name: sportLevelName.trim(),
                sportId: selectedSportId
            };
            await sportLevelService.createSportLevel(sportLevelData);
            
            toast.success('Nivel deportivo creado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setCreateDialogOpen(false);
            setSportLevelName('');
            setSelectedSportId('');
            fetchData(); // Recargar la lista
        } catch (err) {
            console.error('Error creating sport level:', err);
            toast.error('Error al crear el nivel deportivo', {
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

    const handleEditOpen = (sportLevel) => {
        setSelectedSportLevel(sportLevel);
        setSportLevelName(sportLevel.name);
        setSelectedSportId(sportLevel.sport.id);
        setEditDialogOpen(true);
    };

    const handleEditConfirm = async () => {
        if (!sportLevelName.trim()) {
            toast.error('El nombre del nivel deportivo es requerido');
            return;
        }

        if (!selectedSportId) {
            toast.error('Debe seleccionar un deporte');
            return;
        }

        try {
            setActionLoading(true);
            const sportLevelData = { 
                name: sportLevelName.trim(),
                sportId: selectedSportId
            };
            await sportLevelService.updateSportLevel(selectedSportLevel.id, sportLevelData);
            
            toast.success('Nivel deportivo actualizado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setEditDialogOpen(false);
            setSelectedSportLevel(null);
            setSportLevelName('');
            setSelectedSportId('');
            fetchData(); // Recargar la lista
        } catch (err) {
            console.error('Error updating sport level:', err);
            toast.error('Error al actualizar el nivel deportivo', {
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

    const handleDeleteOpen = (sportLevel) => {
        setSelectedSportLevel(sportLevel);
        setDeleteDialogOpen(true);
    };

    const handleDeleteConfirm = async () => {
        try {
            setActionLoading(true);
            await sportLevelService.deleteSportLevel(selectedSportLevel.id);
            
            toast.success('Nivel deportivo eliminado exitosamente', {
                position: "top-right",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
                pauseOnHover: true,
                draggable: true,
            });

            setDeleteDialogOpen(false);
            setSelectedSportLevel(null);
            fetchData(); // Recargar la lista
        } catch (err) {
            console.error('Error deleting sport level:', err);
            toast.error('Error al eliminar el nivel deportivo', {
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
        setSelectedSportLevel(null);
        setSportLevelName('');
        setSelectedSportId('');
    };

    if (loading) {
        return (
            <Container maxWidth="lg" sx={{ py: 4 }}>
                <HeaderContainer>
                    <TitleContainer>
                        <IconContainer>
                            <EmojiEvents />
                        </IconContainer>
                        <Typography 
                            variant="h3" 
                            sx={{ 
                                color: '#f1f5f9',
                                fontWeight: 'bold',
                            }}
                        >
                            Gestión de Niveles Deportivos
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
                            <EmojiEvents />
                        </IconContainer>
                        <Typography 
                            variant="h3" 
                            sx={{ 
                                color: '#f1f5f9',
                                fontWeight: 'bold',
                            }}
                        >
                            Gestión de Niveles Deportivos
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
                        <EmojiEvents />
                    </IconContainer>
                    <Typography 
                        variant="h3" 
                        sx={{ 
                            color: '#f1f5f9',
                            fontWeight: 'bold',
                        }}
                    >
                        Gestión de Niveles Deportivos
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
                Total de niveles deportivos registrados: {sportLevels.length}
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
                    Agregar Nivel Deportivo
                </Button>
            </Container>

            <StyledTableContainer component={Paper}>
                <Table>
                    <StyledTableHead>
                        <TableRow>
                            <TableCell align="center">ID</TableCell>
                            <TableCell align="center">Nombre</TableCell>
                            <TableCell align="center">Deporte</TableCell>
                            <TableCell align="center" sx={{ width: '150px' }}>Acciones</TableCell>
                        </TableRow>
                    </StyledTableHead>
                    <TableBody>
                        {sportLevels.length === 0 ? (
                            <StyledTableRow>
                                <TableCell 
                                    colSpan={4} 
                                    align="center"
                                    sx={{ 
                                        py: 4,
                                        color: '#94a3b8',
                                        fontStyle: 'italic'
                                    }}
                                >
                                    No hay niveles deportivos registrados
                                </TableCell>
                            </StyledTableRow>
                        ) : (
                            sportLevels.map((sportLevel) => (
                                <StyledTableRow key={sportLevel.id}>
                                    <TableCell align="center">
                                        {sportLevel.id}
                                    </TableCell>
                                    <TableCell align="center">
                                        {sportLevel.name}
                                    </TableCell>
                                    <TableCell align="center">
                                        {sportLevel.sport?.name || 'N/A'}
                                    </TableCell>
                                    <ActionCell align="center">
                                        <IconButton
                                            className="edit-button"
                                            onClick={() => handleEditOpen(sportLevel)}
                                            size="small"
                                        >
                                            <EditIcon />
                                        </IconButton>
                                        <IconButton
                                            className="delete-button"
                                            onClick={() => handleDeleteOpen(sportLevel)}
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

            {/* Modal para crear nivel deportivo */}
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
                    Agregar Nuevo Nivel Deportivo
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Nombre del Nivel Deportivo"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={sportLevelName}
                        onChange={(e) => setSportLevelName(e.target.value)}
                        disabled={actionLoading}
                        sx={{
                            mb: 2,
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
                    <FormControl 
                        fullWidth 
                        variant="outlined"
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
                            '& .MuiSelect-icon': {
                                color: '#94a3b8',
                            },
                        }}
                    >
                        <InputLabel>Deporte</InputLabel>
                        <Select
                            value={selectedSportId}
                            onChange={(e) => setSelectedSportId(e.target.value)}
                            label="Deporte"
                            MenuProps={{
                                PaperProps: {
                                    sx: {
                                        backgroundColor: '#1e293b',
                                        color: '#f1f5f9',
                                        border: '1px solid #475569',
                                        '& .MuiMenuItem-root': {
                                            color: '#f1f5f9',
                                            '&:hover': {
                                                backgroundColor: 'rgba(139, 92, 246, 0.1)',
                                            },
                                            '&.Mui-selected': {
                                                backgroundColor: 'rgba(139, 92, 246, 0.2)',
                                                '&:hover': {
                                                    backgroundColor: 'rgba(139, 92, 246, 0.3)',
                                                },
                                            },
                                        },
                                    },
                                },
                            }}
                        >
                            {sports.map((sport) => (
                                <MenuItem key={sport.id} value={sport.id}>
                                    {sport.name}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
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
                        disabled={actionLoading || !sportLevelName.trim() || !selectedSportId}
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

            {/* Modal para editar nivel deportivo */}
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
                    Editar Nivel Deportivo
                </DialogTitle>
                <DialogContent sx={{ pt: 3 }}>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Nombre del Nivel Deportivo"
                        type="text"
                        fullWidth
                        variant="outlined"
                        value={sportLevelName}
                        onChange={(e) => setSportLevelName(e.target.value)}
                        disabled={actionLoading}
                        sx={{
                            mb: 2,
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
                    <FormControl 
                        fullWidth 
                        variant="outlined"
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
                            '& .MuiSelect-icon': {
                                color: '#94a3b8',
                            },
                        }}
                    >
                        <InputLabel>Deporte</InputLabel>
                        <Select
                            value={selectedSportId}
                            onChange={(e) => setSelectedSportId(e.target.value)}
                            label="Deporte"
                            MenuProps={{
                                PaperProps: {
                                    sx: {
                                        backgroundColor: '#1e293b',
                                        color: '#f1f5f9',
                                        border: '1px solid #475569',
                                        '& .MuiMenuItem-root': {
                                            color: '#f1f5f9',
                                            '&:hover': {
                                                backgroundColor: 'rgba(139, 92, 246, 0.1)',
                                            },
                                            '&.Mui-selected': {
                                                backgroundColor: 'rgba(139, 92, 246, 0.2)',
                                                '&:hover': {
                                                    backgroundColor: 'rgba(139, 92, 246, 0.3)',
                                                },
                                            },
                                        },
                                    },
                                },
                            }}
                        >
                            {sports.map((sport) => (
                                <MenuItem key={sport.id} value={sport.id}>
                                    {sport.name}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
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
                        disabled={actionLoading || !sportLevelName.trim() || !selectedSportId}
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

            {/* Modal para eliminar nivel deportivo */}
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
                        ¿Estás seguro de que deseas eliminar el nivel deportivo "{selectedSportLevel?.name}" 
                        del deporte "{selectedSportLevel?.sport?.name}"? 
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

export default SportLevelAdminDashboard;