import {
  CPF_PATTERN,
  EMAIL_PATTERN,
  MIN_PASSWORD_LENGTH,
} from '../../constants/validation';

export function isValidEmail(value: string): boolean {
  return EMAIL_PATTERN.test(value);
}

export function hasCpfFormat(value: string): boolean {
  return CPF_PATTERN.test(value);
}

export function isStrongEnoughPassword(value: string): boolean {
  return value.length >= MIN_PASSWORD_LENGTH;
}
