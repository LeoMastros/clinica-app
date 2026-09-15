import Drawer from '@mui/material/Drawer';

import { layoutSizes } from '../theme/spacing';
import { Sidebar } from './Sidebar';

export interface MobileDrawerProps {
  open: boolean;
  onClose: () => void;
}

export function MobileDrawer({ open, onClose }: MobileDrawerProps) {
  return (
    <Drawer
      variant="temporary"
      open={open}
      onClose={onClose}
      ModalProps={{ keepMounted: true }}
      sx={{
        display: { xs: 'block', md: 'none' },
        '& .MuiDrawer-paper': {
          width: layoutSizes.sidebarWidth,
          boxSizing: 'border-box',
        },
      }}
    >
      <Sidebar onNavigate={onClose} />
    </Drawer>
  );
}
