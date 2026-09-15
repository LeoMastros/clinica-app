export type Role = 'admin' | 'psychologist' | 'secretary';

export type Resource =
  | 'appointment'
  | 'triage'
  | 'anamnesis'
  | 'session'
  | 'report'
  | 'patient'
  | 'user'
  | 'cryptoKey';

export type Action =
  | 'view'
  | 'create'
  | 'edit'
  | 'cancel'
  | 'assign_psychologist'
  | 'edit_status'
  | 'reset_password'
  | 'generate';

export type PermissionMatrix = Record<
  Role,
  Partial<Record<Resource, Action[]>>
>;
