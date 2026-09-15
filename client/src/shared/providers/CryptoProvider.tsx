import { createContext } from 'react';
import type { ReactNode } from 'react';

import type { KeyPair } from '../../types/crypto';

export interface CryptoContextValue {
  keyPair: KeyPair | null;
  unlock: (passphrase: string) => Promise<void>;
}

export const CryptoContext = createContext<CryptoContextValue | undefined>(
  undefined
);

export function CryptoProvider({ children }: { children: ReactNode }) {
  // TODO: Load the user key pair from secure storage and expose unlock()
  // TODO: Keep the private key in memory only, never in localStorage
  // TODO: Expose encrypt/decrypt helpers bound to the current key pair
  // NO FUNCTIONAL CODE - Implementation guide only
  return (
    <CryptoContext.Provider value={null as unknown as CryptoContextValue}>
      {children}
    </CryptoContext.Provider>
  );
}
