import type { EnvelopedPayload } from '../../types/crypto';

export function decrypt(
  _payload: EnvelopedPayload,
  _privateKey: string
): string {
  // TODO: Resolve the recipient envelope for the current user public key
  // TODO: Open the content key with tweetnacl.box.open using the ephemeral public key
  // TODO: Decrypt the ciphertext with the recovered content key
  // TODO: Throw a typed error when the current user has no envelope (no access)
  // NO FUNCTIONAL CODE - Implementation guide only
  throw new Error('not implemented');
}
