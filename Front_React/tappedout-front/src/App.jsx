import { BrowserRouter } from 'react-router-dom'
import AppRoutes from './AppRoutes'

import Header from './components/Header'
import { Box } from '@mui/material'

function App() {
  return (
    <BrowserRouter>
      <Box sx={{ minHeight: '100vh', width: '100vw', backgroundColor: '#121212' }}>
        <Header />
        <AppRoutes />
      </Box>
    </BrowserRouter>
  )
}

export default App
