import apiClient from '../../../core/api/client';
import { ENDPOINTS } from '../../../core/api/endpoints';
import type {
  BackendUser,
  CreateProfessionalValues,
  CreateUserValues,
  LookupItem,
  PageResponse,
  UpdateUserValues,
  UserFilters,
} from '../types';

interface ApiEnvelope<T> {
  success: boolean;
  message: string | null;
  data: T;
}

export const userService = {
  async listUsers(
    filters: UserFilters = {}
  ): Promise<PageResponse<BackendUser>> {
    const { data } = await apiClient.get<
      ApiEnvelope<PageResponse<BackendUser>>
    >(ENDPOINTS.users.root, {
      params: {
        page: 0,
        size: 100,
        ...(filters.type ? { type: filters.type } : {}),
        ...(filters.active !== undefined ? { active: filters.active } : {}),
      },
    });
    return data.data;
  },

  async createUser(values: CreateUserValues): Promise<BackendUser> {
    const { data } = await apiClient.post<ApiEnvelope<BackendUser>>(
      ENDPOINTS.users.root,
      values
    );
    return data.data;
  },

  async createProfessional(
    values: CreateProfessionalValues
  ): Promise<BackendUser> {
    const { data } = await apiClient.post<ApiEnvelope<BackendUser>>(
      ENDPOINTS.professionals.root,
      values
    );
    return data.data;
  },

  async updateUser(id: number, values: UpdateUserValues): Promise<BackendUser> {
    const { data } = await apiClient.put<ApiEnvelope<BackendUser>>(
      ENDPOINTS.users.byId(String(id)),
      values
    );
    return data.data;
  },

  async setUserActive(id: number, active: boolean): Promise<BackendUser> {
    const { data } = await apiClient.patch<ApiEnvelope<BackendUser>>(
      ENDPOINTS.users.activation(String(id)),
      { active }
    );
    return data.data;
  },

  async listSpecialties(): Promise<LookupItem[]> {
    const { data } = await apiClient.get<ApiEnvelope<LookupItem[]>>(
      `${ENDPOINTS.lookups.root}/specialties`
    );
    return data.data;
  },

  async listProfessionalLevels(): Promise<LookupItem[]> {
    const { data } = await apiClient.get<ApiEnvelope<LookupItem[]>>(
      `${ENDPOINTS.lookups.root}/professional-levels`
    );
    return data.data;
  },
};
