export const ROUTES = {
  login: '/login',
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
