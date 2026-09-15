import type { AppointmentLocation } from './appointment';
import type { ID } from './common';

export type SessionStatus =
  'scheduled' | 'confirmed' | 'completed' | 'cancelled';

export interface Session {
  id: ID;
  patientId: ID;
  psychologistId: ID;
  appointmentId?: ID;
  date: string;
  status: SessionStatus;
  location: AppointmentLocation;
  notes?: string;
  cancellationReason?: string;
}
