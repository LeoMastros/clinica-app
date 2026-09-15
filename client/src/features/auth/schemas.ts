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

export const signupSchema = z
  .object({
    name: z
      .string()
      .trim()
      .min(3, 'Informe o nome completo com ao menos 3 caracteres'),
    email,
    password: password.regex(/[0-9]/, 'A senha deve conter ao menos um número'),
    passwordConfirmation: z.string().min(1, 'Confirme a senha'),
  })
  .refine(values => values.password === values.passwordConfirmation, {
    path: ['passwordConfirmation'],
    message: 'As senhas não conferem',
  });

export type SignupFormValues = z.infer<typeof signupSchema>;
