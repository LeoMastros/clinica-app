import EditIcon from '@mui/icons-material/Edit';
import VisibilityIcon from '@mui/icons-material/Visibility';
import Chip from '@mui/material/Chip';
import IconButton from '@mui/material/IconButton';
import Paper from '@mui/material/Paper';
import Switch from '@mui/material/Switch';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Tooltip from '@mui/material/Tooltip';
import Typography from '@mui/material/Typography';

import { USER_TYPE_LABELS } from '../types';
import type { BackendUser } from '../types';

export interface UserTableProps {
  users: BackendUser[];
  isToggling: boolean;
  canEdit: boolean;
  onToggleActive: (user: BackendUser, active: boolean) => void;
  onView: (user: BackendUser) => void;
  onEdit: (user: BackendUser) => void;
}

export function UserTable({
  users,
  isToggling,
  canEdit,
  onToggleActive,
  onView,
  onEdit,
}: UserTableProps) {
  if (users.length === 0) {
    return (
      <Typography color="text.secondary">
        Nenhum usuário encontrado para os filtros aplicados.
      </Typography>
    );
  }

  return (
    <TableContainer component={Paper} variant="outlined">
      <Table aria-label="Usuários cadastrados">
        <TableHead>
          <TableRow>
            <TableCell>Nome</TableCell>
            <TableCell>E-mail</TableCell>
            <TableCell>Perfil</TableCell>
            <TableCell>Status</TableCell>
            <TableCell align="center">Ativo</TableCell>
            <TableCell align="right">Ações</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {users.map(user => {
            const isCoordinator = user.userType === 'COORDINATOR';
            const fullName =
              [user.firstName, user.lastName].filter(Boolean).join(' ') || '—';
            return (
              <TableRow key={user.id} hover>
                <TableCell>{fullName}</TableCell>
                <TableCell>{user.loginEmail}</TableCell>
                <TableCell>
                  <Chip
                    label={USER_TYPE_LABELS[user.userType]}
                    size="small"
                    variant="outlined"
                  />
                </TableCell>
                <TableCell>
                  <Chip
                    label={user.isActive ? 'Ativo' : 'Inativo'}
                    size="small"
                    color={user.isActive ? 'success' : 'default'}
                  />
                </TableCell>
                <TableCell align="center">
                  <Switch
                    checked={user.isActive}
                    disabled={isCoordinator || isToggling}
                    onChange={event =>
                      onToggleActive(user, event.target.checked)
                    }
                    slotProps={{
                      input: {
                        'aria-label': `${user.isActive ? 'Desativar' : 'Ativar'} ${user.loginEmail}`,
                      },
                    }}
                  />
                </TableCell>
                <TableCell align="right">
                  <Tooltip title="Ver detalhes">
                    <IconButton
                      size="small"
                      aria-label={`Ver detalhes de ${user.loginEmail}`}
                      onClick={() => onView(user)}
                    >
                      <VisibilityIcon fontSize="small" />
                    </IconButton>
                  </Tooltip>
                  {canEdit && (
                    <Tooltip title="Editar usuário">
                      <IconButton
                        size="small"
                        aria-label={`Editar ${user.loginEmail}`}
                        onClick={() => onEdit(user)}
                      >
                        <EditIcon fontSize="small" />
                      </IconButton>
                    </Tooltip>
                  )}
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
