import type { ID } from './common';

export type PatientStatus = 'active' | 'inactive' | 'deceased' | 'dropout';

export interface Patient {
  id: ID;
  name: string;
  cpf: string;
  birthDate: string;
  status: PatientStatus;
  psychologistId?: ID;
}
