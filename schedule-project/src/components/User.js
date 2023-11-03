import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Container from '@mui/material/Container';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';


export default function User() {
    return (
        <Container maxWidth="md">
            <Paper elevation= {3}>
                <Typography variant="h5" component="div">
                    User Identification
                </Typography>

        <Box
        component="form"
        sx={{
            '& > :not(style)': { m: 1 },
        }}
        noValidate
        autoComplete="off"
        >
            <TextField id="outlined-basic" label="First Name" variant="outlined" />
            <TextField id="outlined-basic" label="Last Name" variant="outlined" />
        </Box>
            </Paper>
        </Container>
    );
}
