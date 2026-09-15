import PsychologyAltIcon from '@mui/icons-material/PsychologyAlt';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';

import { APP_NAME } from '../../core/utils/constants';
import { gradients } from '../../theme';

export interface BrandMarkProps {
  /** Rendered over a dark gradient surface. */
  onDark?: boolean;
  subtitle?: string;
  size?: 'medium' | 'large';
}

export function BrandMark({
  onDark,
  subtitle,
  size = 'medium',
}: BrandMarkProps) {
  const iconSize = size === 'large' ? 56 : 40;

  return (
    <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
      <Box
        aria-hidden
        sx={{
          width: iconSize,
          height: iconSize,
          borderRadius: 2,
          display: 'grid',
          placeItems: 'center',
          color: 'common.white',
          background: onDark ? 'rgba(255, 255, 255, 0.16)' : gradients.brand,
        }}
      >
        <PsychologyAltIcon fontSize={size === 'large' ? 'large' : 'medium'} />
      </Box>
      <Stack spacing={0}>
        <Typography
          variant={size === 'large' ? 'h4' : 'h6'}
          component="span"
          sx={{
            fontWeight: 700,
            letterSpacing: '-0.02em',
            color: onDark ? 'common.white' : 'secondary.main',
          }}
        >
          {APP_NAME}
        </Typography>
        {subtitle && (
          <Typography
            variant="body2"
            sx={{
              color: onDark ? 'rgba(255, 255, 255, 0.82)' : 'text.secondary',
            }}
          >
            {subtitle}
          </Typography>
        )}
      </Stack>
    </Stack>
  );
}
