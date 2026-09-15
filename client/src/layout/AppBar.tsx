import LogoutIcon from '@mui/icons-material/Logout';
import MenuIcon from '@mui/icons-material/Menu';
import MuiAppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import IconButton from '@mui/material/IconButton';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';

import { ROLE_LABELS } from '../constants/roles';
import { useAuth } from '../core/auth';
import { layoutSizes } from '../theme/spacing';

export interface AppBarProps {
  onOpenMenu: () => void;
  title: string;
}

export function AppBar({ onOpenMenu, title }: AppBarProps) {
  const { user, logout } = useAuth();

  return (
    <MuiAppBar
      position="fixed"
      color="inherit"
      elevation={0}
      sx={{
        borderBottom: 1,
        borderColor: 'divider',
        width: { md: `calc(100% - ${layoutSizes.sidebarWidth}px)` },
        ml: { md: `${layoutSizes.sidebarWidth}px` },
      }}
    >
      <Toolbar>
        <IconButton
          edge="start"
          aria-label="Abrir menu"
          onClick={onOpenMenu}
          sx={{ mr: 2, display: { md: 'none' } }}
        >
          <MenuIcon />
        </IconButton>
        <Typography variant="h6" component="h1" noWrap sx={{ flexGrow: 1 }}>
          {title}
        </Typography>
        {user && (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <Chip
              label={`${user.name} · ${ROLE_LABELS[user.role]}`}
              size="small"
              sx={{ display: { xs: 'none', sm: 'flex' } }}
            />
            <Button onClick={logout} startIcon={<LogoutIcon />} size="small">
              Sair
            </Button>
          </Box>
        )}
      </Toolbar>
    </MuiAppBar>
  );
}
