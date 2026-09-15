export const ROUTES = {
  login: '/login',
  signup: '/signup',
  appointments: '/agendamentos',
  triage: '/triagem',
  anamnesis: '/anamnese',
  sessions: '/sessoes',
  reports: '/laudos',
  patients: '/pacientes',
  users: '/usuarios',
  forbidden: '/sem-acesso',
} as const;

export type RoutePath = (typeof ROUTES)[keyof typeof ROUTES];
