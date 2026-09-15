export interface RuleViolation {
  rule: string;
  message: string;
}

export type RuleResult =
  { valid: true } | { valid: false; violations: RuleViolation[] };
