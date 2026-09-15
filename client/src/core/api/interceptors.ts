import type { AxiosInstance } from 'axios';

/**
 * Wires request/response interceptors into the shared axios instance.
 */
export function setupInterceptors(_client: AxiosInstance): void {
  // TODO: Attach request interceptor reading AUTH_TOKEN_STORAGE_KEY and setting the Authorization header
  // TODO: Attach response interceptor mapping axios errors to ApiError (status, message, details)
  // TODO: On 401, clear stored token and redirect to ROUTES.login
  // TODO: Decrypt enveloped payloads on response using core/crypto decryption
  //       (Crypto Rule: everything except IDs and dates is encrypted)
  // NO FUNCTIONAL CODE - Implementation guide only
}
