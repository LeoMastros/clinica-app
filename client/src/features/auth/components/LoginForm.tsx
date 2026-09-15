import { useForm } from 'react-hook-form';

import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';

import { useAuth } from '../../../core/auth';
import { isValidEmail } from '../../../core/utils/validators';

interface LoginFormValues {
  email: string;
  password: string;
}

export function LoginForm() {
  const { login, isLoading } = useAuth();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormValues>({ defaultValues: { email: '', password: '' } });

  return (
    <Stack
      component="form"
      spacing={2}
      onSubmit={handleSubmit(values => login(values))}
      noValidate
    >
      <Alert severity="info">
        Autenticação simulada: use admin@, psicologo@ ou secretaria@ para entrar
        com cada perfil.
      </Alert>
      <TextField
        label="E-mail"
        type="email"
        autoComplete="email"
        error={Boolean(errors.email)}
        helperText={errors.email?.message}
        {...register('email', {
          required: 'Informe o e-mail',
          validate: value => isValidEmail(value) || 'E-mail inválido',
        })}
      />
      <TextField
        label="Senha"
        type="password"
        autoComplete="current-password"
        error={Boolean(errors.password)}
        helperText={errors.password?.message}
        {...register('password', { required: 'Informe a senha' })}
      />
      <Button
        type="submit"
        variant="contained"
        size="large"
        disabled={isLoading}
      >
        Entrar
      </Button>
    </Stack>
  );
}
