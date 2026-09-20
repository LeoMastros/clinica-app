import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';

import { USER_TYPE_LABELS } from '../types';
import type { BackendUser } from '../types';

export interface UserDetailModalProps {
  user: BackendUser | null;
  onClose: () => void;
}

/** Read-only details for a user account. */
export function UserDetailModal({ user, onClose }: UserDetailModalProps) {
  return (
    <Dialog open={user != null} onClose={onClose} maxWidth="xs" fullWidth>
      <DialogTitle>Detalhes do usuário</DialogTitle>
      <DialogContent>
        {user && (
          <List dense>
            <ListItem>
              <ListItemText
                primary="Nome"
                secondary={
                  [user.firstName, user.lastName].filter(Boolean).join(' ') ||
                  '—'
                }
              />
            </ListItem>
            <ListItem>
              <ListItemText primary="E-mail" secondary={user.loginEmail} />
            </ListItem>
            <ListItem>
              <ListItemText
                primary="Perfil"
                secondary={USER_TYPE_LABELS[user.userType]}
              />
            </ListItem>
            <ListItem>
              <ListItemText
                primary="Status"
                secondary={user.isActive ? 'Ativo' : 'Inativo'}
              />
            </ListItem>
          </List>
        )}
      </DialogContent>
    </Dialog>
  );
}
