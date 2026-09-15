import type { ThemeOptions } from '@mui/material/styles';

import { accessibility } from './accessibility';
import { elevation } from './shadows';

export const components: ThemeOptions['components'] = {
  MuiCssBaseline: {
    styleOverrides: {
      ':focus-visible': {
        outline: accessibility.focusRing,
        outlineOffset: accessibility.focusRingOffset,
      },
    },
  },
  MuiButton: {
    defaultProps: { disableElevation: true },
    styleOverrides: {
      root: { borderRadius: 8, minHeight: accessibility.minTouchTarget },
    },
  },
  MuiPaper: {
    styleOverrides: {
      rounded: { borderRadius: 12 },
      elevation1: { boxShadow: elevation.card },
    },
  },
  MuiListItemButton: {
    styleOverrides: {
      root: { borderRadius: 8, minHeight: accessibility.minTouchTarget },
    },
  },
  MuiDialog: {
    styleOverrides: {
      paper: { borderRadius: 12, boxShadow: elevation.dialog },
    },
  },
};
