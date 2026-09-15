import { Link as RouterLink } from 'react-router-dom';

import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';

export interface ErrorPageProps {
  title: string;
  message: string;
}

export function ErrorPage({ title, message }: ErrorPageProps) {
  return (
    <Stack spacing={2} sx={{ alignItems: 'flex-start' }}>
      <Alert severity="error" sx={{ width: '100%' }}>
        <AlertTitle>{title}</AlertTitle>
        {message}
      </Alert>
      <Button component={RouterLink} to="/" variant="contained">
        Voltar ao início
      </Button>
    </Stack>
  );
}

export function ForbiddenPage() {
  return (
    <ErrorPage
      title="Sem acesso"
      message="Seu perfil não tem permissão para acessar esta área da clínica."
    />
  );
}
