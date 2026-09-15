import { describe, expect, it } from 'vitest';

import {
  can,
  canAssignPsychologist,
  canEditPatientStatus,
  canGenerateCryptoKeys,
} from './permissions';

describe('permissions', () => {
  it('gives the admin full access to every module in the sidebar', () => {
    for (const resource of [
      'appointment',
      'triage',
      'anamnesis',
      'session',
      'report',
      'patient',
      'user',
    ] as const) {
      expect(can('admin', 'view', resource)).toBe(true);
    }
  });

  it('keeps appointments read-only for psychologists', () => {
    expect(can('psychologist', 'view', 'appointment')).toBe(true);
    expect(can('psychologist', 'create', 'appointment')).toBe(false);
    expect(can('psychologist', 'cancel', 'appointment')).toBe(false);
  });

  it('hides triage, anamnesis and user management from the secretary', () => {
    expect(can('secretary', 'view', 'triage')).toBe(false);
    expect(can('secretary', 'view', 'anamnesis')).toBe(false);
    expect(can('secretary', 'view', 'user')).toBe(false);
  });

  it('lets the secretary edit patient status but not the psychologist assignment', () => {
    expect(canEditPatientStatus('secretary')).toBe(true);
    expect(canAssignPsychologist('secretary')).toBe(false);
    expect(canAssignPsychologist('admin')).toBe(true);
  });

  it('restricts crypto key generation to the admin', () => {
    expect(canGenerateCryptoKeys('admin')).toBe(true);
    expect(canGenerateCryptoKeys('psychologist')).toBe(false);
    expect(canGenerateCryptoKeys('secretary')).toBe(false);
  });

  it('never grants any permission without a role', () => {
    expect(can(undefined, 'view', 'patient')).toBe(false);
  });
});
