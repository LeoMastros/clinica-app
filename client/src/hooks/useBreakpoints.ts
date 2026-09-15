import { useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';

export function useBreakpoints() {
  const theme = useTheme();
  return {
    isXs: useMediaQuery(theme.breakpoints.only('xs')),
    isSm: useMediaQuery(theme.breakpoints.only('sm')),
    isMdUp: useMediaQuery(theme.breakpoints.up('md')),
  };
}
