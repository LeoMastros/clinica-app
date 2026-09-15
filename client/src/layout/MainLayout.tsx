import { useState } from 'react';
import { Outlet, useLocation } from 'react-router-dom';

import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import Toolbar from '@mui/material/Toolbar';

import { APP_NAME } from '../core/utils/constants';
import { layoutSizes } from '../theme/spacing';
import { AppBar } from './AppBar';
import { Footer } from './Footer';
import { MobileDrawer } from './MobileDrawer';
import { NAV_ITEMS, Sidebar } from './Sidebar';

export function MainLayout() {
  const [mobileOpen, setMobileOpen] = useState(false);
  const { pathname } = useLocation();
  const title =
    NAV_ITEMS.find(item => pathname.startsWith(item.path))?.label ?? APP_NAME;

  return (
    <Box
      sx={{
        display: 'flex',
        minHeight: '100vh',
        bgcolor: 'background.default',
      }}
    >
      <AppBar title={title} onOpenMenu={() => setMobileOpen(true)} />
      <Drawer
        variant="permanent"
        sx={{
          display: { xs: 'none', md: 'block' },
          width: layoutSizes.sidebarWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: layoutSizes.sidebarWidth,
            boxSizing: 'border-box',
          },
        }}
        open
      >
        <Sidebar />
      </Drawer>
      <MobileDrawer open={mobileOpen} onClose={() => setMobileOpen(false)} />
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          display: 'flex',
          flexDirection: 'column',
          width: { md: `calc(100% - ${layoutSizes.sidebarWidth}px)` },
        }}
      >
        <Toolbar />
        <Box sx={{ flexGrow: 1, p: { xs: 2, md: 3 } }}>
          <Outlet />
        </Box>
        <Footer />
      </Box>
    </Box>
  );
}
