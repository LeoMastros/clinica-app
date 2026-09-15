import { createTheme } from '@mui/material/styles';

import { breakpoints } from './breakpoints';
import { colors } from './colors';
import { components } from './components';
import { spacingUnit } from './spacing';
import { typography } from './typography';

export const theme = createTheme({
  palette: { mode: 'light', ...colors },
  typography,
  breakpoints,
  spacing: spacingUnit,
  shape: { borderRadius: 8 },
  components,
});

export { colors } from './colors';
export { layoutSizes } from './spacing';
export default theme;
