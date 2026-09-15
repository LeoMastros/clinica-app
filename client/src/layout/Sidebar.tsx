import { NavLink, useLocation } from 'react-router-dom';

import type { SvgIconComponent } from '@mui/icons-material';
import AssignmentIndIcon from '@mui/icons-material/AssignmentInd';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import DescriptionIcon from '@mui/icons-material/Description';
import EventNoteIcon from '@mui/icons-material/EventNote';
import GroupIcon from '@mui/icons-material/Group';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import PsychologyIcon from '@mui/icons-material/Psychology';
import Box from '@mui/material/Box';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';

import { ROUTES } from '../constants/routes';
import { APP_NAME } from '../core/utils/constants';
import { usePermissions } from '../hooks/usePermissions';
import { gradients } from '../theme';
import type { Resource } from '../types/permissions';

export interface NavItem {
  label: string;
  path: string;
  icon: SvgIconComponent;
  resource: Resource;
}

/** Sidebar Navigation Items as defined in CLINIC_APP_STRUCTURE_PLAN.md. */
export const NAV_ITEMS: NavItem[] = [
  {
    label: 'Agendamentos',
    path: ROUTES.appointments,
    icon: CalendarMonthIcon,
    resource: 'appointment',
  },
  {
    label: 'Triagem',
    path: ROUTES.triage,
    icon: AssignmentIndIcon,
    resource: 'triage',
  },
  {
    label: 'Anamnese',
    path: ROUTES.anamnesis,
    icon: PsychologyIcon,
    resource: 'anamnesis',
  },
  {
    label: 'Sessões',
    path: ROUTES.sessions,
    icon: EventNoteIcon,
    resource: 'session',
  },
  {
    label: 'Laudos',
    path: ROUTES.reports,
    icon: DescriptionIcon,
    resource: 'report',
  },
  {
    label: 'Pacientes',
    path: ROUTES.patients,
    icon: GroupIcon,
    resource: 'patient',
  },
  {
    label: 'Gestão de Usuários',
    path: ROUTES.users,
    icon: ManageAccountsIcon,
    resource: 'user',
  },
];

export function Sidebar({ onNavigate }: { onNavigate?: () => void }) {
  const { can } = usePermissions();
  const { pathname } = useLocation();
  const items = NAV_ITEMS.filter(item => can('view', item.resource));

  return (
    <Box
      component="nav"
      aria-label="Navegação principal"
      sx={{
        height: '100%',
        background: gradients.sidebar,
        color: 'common.white',
      }}
    >
      <Toolbar sx={{ px: 2 }}>
        <Typography variant="h6" noWrap sx={{ fontWeight: 700 }}>
          {APP_NAME}
        </Typography>
      </Toolbar>
      <List sx={{ px: 1 }}>
        {items.map(({ label, path, icon: Icon }) => (
          <ListItemButton
            key={path}
            component={NavLink}
            to={path}
            selected={pathname.startsWith(path)}
            onClick={onNavigate}
            sx={{
              mb: 0.5,
              color: 'rgba(255, 255, 255, 0.86)',
              '&:hover': { bgcolor: 'rgba(255, 255, 255, 0.10)' },
              '&.Mui-selected': {
                bgcolor: 'rgba(255, 255, 255, 0.18)',
                color: 'common.white',
                '&:hover': { bgcolor: 'rgba(255, 255, 255, 0.24)' },
              },
            }}
          >
            <ListItemIcon sx={{ minWidth: 40, color: 'inherit' }}>
              <Icon fontSize="small" />
            </ListItemIcon>
            <ListItemText primary={label} />
          </ListItemButton>
        ))}
      </List>
    </Box>
  );
}
