import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';

import { APP_NAME } from '../core/utils/constants';

export function Footer() {
  return (
    <Box
      component="footer"
      sx={{ py: 2, px: 3, borderTop: 1, borderColor: 'divider' }}
    >
      <Typography variant="body2" color="text.secondary">
        {APP_NAME} · {new Date().getFullYear()}
      </Typography>
    </Box>
  );
}
