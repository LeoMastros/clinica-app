import { useQuery } from '@tanstack/react-query';

import { userService } from '../services/userService';
import type { UserFilters } from '../types';

export const USERS_QUERY_KEY = 'users';
export const LOOKUPS_QUERY_KEY = 'lookups';

export function useUsers(filters: UserFilters = {}) {
  const query = useQuery({
    queryKey: [USERS_QUERY_KEY, filters],
    queryFn: () => userService.listUsers(filters),
  });
  return { ...query, users: query.data?.content ?? [] };
}

export function useSpecialties() {
  const query = useQuery({
    queryKey: [LOOKUPS_QUERY_KEY, 'specialties'],
    queryFn: () => userService.listSpecialties(),
    staleTime: Infinity,
  });
  return { ...query, specialties: query.data ?? [] };
}

export function useProfessionalLevels() {
  const query = useQuery({
    queryKey: [LOOKUPS_QUERY_KEY, 'professional-levels'],
    queryFn: () => userService.listProfessionalLevels(),
    staleTime: Infinity,
  });
  return { ...query, levels: query.data ?? [] };
}
