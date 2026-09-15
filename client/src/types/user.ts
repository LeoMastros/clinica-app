import type { ID } from './common';
import type { Role } from './permissions';

export type ProfessionalKind = 'student_intern' | 'recent_graduate' | 'staff';

export interface User {
  id: ID;
  name: string;
  email: string;
  role: Role;
  professionalKind?: ProfessionalKind;
  registrationEndDate?: string;
  active: boolean;
}
