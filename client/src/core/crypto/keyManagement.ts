import type { KeyPair } from '../../types/crypto';

export function generateKeyPair(): KeyPair {
  // TODO: Verify canGenerateCryptoKeys(currentUser.role) before generating
  //       (Business Rule: only admin can generate new keys)
  // TODO: Generate an x25519 key pair and persist the public key on the backend
  // TODO: Warn that key loss is irreversible (no-deletion policy)
  // NO FUNCTIONAL CODE - Implementation guide only
  throw new Error('not implemented');
}

export function rotateRecipientKeys(_userId: string): void {
  // TODO: Re-envelope existing content keys for the new recipient public key
  // NO FUNCTIONAL CODE - Implementation guide only
}
