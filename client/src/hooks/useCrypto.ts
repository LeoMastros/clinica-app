import { useContext } from 'react';

import { CryptoContext } from '../shared/providers/CryptoProvider';

export function useCrypto() {
  // TODO: Return encrypt/decrypt bound to the current key pair once CryptoProvider is functional
  // NO FUNCTIONAL CODE - Implementation guide only
  return useContext(CryptoContext);
}
