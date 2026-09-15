import { Controller, useForm } from 'react-hook-form';

import { zodResolver } from '@hookform/resolvers/zod';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';

import { FormField, PasswordField } from '../../../shared/molecules';
import { signupSchema } from '../schemas';
import type { SignupFormValues } from '../schemas';

export function SignupForm() {
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<SignupFormValues>({
    resolver: zodResolver(signupSchema),
    defaultValues: {
      name: '',
      email: '',
      password: '',
      passwordConfirmation: '',
    },
    mode: 'onTouched',
    reValidateMode: 'onChange',
  });

  // TODO: Submit through authService.signup once the backend contract exists
  return (
    <Stack
      component="form"
      spacing={2}
      onSubmit={handleSubmit(() => undefined)}
      noValidate
      aria-label="Formulário de cadastro"
    >
      <Alert severity="warning" variant="outlined">
        O cadastro de profissionais é feito pela administração. Este formulário
        ainda não envia dados.
      </Alert>

      <Controller
        name="name"
        control={control}
        render={({ field }) => (
          <FormField
            {...field}
            label="Nome completo"
            autoComplete="name"
            required
            errorMessage={errors.name?.message}
          />
        )}
      />

      <Controller
        name="email"
        control={control}
        render={({ field }) => (
          <FormField
            {...field}
            label="E-mail institucional"
            type="email"
            autoComplete="email"
            required
            errorMessage={errors.email?.message}
          />
        )}
      />

      <Controller
        name="password"
        control={control}
        render={({ field }) => (
          <PasswordField
            {...field}
            label="Senha"
            autoComplete="new-password"
            required
            hint="Mínimo de 8 caracteres, com ao menos um número."
            errorMessage={errors.password?.message}
          />
        )}
      />

      <Controller
        name="passwordConfirmation"
        control={control}
        render={({ field }) => (
          <PasswordField
            {...field}
            label="Confirmar senha"
            autoComplete="new-password"
            required
            errorMessage={errors.passwordConfirmation?.message}
          />
        )}
      />

      <Button type="submit" variant="contained" size="large" disabled>
        Criar conta
      </Button>
    </Stack>
  );
}
