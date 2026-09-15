export const unisantos = {
  navy: '#022855',
  blue: '#0067D6',
  white: '#FFFFFF',
  gray: '#404040',
} as const;

export const colors = {
  primary: {
    main: unisantos.blue,
    light: '#4D93E4',
    dark: unisantos.navy,
    contrastText: unisantos.white,
  },
  secondary: {
    main: unisantos.navy,
    light: '#0A3D7A',
    dark: '#011730',
    contrastText: unisantos.white,
  },
  error: { main: '#C62828' },
  warning: { main: '#ED6C02' },
  info: { main: unisantos.blue },
  success: { main: '#2E7D32' },
  background: { default: '#F4F7FB', paper: unisantos.white },
  text: { primary: unisantos.navy, secondary: unisantos.gray },
  divider: 'rgba(2, 40, 85, 0.12)',
} as const;

export const gradients = {
  brand: `linear-gradient(135deg, ${unisantos.navy} 0%, ${unisantos.blue} 100%)`,
  brandSoft: `linear-gradient(135deg, rgba(2, 40, 85, 0.06) 0%, rgba(0, 103, 214, 0.12) 100%)`,
  sidebar: `linear-gradient(180deg, ${unisantos.navy} 0%, #013A7C 100%)`,
  appBar: `linear-gradient(90deg, ${unisantos.white} 0%, #EFF5FD 100%)`,
} as const;
