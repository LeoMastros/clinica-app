import { useState } from 'react';

import AddIcon from '@mui/icons-material/Add';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import { AxiosError } from 'axios';

import { usePermissions } from '../../../hooks/usePermissions';
import { PageLayout } from '../../../shared/templates/PageLayout';
import { UserDetailModal } from '../components/UserDetailModal';
import { UserFilter } from '../components/UserFilter';
import type { CreateUserFormValues } from '../components/UserForm';
import { UserModal } from '../components/UserModal';
import { UserTable } from '../components/UserTable';
import { useUserMutation } from '../hooks/useUserMutation';
import { useUsers } from '../hooks/useUsers';
import type { UpdateUserFormValues } from '../schemas';
import type {
  BackendUser,
  CreatableRole,
  CreateProfessionalValues,
  CreateUserValues,
  UserFilters,
} from '../types';

function extractError(error: unknown, fallback: string): string {
  if (error instanceof AxiosError) {
    return error.response?.data?.message ?? fallback;
  }
  return fallback;
}

export function UserListPage() {
  const { can } = usePermissions();
  const [filters, setFilters] = useState<UserFilters>({});
  const { users, isLoading, isError } = useUsers(filters);
  const { createUser, createProfessional, updateUser, setActive } =
    useUserMutation();

  const [modalOpen, setModalOpen] = useState(false);
  const [editUser, setEditUser] = useState<BackendUser | null>(null);
  const [detailUser, setDetailUser] = useState<BackendUser | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);

  const handleCreate = (kind: CreatableRole, values: CreateUserFormValues) => {
    setSubmitError(null);
    const onSuccess = () => setModalOpen(false);
    const onError = (error: unknown) =>
      setSubmitError(extractError(error, 'Falha ao criar usuário'));

    if (kind === 'PROFESSIONAL') {
      createProfessional.mutate(values as CreateProfessionalValues, {
        onSuccess,
        onError,
      });
    } else {
      createUser.mutate({ ...values, role: 'SECRETARY' } as CreateUserValues, {
        onSuccess,
        onError,
      });
    }
  };

  const handleEdit = (id: number, values: UpdateUserFormValues) => {
    setSubmitError(null);
    updateUser.mutate(
      { id, values },
      {
        onSuccess: () => setEditUser(null),
        onError: error =>
          setSubmitError(extractError(error, 'Falha ao salvar usuário')),
      }
    );
  };

  const isSubmitting =
    createUser.isPending ||
    createProfessional.isPending ||
    updateUser.isPending;

  return (
    <PageLayout
      title="Gestão de Usuários"
      description="Contas de acesso cadastradas na clínica"
      actions={
        can('create', 'user') ? (
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => {
              setSubmitError(null);
              setModalOpen(true);
            }}
          >
            Novo usuário
          </Button>
        ) : undefined
      }
    >
      <UserFilter filters={filters} onChange={setFilters} />

      {isError ? (
        <Alert severity="error">Não foi possível carregar os usuários.</Alert>
      ) : isLoading ? (
        <CircularProgress aria-label="Carregando usuários" />
      ) : (
        <UserTable
          users={users}
          isToggling={setActive.isPending}
          canEdit={can('edit', 'user')}
          onToggleActive={(user, active) =>
            setActive.mutate({ id: user.id, active })
          }
          onView={setDetailUser}
          onEdit={user => {
            setSubmitError(null);
            setEditUser(user);
          }}
        />
      )}

      <UserModal
        open={modalOpen || editUser != null}
        editUser={editUser}
        isSubmitting={isSubmitting}
        errorMessage={submitError}
        onClose={() => {
          setModalOpen(false);
          setEditUser(null);
        }}
        onCreate={handleCreate}
        onEdit={handleEdit}
      />

      <UserDetailModal user={detailUser} onClose={() => setDetailUser(null)} />
    </PageLayout>
  );
}
