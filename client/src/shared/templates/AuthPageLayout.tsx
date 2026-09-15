import type { ReactNode } from 'react';

import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';

import { gradients } from '../../theme';

export interface AuthPageLayoutProps {
  title: string;
  subtitle?: string;
  children: ReactNode;
  footer?: ReactNode;
}

export function AuthPageLayout({
  title,
  subtitle,
  children,
  footer,
}: AuthPageLayoutProps) {
  return (
    <Paper
      elevation={1}
      sx={{ overflow: 'hidden', border: 1, borderColor: 'divider' }}
    >
      <Box sx={{ height: 6, background: gradients.brand }} />
      <Stack spacing={3} sx={{ p: { xs: 3, sm: 4 } }}>
        <Stack spacing={0.5}>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 700 }}>
            {title}
          </Typography>
          {subtitle && (
            <Typography variant="body2" color="text.secondary">
              {subtitle}
            </Typography>
          )}
        </Stack>
        {children}
        {footer}
      </Stack>
    </Paper>
  );
}
