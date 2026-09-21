import { useMutation, useQueryClient } from '@tanstack/react-query';

import { userService } from '../services/userService';
import type {
  CreateProfessionalValues,
  CreateUserValues,
  UpdateUserValues,
} from '../types';
import { USERS_QUERY_KEY } from './useUsers';

export function useUserMutation() {
  const queryClient = useQueryClient();
  const invalidate = () =>
    queryClient.invalidateQueries({ queryKey: [USERS_QUERY_KEY] });

  const createUser = useMutation({
    mutationFn: (values: CreateUserValues) => userService.createUser(values),
    onSuccess: invalidate,
  });

  const createProfessional = useMutation({
    mutationFn: (values: CreateProfessionalValues) =>
      userService.createProfessional(values),
    onSuccess: invalidate,
  });

  const updateUser = useMutation({
    mutationFn: ({ id, values }: { id: number; values: UpdateUserValues }) =>
      userService.updateUser(id, values),
    onSuccess: invalidate,
  });

  const setActive = useMutation({
    mutationFn: ({ id, active }: { id: number; active: boolean }) =>
      userService.setUserActive(id, active),
    onSuccess: invalidate,
  });

  return { createUser, createProfessional, updateUser, setActive };
}
