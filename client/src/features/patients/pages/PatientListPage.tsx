import AddIcon from '@mui/icons-material/Add';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';

import { usePermissions } from '../../../hooks/usePermissions';
import { PageLayout } from '../../../shared/templates/PageLayout';

export function PatientListPage() {
  // TODO: Psychologists only see their assigned patients and cannot edit them
  // TODO: Secretary can edit patient status but not the psychologist assignment
  // NO FUNCTIONAL CODE beyond the layout - Implementation guide only
  const { can } = usePermissions();

  return (
    <PageLayout
      title="Pacientes"
      description="Cadastro e acompanhamento de pacientes"
      actions={
        can('create', 'patient') ? (
          <Button variant="contained" startIcon={<AddIcon />} disabled>
            Novo paciente
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
