import AddIcon from '@mui/icons-material/Add';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';

import { usePermissions } from '../../../hooks/usePermissions';
import { PageLayout } from '../../../shared/templates/PageLayout';

export function UserListPage() {
  // TODO: Admin only: create/edit professionals and reset passwords, never delete
  // TODO: RoleSelector must prevent creating a second admin
  // TODO: Default registration end date depends on professional kind (intern vs recent graduate)
  // NO FUNCTIONAL CODE beyond the layout - Implementation guide only
  const { can } = usePermissions();

  return (
    <PageLayout
      title="Gestão de Usuários"
      description="Profissionais cadastrados na clínica"
      actions={
        can('create', 'user') ? (
          <Button variant="contained" startIcon={<AddIcon />} disabled>
            Novo usuário
          </Button>
        ) : undefined
      }
    >
      <Alert severity="info">
        Módulo em construção: estrutura e permissões prontas, regras de negócio
        pendentes.
      </Alert>
    </PageLayout>
  );
}
