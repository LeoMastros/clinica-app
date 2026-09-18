export const ENDPOINTS = {
  // Nao existe cadastro publico: quem cria usuario e o administrador, pela
  // tela de gestao de usuarios.
  auth: { login: '/auth/login', me: '/auth/me' },
  users: {
    root: '/users',
    byId: (id: string) => `/users/${id}`,
    resetPassword: (id: string) => `/users/${id}/reset-password`,
  },
  patients: { root: '/patients', byId: (id: string) => `/patients/${id}` },
  appointments: {
    root: '/appointments',
    byId: (id: string) => `/appointments/${id}`,
  },
  sessions: { root: '/sessions', byId: (id: string) => `/sessions/${id}` },
  anamnesis: { root: '/anamnesis', byId: (id: string) => `/anamnesis/${id}` },
  triage: { root: '/triage', byId: (id: string) => `/triage/${id}` },
  reports: { root: '/reports', byId: (id: string) => `/reports/${id}` },
} as const;
