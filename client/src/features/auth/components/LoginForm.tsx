import { useState } from 'react';
import { Controller, useForm } from 'react-hook-form';

import { zodResolver } from '@hookform/resolvers/zod';
import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import Stack from '@mui/material/Stack';
import { AxiosError } from 'axios';

import { FormField, PasswordField } from '../../../shared/molecules';
import { useLogin } from '../hooks/useLogin';
import { loginSchema } from '../schemas';
import type { LoginFormValues } from '../schemas';

export function LoginForm() {
  const { login, isLoading } = useLogin();
  const [loginError, setLoginError] = useState<string | null>(null);
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '' },
    mode: 'onTouched',
    reValidateMode: 'onChange',
  });

  const onSubmit = async (values: LoginFormValues) => {
    setLoginError(null);
    try {
      await login(values);
    } catch (error) {
      const message =
        error instanceof AxiosError
          ? (error.response?.data?.message ?? 'Não foi possível entrar')
          : 'Não foi possível entrar';
      setLoginError(message);
    }
  };

  return (
    <Stack
      component="form"
      spacing={2}
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      aria-label="Formulário de acesso"
    >
      {loginError ? (
        <Alert severity="error" role="alert">
          {loginError}
        </Alert>
      ) : null}

      <Alert severity="info" variant="outlined">
        <AlertTitle>Ambiente de desenvolvimento</AlertTitle>
        As credenciais do coordenador inicial são definidas via variáveis de
        ambiente (<code>COORDINATOR_EMAIL</code> /{' '}
        <code>COORDINATOR_PASSWORD</code>) — veja o README.
      </Alert>

      <Controller
        name="email"
        control={control}
        render={({ field }) => (
          <FormField
            {...field}
            label="E-mail institucional"
            type="email"
            autoComplete="email"
            autoFocus
            required
            hint="Use o e-mail cadastrado na clínica."
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
            autoComplete="current-password"
            required
            hint="Mínimo de 8 caracteres."
            errorMessage={errors.password?.message}
          />
        )}
      />

      <Button
        type="submit"
        variant="contained"
        size="large"
        disabled={isLoading}
        startIcon={
          isLoading ? <CircularProgress size={18} color="inherit" /> : undefined
        }
      >
        {isLoading ? 'Entrando…' : 'Entrar'}
      </Button>
    </Stack>
  );
}
