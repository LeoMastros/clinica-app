/**
 * `VITE_API_URL` guarda apenas a origem do servidor (protocolo, host e porta).
 * O prefixo `/api/v1` vem do `context-path` configurado no Spring e é
 * responsabilidade do client conhecer, para o `.env` da VM não precisar mudar.
 */
const SERVER_ORIGIN = import.meta.env.VITE_API_URL ?? 'http://localhost:8080';

export const API_BASE_URL = `${SERVER_ORIGIN.replace(/\/+$/, '')}/api/v1`;

export const API_TIMEOUT_MS = 15000;

export const AUTH_TOKEN_STORAGE_KEY = 'clinica.auth.token';
