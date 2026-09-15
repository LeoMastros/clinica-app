import type { ID } from './common';

export interface Anamnesis {
  id: ID;
  patientId: ID;
  psychologistId: ID;
  createdAt: string;
  content?: string;
}
