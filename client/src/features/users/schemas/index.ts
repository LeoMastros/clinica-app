import { z } from 'zod';

import {
  EMAIL_PATTERN,
  MIN_PASSWORD_LENGTH,
} from '../../../constants/validation';

const name = (label: string) => z.string().trim().min(1, `Informe o ${label}`);

const email = z
  .string()
  .trim()
  .min(1, 'Informe o e-mail')
  .regex(EMAIL_PATTERN, 'Digite um e-mail válido');

const password = z
  .string()
  .min(1, 'Informe a senha inicial')
  .min(
    MIN_PASSWORD_LENGTH,
    `A senha deve ter ao menos ${MIN_PASSWORD_LENGTH} caracteres`
  );

const baseUserFields = {
  firstName: name('nome'),
  lastName: name('sobrenome'),
  email,
  password,
};

export const createSecretarySchema = z.object(baseUserFields);
export type CreateSecretaryFormValues = z.infer<typeof createSecretarySchema>;

export const createProfessionalSchema = z.object({
  ...baseUserFields,
  professionalLevelId: z.number({ error: 'Selecione o nível' }),
  specialtyIds: z
    .array(z.number())
    .min(1, 'Selecione ao menos uma especialidade'),
  crpRegistration: name('CRP'),
  phone: name('telefone'),
  cpf: name('CPF'),
  birthDate: z.string().min(1, 'Informe a data de nascimento'),
});
export type CreateProfessionalFormValues = z.infer<
  typeof createProfessionalSchema
>;

export const updateUserSchema = z.object({
  firstName: name('nome'),
  lastName: name('sobrenome'),
  email,
});
export type UpdateUserFormValues = z.infer<typeof updateUserSchema>;
