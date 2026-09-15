import type { Session } from '../../types/session';
import type { RuleResult } from './types';

export function canCancelSession(
  _session: Session,
  _currentUserId: string
): RuleResult {
  // TODO: Only the owning psychologist can cancel (see Role-Based Permission Matrix)
  // TODO: Past/completed sessions are immutable
  // NO FUNCTIONAL CODE - Implementation guide only
  return { valid: true };
}

export function isLinkedToAppointment(_session: Session): boolean {
  // TODO: Hybrid rule - sessions may exist without an appointment (walk-in)
  // NO FUNCTIONAL CODE - Implementation guide only
  return false;
}
