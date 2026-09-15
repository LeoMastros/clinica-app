import type { EnvelopedPayload } from '../../types/crypto';

export function encrypt(
  _plaintext: string,
  _recipientPublicKeys: string[]
): EnvelopedPayload {
  // TODO: Generate an ephemeral x25519 key pair with tweetnacl.box.keyPair()
  // TODO: Encrypt the plaintext once with a random symmetric content key
  // TODO: Envelope the content key for each recipient public key
  //       (Crypto Rule: elliptic curve + enveloping, admin distributes keys)
  // TODO: Never encrypt IDs or dates (see UNENCRYPTED_FIELDS)
  // NO FUNCTIONAL CODE - Implementation guide only
  throw new Error('not implemented');
}
