import { ACCOUNTS } from '../fixtures/accounts';

interface ApiEnvelope<T> {
  data: T;
}

export interface ApiUser {
  id: number;
  loginEmail: string;
  userType: 'COORDINATOR' | 'SECRETARY' | 'PROFESSIONAL';
  isActive: boolean;
}

/**
 * Thin backend client for test setup and backend-state assertions.
 * Specs should exercise the UI for the behavior under test and use this only
 * to arrange data (e.g. create an inactive user) or verify side effects that
 * are not fully visible in the UI.
 */
export class ApiHelper {
  private readonly baseUrl = process.env.E2E_API_URL ?? 'http://localhost:8080';
  private token: string | undefined;

  private async adminToken(): Promise<string> {
    if (this.token) return this.token;
    const res = await fetch(`${this.baseUrl}/api/v1/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(ACCOUNTS.coordinator),
    });
    if (!res.ok) {
      throw new Error(`Admin login failed: ${res.status}`);
    }
    this.token = (await res.json()).data.accessToken;
    return this.token!;
  }

  async findUserByEmail(email: string): Promise<ApiUser | undefined> {
    const token = await this.adminToken();
    const res = await fetch(`${this.baseUrl}/api/v1/users?page=0&size=100`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    const body = (await res.json()) as ApiEnvelope<{ content: ApiUser[] }>;
    return body.data.content.find(u => u.loginEmail === email);
  }

  async createUser(input: {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    role: 'SECRETARY' | 'PROFESSIONAL';
  }): Promise<ApiUser> {
    const token = await this.adminToken();
    const res = await fetch(`${this.baseUrl}/api/v1/users`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(input),
    });
    if (!res.ok) {
      throw new Error(`createUser failed: ${res.status} ${await res.text()}`);
    }
    return (await res.json()).data;
  }

  /** Registers a professional (created inactive). Secretary+coordinator. */
  async createProfessional(input: {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    specialtyIds: number[];
    professionalLevelId?: number;
  }): Promise<{ id: number; userId: number }> {
    const token = await this.adminToken();
    const res = await fetch(`${this.baseUrl}/api/v1/professionals`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(input),
    });
    if (!res.ok) {
      throw new Error(
        `createProfessional failed: ${res.status} ${await res.text()}`
      );
    }
    return (await res.json()).data;
  }

  async setUserActive(id: number, active: boolean): Promise<void> {
    const token = await this.adminToken();
    const res = await fetch(`${this.baseUrl}/api/v1/users/${id}/activation`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ active }),
    });
    if (!res.ok) {
      throw new Error(`setUserActive failed: ${res.status}`);
    }
  }
}
