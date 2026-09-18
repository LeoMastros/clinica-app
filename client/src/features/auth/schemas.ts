import { z } from 'zod';

import { EMAIL_PATTERN, MIN_PASSWORD_LENGTH } from '../../constants/validation';

const email = z
  .string()
  .trim()
  .min(1, 'Informe o e-mail')
  .regex(EMAIL_PATTERN, 'Digite um e-mail válido, como nome@unisantos.br');

const password = z
  .string()
  .min(1, 'Informe a senha')
  .min(
    MIN_PASSWORD_LENGTH,
    `A senha deve ter ao menos ${MIN_PASSWORD_LENGTH} caracteres`
  );

export const loginSchema = z.object({ email, password });

export type LoginFormValues = z.infer<typeof loginSchema>;
