export type {
  Action,
  PermissionMatrix,
  Resource,
  Role,
} from '../../types/permissions';

export interface PermissionCheck {
  role: import('../../types/permissions').Role;
  action: import('../../types/permissions').Action;
  resource: import('../../types/permissions').Resource;
}
