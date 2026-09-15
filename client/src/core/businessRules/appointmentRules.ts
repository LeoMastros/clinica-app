import type { Appointment } from '../../types/appointment';
import type { RuleResult } from './types';

export function expandRecurrence(
  _appointment: Appointment,
  _rangeStart: Date,
  _rangeEnd: Date
): Date[] {
  // TODO: Expand recurrence (weekly/biweekly/monthly) within the range using date-fns
  // TODO: Skip dates listed in recurrence.exceptions and stop at recurrence.until
  // NO FUNCTIONAL CODE - Implementation guide only
  return [];
}

export function validateCancellation(
  _appointment: Appointment,
  _reason?: string
): RuleResult {
  // TODO: Require a reason for in-person locations (Apasem/Clínica)
  //       (Business Rule: in-person sessions require cancellation reason)
  // TODO: Reject cancellation of appointments already completed
  // NO FUNCTIONAL CODE - Implementation guide only
  return { valid: true };
}
