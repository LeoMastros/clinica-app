export type ID = string;

export interface Paginated<T> {
  items: T[];
  page: number;
  pageSize: number;
  total: number;
}

export type LoadingState = 'idle' | 'loading' | 'success' | 'error';
