import type { ProfessionalKind } from '../../types/user';

export function defaultRegistrationEndDate(
  _kind: ProfessionalKind,
  _reference: Date
): Date {
  // TODO: student_intern -> last day of June or November (end of semester), via date-fns
  // TODO: recent_graduate -> last day of the current year (1 year validity)
  // NO FUNCTIONAL CODE - Implementation guide only
  throw new Error('not implemented');
}
