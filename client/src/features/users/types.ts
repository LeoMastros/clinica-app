import type { Role } from '../../types/permissions';

/** Matches the backend UserDto (GET /api/v1/users). */
export interface BackendUser {
  id: number;
  firstName: string | null;
  lastName: string | null;
  loginEmail: string;
  userType: 'COORDINATOR' | 'SECRETARY' | 'PROFESSIONAL';
  isActive: boolean;
}

export type UserTypeName = BackendUser['userType'];
export type CreatableRole = 'SECRETARY' | 'PROFESSIONAL';

export interface CreateUserValues {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: CreatableRole;
}

export interface CreateProfessionalValues {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  professionalLevelId?: number;
  specialtyIds: number[];
  cpf?: string;
  phone?: string;
  birthDate?: string;
  crpRegistration?: string;
}

export interface UpdateUserValues {
  firstName: string;
  lastName: string;
  email: string;
}

export interface UserFilters {
  type?: UserTypeName;
  active?: boolean;
}

export interface LookupItem {
  id: number;
  name: string;
}

export const USER_TYPE_TO_ROLE: Record<UserTypeName, Role> = {
  COORDINATOR: 'admin',
  SECRETARY: 'secretary',
  PROFESSIONAL: 'psychologist',
};

export const USER_TYPE_LABELS: Record<UserTypeName, string> = {
  COORDINATOR: 'Coordenador',
  SECRETARY: 'Secretaria',
  PROFESSIONAL: 'Psicólogo(a)',
};

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
