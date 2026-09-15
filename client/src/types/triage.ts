import type { ID } from './common';

export type TriagePriority = 'low' | 'medium' | 'high';

export interface Triage {
  id: ID;
  patientId: ID;
  psychologistId: ID;
  createdAt: string;
  priority: TriagePriority;
  notes?: string;
}
