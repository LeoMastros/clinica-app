import type { ThemeOptions } from '@mui/material/styles';

export const typography: ThemeOptions['typography'] = {
  fontFamily: ['Roboto', 'system-ui', 'Segoe UI', 'sans-serif'].join(','),
  h1: { fontSize: '2.25rem', fontWeight: 600 },
  h2: { fontSize: '1.75rem', fontWeight: 600 },
  h3: { fontSize: '1.5rem', fontWeight: 600 },
  h4: { fontSize: '1.25rem', fontWeight: 600 },
  h5: { fontSize: '1.125rem', fontWeight: 600 },
  h6: { fontSize: '1rem', fontWeight: 600 },
  button: { textTransform: 'none', fontWeight: 600 },
};
