import { Controller, useForm } from 'react-hook-form';

import { zodResolver } from '@hookform/resolvers/zod';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormGroup from '@mui/material/FormGroup';
import FormHelperText from '@mui/material/FormHelperText';
import FormLabel from '@mui/material/FormLabel';
import MenuItem from '@mui/material/MenuItem';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';

import { FormField, PasswordField } from '../../../shared/molecules';
import { useProfessionalLevels, useSpecialties } from '../hooks/useUsers';
import { createProfessionalSchema, createSecretarySchema } from '../schemas';
import type {
  CreateProfessionalFormValues,
  CreateSecretaryFormValues,
} from '../schemas';
import type { CreatableRole } from '../types';

export type CreateUserFormValues =
  CreateSecretaryFormValues | CreateProfessionalFormValues;

export interface UserFormProps {
  kind: CreatableRole;
  onSubmit: (values: CreateUserFormValues) => void;
  isSubmitting?: boolean;
}

export function UserForm({ kind, onSubmit, isSubmitting }: UserFormProps) {
  const isProfessional = kind === 'PROFESSIONAL';
  const { specialties } = useSpecialties();
  const { levels } = useProfessionalLevels();

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateUserFormValues>({
    resolver: zodResolver(
      isProfessional ? createProfessionalSchema : createSecretarySchema
    ),
    defaultValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      ...(isProfessional
        ? {
            professionalLevelId: undefined,
            specialtyIds: [],
            crpRegistration: '',
            phone: '',
            cpf: '',
            birthDate: '',
          }
        : {}),
    } as CreateUserFormValues,
    mode: 'onTouched',
    reValidateMode: 'onChange',
  });

  const professionalErrors = errors as Partial<
    Record<keyof CreateProfessionalFormValues, { message?: string }>
  >;

  return (
    <Stack
      component="form"
      id="create-user-form"
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
            autoComplete="email"
            required
            disabled={isSubmitting}
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
            label="Senha inicial"
            autoComplete="new-password"
            required
            disabled={isSubmitting}
            hint="O usuário pode trocá-la após o primeiro acesso."
            errorMessage={errors.password?.message}
          />
        )}
      />

      {isProfessional && (
        <>
          <Controller
            name="professionalLevelId"
            control={control}
            render={({ field }) => (
              <TextField
                select
                label="Nível profissional"
                required
                fullWidth
                value={field.value ?? ''}
                onChange={event => field.onChange(Number(event.target.value))}
                onBlur={field.onBlur}
                error={Boolean(professionalErrors.professionalLevelId)}
                helperText={
                  professionalErrors.professionalLevelId?.message ?? ' '
                }
              >
                {levels.map(level => (
                  <MenuItem key={level.id} value={level.id}>
                    {level.name}
                  </MenuItem>
                ))}
              </TextField>
            )}
          />

          <Controller
            name="specialtyIds"
            control={control}
            render={({ field }) => (
              <Stack>
                <FormLabel required>Especialidades</FormLabel>
                <FormGroup aria-label="Especialidades">
                  {specialties.map(specialty => (
                    <FormControlLabel
                      key={specialty.id}
                      control={
                        <Checkbox
                          checked={field.value?.includes(specialty.id) ?? false}
                          onChange={event => {
                            const current = field.value ?? [];
                            field.onChange(
                              event.target.checked
                                ? [...current, specialty.id]
                                : current.filter(id => id !== specialty.id)
                            );
                          }}
                        />
                      }
                      label={specialty.name}
                    />
                  ))}
                </FormGroup>
                <FormHelperText
                  error={Boolean(professionalErrors.specialtyIds)}
                >
                  {professionalErrors.specialtyIds?.message ?? ' '}
                </FormHelperText>
              </Stack>
            )}
          />

          <Controller
            name="crpRegistration"
            control={control}
            render={({ field }) => (
              <FormField
                {...field}
                label="CRP"
                required
                disabled={isSubmitting}
                errorMessage={professionalErrors.crpRegistration?.message}
              />
            )}
          />

          <Stack direction="row" spacing={2}>
            <Controller
              name="phone"
              control={control}
              render={({ field }) => (
                <FormField
                  {...field}
                  label="Telefone"
                  required
                  fullWidth
                  disabled={isSubmitting}
                  errorMessage={professionalErrors.phone?.message}
                />
              )}
            />
            <Controller
              name="cpf"
              control={control}
              render={({ field }) => (
                <FormField
                  {...field}
                  label="CPF"
                  required
                  fullWidth
                  disabled={isSubmitting}
                  errorMessage={professionalErrors.cpf?.message}
                />
              )}
            />
          </Stack>

          <Controller
            name="birthDate"
            control={control}
            render={({ field }) => (
              <FormField
                {...field}
                label="Data de nascimento"
                type="date"
                required
                disabled={isSubmitting}
                slotProps={{ inputLabel: { shrink: true } }}
                errorMessage={professionalErrors.birthDate?.message}
              />
            )}
          />
        </>
      )}
    </Stack>
  );
}
