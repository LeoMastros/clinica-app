import { Component } from 'react';
import type { ErrorInfo, ReactNode } from 'react';

import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';

interface ErrorBoundaryProps {
  children: ReactNode;
}

interface ErrorBoundaryState {
  error: Error | null;
}

export class ErrorBoundary extends Component<
  ErrorBoundaryProps,
  ErrorBoundaryState
> {
  state: ErrorBoundaryState = { error: null };

  static getDerivedStateFromError(error: Error): ErrorBoundaryState {
    return { error };
  }

  componentDidCatch(error: Error, info: ErrorInfo) {
    console.error('Erro não tratado na aplicação', error, info);
  }

  render() {
    const { error } = this.state;
    if (!error) return this.props.children;

    return (
      <Box sx={{ p: 3 }}>
        <Alert severity="error">
          <AlertTitle>Algo deu errado</AlertTitle>
          {error.message}
        </Alert>
        <Button
          sx={{ mt: 2 }}
          variant="contained"
          onClick={() => window.location.reload()}
        >
          Recarregar
        </Button>
      </Box>
    );
  }
}
