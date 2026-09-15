import type { ID } from './common';

export type ReportStatus = 'draft' | 'issued';

export interface Report {
  id: ID;
  patientId: ID;
  psychologistId: ID;
  issuedAt?: string;
  status: ReportStatus;
  title: string;
}
