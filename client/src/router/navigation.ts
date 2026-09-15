import { ROUTES } from '../constants/routes';
import { can } from '../core/permissions';
import { NAV_ITEMS } from '../layout/Sidebar';
import type { Role } from '../types/permissions';

export function firstAllowedRoute(role: Role | undefined): string {
  const item = NAV_ITEMS.find(navItem => can(role, 'view', navItem.resource));
  return item?.path ?? ROUTES.forbidden;
}
