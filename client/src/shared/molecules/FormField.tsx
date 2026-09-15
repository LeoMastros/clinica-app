import { forwardRef, useId } from 'react';

import TextField from '@mui/material/TextField';
import type { TextFieldProps } from '@mui/material/TextField';

/**
 * FormField - Material UI text field with accessible label, hint and error message.
 */
export type FormFieldProps = Omit<TextFieldProps, 'error' | 'helperText'> & {
  /** Validation message rendered below the field. */
  errorMessage?: string;
  /** Guidance shown while the field has no error. */
  hint?: string;
};

export const FormField = forwardRef<HTMLDivElement, FormFieldProps>(
  function FormField({ errorMessage, hint, id, ...props }, ref) {
    const generatedId = useId();
    const fieldId = id ?? generatedId;
    const messageId = `${fieldId}-message`;
    const hasError = Boolean(errorMessage);

    return (
      <TextField
        {...props}
        id={fieldId}
        ref={ref}
        error={hasError}
        helperText={errorMessage ?? hint ?? ' '}
        slotProps={{
          ...props.slotProps,
          formHelperText: {
            id: messageId,
            role: hasError ? 'alert' : undefined,
          },
          htmlInput: {
            ...props.slotProps?.htmlInput,
            'aria-describedby': messageId,
            'aria-invalid': hasError,
          },
        }}
      />
    );
  }
);
