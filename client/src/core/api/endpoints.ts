const API_V1 = '/api/v1';

export const ENDPOINTS = {
  auth: {
    login: `${API_V1}/auth/login`,
    logout: `${API_V1}/auth/logout`,
    refresh: `${API_V1}/auth/refresh`,
    me: `${API_V1}/auth/me`,
    forgotPassword: `${API_V1}/auth/forgot-password`,
    resetPassword: `${API_V1}/auth/reset-password`,
  },
  users: {
    root: `${API_V1}/users`,
    byId: (id: string) => `${API_V1}/users/${id}`,
    activation: (id: string) => `${API_V1}/users/${id}/activation`,
    resetPassword: (id: string) => `${API_V1}/users/${id}/reset-password`,
  },
  professionals: {
    root: `${API_V1}/professionals`,
    byId: (id: string) => `${API_V1}/professionals/${id}`,
  },
  lookups: { root: `${API_V1}/lookups` },
  patients: {
    root: `${API_V1}/patients`,
    byId: (id: string) => `${API_V1}/patients/${id}`,
  },
  appointments: {
    root: `${API_V1}/appointments`,
    byId: (id: string) => `${API_V1}/appointments/${id}`,
  },
  sessions: {
    root: `${API_V1}/sessions`,
    byId: (id: string) => `${API_V1}/sessions/${id}`,
  },
  anamnesis: {
    root: `${API_V1}/anamnesis`,
    byId: (id: string) => `${API_V1}/anamnesis/${id}`,
  },
  triage: {
    root: `${API_V1}/triage`,
    byId: (id: string) => `${API_V1}/triage/${id}`,
  },
  reports: {
    root: `${API_V1}/reports`,
    byId: (id: string) => `${API_V1}/reports/${id}`,
  },
} as const;
