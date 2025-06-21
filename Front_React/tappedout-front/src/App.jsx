import { BrowserRouter } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'

import { Box } from '@mui/material'

import AppRoutes from './AppRoutes'

import Header from './components/Header'

function App() {
  return (
    <BrowserRouter>
    <Box sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
        width: '100vw',
        backgroundColor: '#0d1117' }}
      >
        <Header />
        <AppRoutes />
        <ToastContainer />
      </Box>
    </BrowserRouter>
  )
}

export default App
