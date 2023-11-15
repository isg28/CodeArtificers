import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import { Link } from 'react-router-dom';

export default function ButtonAppBar() {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar sx ={{ justifyContent: 'space-between'  }}>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <Button color="inherit" component={Link} to="/">
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Scheduling App
          </Typography>
          </Button>
          <Box>
          <Button color="inherit" component={Link} to= "/custom-login"> 
          Login</Button>
          <Button color="inherit" component={Link} to= "/calendar">
          Calendar</Button>
          </Box>
        </Toolbar>
      </AppBar>
    </Box>
  );
}

// Link creates a connection with path routed in index.js
