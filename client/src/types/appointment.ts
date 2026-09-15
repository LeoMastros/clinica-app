import type { ID } from './common';

export type AppointmentLocation = 'apasem' | 'clinica' | 'remote' | 'phone';

export type RecurrenceFrequency = 'none' | 'weekly' | 'biweekly' | 'monthly';

export interface Recurrence {
  frequency: RecurrenceFrequency;
  interval: number;
  until?: string;
  exceptions?: string[];
}

export interface Appointment {
  id: ID;
  patientId: ID;
  psychologistId: ID;
  start: string;
  end: string;
  location: AppointmentLocation;
  recurrence: Recurrence;
  cancelledAt?: string;
  cancellationReason?: string;
}
