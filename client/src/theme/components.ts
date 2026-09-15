import type { ThemeOptions } from '@mui/material/styles';

import { accessibility } from './accessibility';
import { gradients, unisantos } from './colors';
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
      root: {
        borderRadius: 8,
        minHeight: accessibility.minTouchTarget,
        textTransform: 'none',
        fontWeight: 600,
      },
      contained: {
        background: gradients.brand,
        '&:hover': { background: gradients.brand, filter: 'brightness(1.08)' },
        '&.Mui-disabled': { background: 'rgba(2, 40, 85, 0.18)' },
      },
    },
  },
  MuiTextField: {
    defaultProps: { variant: 'outlined', fullWidth: true },
  },
  MuiFormHelperText: {
    styleOverrides: {
      root: { marginLeft: 0, minHeight: 20 },
    },
  },
  MuiInputLabel: {
    styleOverrides: {
      asterisk: { color: unisantos.gray },
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
