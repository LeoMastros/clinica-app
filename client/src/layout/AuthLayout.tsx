import { Outlet } from 'react-router-dom';

import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutlineOutlined';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';

import { APP_TAGLINE } from '../core/utils/constants';
import { BrandMark } from '../shared/atoms';
import { gradients } from '../theme';

const HIGHLIGHTS = [
  'Agenda, triagem e anamnese em um só lugar',
  'Sessões e laudos organizados por paciente',
  'Acesso controlado por perfil profissional',
];

export function AuthLayout() {
  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'grid',
        gridTemplateColumns: { xs: '1fr', md: '1.1fr 1fr' },
        bgcolor: 'background.default',
      }}
    >
      <Box
        component="aside"
        sx={{
          display: { xs: 'none', md: 'flex' },
          alignItems: 'center',
          p: 8,
          color: 'common.white',
          background: gradients.brand,
        }}
      >
        <Stack spacing={4}>
          <BrandMark onDark size="large" subtitle={APP_TAGLINE} />
          <Typography variant="h5" component="p" sx={{ maxWidth: 420 }}>
            O sistema de gestão clínica da Universidade Católica de Santos.
          </Typography>
          <List dense disablePadding>
            {HIGHLIGHTS.map(highlight => (
              <ListItem key={highlight} disableGutters>
                <ListItemIcon sx={{ color: 'common.white', minWidth: 36 }}>
                  <CheckCircleOutlineIcon />
                </ListItemIcon>
                <ListItemText primary={highlight} />
              </ListItem>
            ))}
          </List>
        </Stack>
      </Box>

      <Box sx={{ display: 'flex', alignItems: 'center', py: 6 }}>
        <Container maxWidth="sm">
          <Stack spacing={3}>
            <Box sx={{ display: { md: 'none' } }}>
              <BrandMark subtitle={APP_TAGLINE} />
            </Box>
            <Outlet />
          </Stack>
        </Container>
      </Box>
    </Box>
  );
}
