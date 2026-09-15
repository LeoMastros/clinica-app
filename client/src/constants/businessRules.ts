export const LOCATIONS = [
  { value: 'apasem', label: 'Apasem', requiresCancellationReason: true },
  { value: 'clinica', label: 'Clínica', requiresCancellationReason: true },
  { value: 'remote', label: 'Remoto', requiresCancellationReason: false },
  { value: 'phone', label: 'Telefone', requiresCancellationReason: false },
] as const;

export const PATIENT_STATUS_LABELS = {
  active: 'Ativo',
  inactive: 'Inativo',
  deceased: 'Falecido',
  dropout: 'Desistente',
} as const;

export const SESSION_STATUS_LABELS = {
  scheduled: 'Agendada',
  confirmed: 'Confirmada',
  completed: 'Realizada',
  cancelled: 'Cancelada',
} as const;
