import { forwardRef, useState } from 'react';

import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import IconButton from '@mui/material/IconButton';
import InputAdornment from '@mui/material/InputAdornment';

import { FormField } from './FormField';
import type { FormFieldProps } from './FormField';

/**
 * PasswordField - password input with a show/hide toggle.
 */
export type PasswordFieldProps = Omit<FormFieldProps, 'type'>;

export const PasswordField = forwardRef<HTMLDivElement, PasswordFieldProps>(
  function PasswordField(props, ref) {
    const [visible, setVisible] = useState(false);

    return (
      <FormField
        {...props}
        ref={ref}
        type={visible ? 'text' : 'password'}
        slotProps={{
          ...props.slotProps,
          input: {
            ...props.slotProps?.input,
            endAdornment: (
              <InputAdornment position="end">
                <IconButton
                  onClick={() => setVisible(current => !current)}
                  edge="end"
                  aria-label={visible ? 'Ocultar senha' : 'Mostrar senha'}
                  aria-pressed={visible}
                >
                  {visible ? <VisibilityOffIcon /> : <VisibilityIcon />}
                </IconButton>
              </InputAdornment>
            ),
          },
        }}
      />
    );
  }
);
