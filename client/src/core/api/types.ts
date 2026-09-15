export type { ApiError, ApiResponse } from '../../types/api';

export interface RequestOptions {
  signal?: AbortSignal;
  params?: Record<string, string | number | boolean | undefined>;
}
