import { useState } from 'react';
import { Controller, useForm } from 'react-hook-form';

import { zodResolver } from '@hookform/resolvers/zod';
import Alert from '@mui/material/Alert';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Stack from '@mui/material/Stack';

import { FormField } from '../../../shared/molecules';
import { updateUserSchema } from '../schemas';
import type { UpdateUserFormValues } from '../schemas';
import type { BackendUser, CreatableRole } from '../types';
import { RoleSelector } from './RoleSelector';
import { UserForm } from './UserForm';
import type { CreateUserFormValues } from './UserForm';

export interface UserModalProps {
  open: boolean;
  /** When set, the modal edits this user instead of creating one. */
  editUser?: BackendUser | null;
  isSubmitting: boolean;
  errorMessage?: string | null;
  onClose: () => void;
  onCreate: (kind: CreatableRole, values: CreateUserFormValues) => void;
  onEdit: (id: number, values: UpdateUserFormValues) => void;
}

export function UserModal({
  open,
  editUser,
  isSubmitting,
  errorMessage,
  onClose,
  onCreate,
  onEdit,
}: UserModalProps) {
  const [kind, setKind] = useState<CreatableRole | null>(null);
  const [step, setStep] = useState<'kind' | 'fields'>('kind');
  const isEdit = editUser != null;

  // Reset wizard state whenever the dialog opens — render-time state
  // adjustment (react.dev: "you might not need an effect").
  const [prevOpen, setPrevOpen] = useState(open);
  if (open !== prevOpen) {
    setPrevOpen(open);
    if (open) {
      setKind(null);
      setStep('kind');
    }
  }

  return (
    <Dialog
      open={open}
      onClose={isSubmitting ? undefined : onClose}
      maxWidth="sm"
      fullWidth
      aria-label={isEdit ? 'Editar usuário' : 'Novo usuário'}
    >
      <DialogTitle>
        {isEdit
          ? 'Editar usuário'
          : step === 'kind'
            ? 'Novo usuário — tipo de conta'
            : `Novo usuário — ${kind === 'PROFESSIONAL' ? 'Psicólogo(a)' : 'Secretaria'}`}
      </DialogTitle>
      <DialogContent>
        {errorMessage ? (
          <Alert severity="error" sx={{ mb: 1 }} role="alert">
            {errorMessage}
          </Alert>
        ) : null}

        {isEdit ? (
          <EditUserForm
            user={editUser}
            isSubmitting={isSubmitting}
            onSubmit={values => onEdit(editUser.id, values)}
          />
        ) : step === 'kind' ? (
          <RoleSelector value={kind} onChange={setKind} />
        ) : (
          kind && (
            <UserForm
              kind={kind}
              isSubmitting={isSubmitting}
              onSubmit={values => onCreate(kind, values)}
            />
          )
        )}
      </DialogContent>
      <DialogActions sx={{ px: 3, pb: 2 }}>
        {step === 'fields' && !isEdit ? (
          <Button onClick={() => setStep('kind')} disabled={isSubmitting}>
            Voltar
          </Button>
        ) : null}
        <Button onClick={onClose} disabled={isSubmitting}>
          Cancelar
        </Button>
        {isEdit ? (
          <Button
            type="submit"
            form="edit-user-form"
            variant="contained"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Salvando…' : 'Salvar'}
          </Button>
        ) : step === 'kind' ? (
          <Button
            variant="contained"
            disabled={!kind}
            onClick={() => setStep('fields')}
          >
            Avançar
          </Button>
        ) : (
          <Button
            type="submit"
            form="create-user-form"
            variant="contained"
            disabled={isSubmitting}
            startIcon={
              isSubmitting ? (
                <CircularProgress size={18} color="inherit" />
              ) : undefined
            }
          >
            {isSubmitting ? 'Criando…' : 'Criar usuário'}
          </Button>
        )}
      </DialogActions>
    </Dialog>
  );
}

function EditUserForm({
  user,
  isSubmitting,
  onSubmit,
}: {
  user: BackendUser;
  isSubmitting: boolean;
  onSubmit: (values: UpdateUserFormValues) => void;
}) {
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<UpdateUserFormValues>({
    resolver: zodResolver(updateUserSchema),
    defaultValues: {
      firstName: user.firstName ?? '',
      lastName: user.lastName ?? '',
      email: user.loginEmail,
    },
  });

  return (
    <Stack
      component="form"
      id="edit-user-form"
      spacing={2}
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      sx={{ pt: 1 }}
    >
      <Stack direction="row" spacing={2}>
        <Controller
          name="firstName"
          control={control}
          render={({ field }) => (
            <FormField
              {...field}
              label="Nome"
              required
              fullWidth
              disabled={isSubmitting}
              errorMessage={errors.firstName?.message}
            />
          )}
        />
        <Controller
          name="lastName"
          control={control}
          render={({ field }) => (
            <FormField
              {...field}
              label="Sobrenome"
              required
              fullWidth
              disabled={isSubmitting}
              errorMessage={errors.lastName?.message}
            />
          )}
        />
      </Stack>
      <Controller
        name="email"
        control={control}
        render={({ field }) => (
          <FormField
            {...field}
            label="E-mail institucional"
            type="email"
            required
            disabled={isSubmitting}
            errorMessage={errors.email?.message}
          />
        )}
      />
    </Stack>
  );
}
