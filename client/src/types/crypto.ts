export interface KeyPair {
  publicKey: string;
  privateKey: string;
}

export interface EnvelopedPayload {
  ciphertext: string;
  nonce: string;
  ephemeralPublicKey: string;
  recipients: Record<string, string>;
}
