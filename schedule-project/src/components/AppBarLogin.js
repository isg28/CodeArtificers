import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import AdbIcon from '@mui/icons-material/Adb';
import { Link } from 'react-router-dom';
import { useNavigate } from "react-router-dom";

const pages = [];
const settings = ['Profile', 'Calendars', 'Logout'];

function ResponsiveAppBar() {
    const navigate = useNavigate();
    const [anchorElNav, setAnchorElNav] = React.useState(null);
    const [anchorElUser, setAnchorElUser] = React.useState(null);

    const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
    };
    const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
    setAnchorElNav(null);
    };

    const handleCloseUserMenu = () => {
    setAnchorElUser(null);
    };

    const handleLogout = () => {
        // Clear tokens from localStorage or sessionStorage
        localStorage.removeItem("access_token");
        localStorage.removeItem("refresh_token");

        window.alert("You have logged out. See you again!");
    
        // Redirect the user to the login page or another destination
        navigate("/custom-login");
    
        // Close the user menu after logout
        handleCloseUserMenu();
    };

    return (
    <AppBar position="static" sx={{ background: '#0F52BA' }}>
        <Container maxWidth="xl">
        <Toolbar disableGutters>
            <AdbIcon sx={{ display: { xs: 'none', md: 'flex' }, mr: 1 }} />
            <Typography
            variant="h6"
            noWrap
            component={Link} to ="/homepage"
            href="#app-bar-with-responsive-menu"
            sx={{
                mr: 2,
                display: { xs: 'none', md: 'flex' },
                fontFamily: 'monospace',
                fontWeight: 700,
                letterSpacing: '.3rem',
                color: 'inherit',
                textDecoration: 'none',
            }}
            >
            TIMEIFICER
            </Typography>

            <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
                size="large"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleOpenNavMenu}
                color="inherit"
            >
                <MenuIcon />
            </IconButton>
            <Menu
                id="menu-appbar"
                anchorEl={anchorElNav}
                anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
                }}
                keepMounted
                transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
                }}
                open={Boolean(anchorElNav)}
                onClose={handleCloseNavMenu}
                sx={{
                display: { xs: 'block', md: 'none' },
                }}
            >
                {pages.map((page) => (
                <MenuItem key={page} component = {Link} to="/calendar" onClick={handleCloseNavMenu}>
                    <Typography textAlign="center">{page}</Typography>
                </MenuItem>
                ))}
            </Menu>
            </Box>
            <AdbIcon sx={{ display: { xs: 'flex', md: 'none' }, mr: 1 }} />
            <Typography
            variant="h5"
            noWrap
            component="a"
            href="#app-bar-with-responsive-menu"
            sx={{
                mr: 2,
                display: { xs: 'flex', md: 'none' },
                flexGrow: 1,
                fontFamily: 'monospace',
                fontWeight: 700,
                letterSpacing: '.3rem',
                color: 'inherit',
                textDecoration: 'none',
            }}
            >
            LOGO
            </Typography>
            <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
            {pages.map((page) => (
                <Button
                key={page}
                onClick={handleCloseNavMenu}
                sx={{ my: 2, color: 'white', display: 'block' }}
                >
                {page}
                </Button>
            ))}
            </Box>

            <Box sx={{ flexGrow: 0 }}>
            <Tooltip title="Open settings">
                <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                <Avatar alt="Remy Sharp" src="/static/images/avatar/2.jpg" />
                </IconButton>
            </Tooltip>
            <Menu
                sx={{ mt: '45px' }}
                id="menu-appbar"
                anchorEl={anchorElUser}
                anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
                }}
                open={Boolean(anchorElUser)}
                onClose={handleCloseUserMenu}
            >
                {settings.map((setting) => (
                <MenuItem key={setting} onClick={setting === "Logout" ? handleLogout : handleCloseUserMenu}>
                    {/*{setting === 'Calendars'?  <Typography textAlign="center" component={Link} to="/calendar" style ={{textDecoration: 'none', color: 'inherit'}}>
                    {setting} </Typography> :<Typography textAlign="center">{setting}</Typography> }*/}
                    {setting === 'Calendars' ? (
                    <Typography textAlign="center" component={Link} to="/homepage" style={{ textDecoration: 'none', color: 'inherit' }}>
                    {setting}
                    </Typography>
                    ) : setting === 'Profile' ? (
                    <Typography textAlign="center" component={Link} to="/profile" style={{ textDecoration: 'none', color: 'inherit' }}>
                    {setting}
                    </Typography>
                    ) : setting === 'Account' ? (
                        <Typography textAlign="center" component={Link} to="/account-settings" style={{ textDecoration: 'none', color: 'inherit' }}>
                        {setting}
                        </Typography>
                        ) : setting === 'Logout' ? (
                            <Typography textAlign="center" component={Link} to="/custom-login" style={{ textDecoration: 'none', color: 'inherit' }}>
                            {setting}
                            </Typography>
                            ) : (
                    <Typography textAlign="center">{setting}</Typography>
                    )}
                </MenuItem>
                ))}
            </Menu>
            </Box>
        </Toolbar>
        </Container>
    </AppBar>
    );
}
export default ResponsiveAppBar;