import type { Patient } from '../../types/patient';
import type { RuleResult } from './types';

export function validatePsychologistAssignment(
  _patient: Patient,
  _psychologistId: string
): RuleResult {
  // TODO: A patient can be linked to only one psychologist
  // TODO: A psychologist may hold many patients
  // NO FUNCTIONAL CODE - Implementation guide only
  return { valid: true };
}
