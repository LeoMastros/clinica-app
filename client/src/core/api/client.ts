import axios from 'axios';

import { API_BASE_URL, API_TIMEOUT_MS } from '../../constants/api';
import { setupInterceptors } from './interceptors';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: API_TIMEOUT_MS,
  headers: { 'Content-Type': 'application/json' },
});

// Instalado aqui, junto da criação, para não existir um caminho em que alguém
// importe o cliente e faça uma chamada sem o token anexado.
setupInterceptors(apiClient);
