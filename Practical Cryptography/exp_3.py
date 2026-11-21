
from Crypto.Cipher import DES, AES
from Crypto.Util.Padding import pad, unpad
from time import perf_counter

# Sample data
message = b"Hello, this is a secret message!"

# Demo keys: DES requires 8 bytes; AES-128 requires 16 bytes
des_key = b"8bytekey"
aes_key = b"Sixteen byte key"

# CBC mode requires an IV equal to the block size.
# Fixed IVs are used only to make this demonstration reproducible.
des_iv = b"12345678"
aes_iv = b"1234567890123456"


def encrypt_decrypt(algorithm, key, iv):
    cipher_class = DES if algorithm == "DES" else AES

    # Encryption
    encryptor = cipher_class.new(key, cipher_class.MODE_CBC, iv)
    ciphertext = encryptor.encrypt(
        pad(message, cipher_class.block_size)
    )

    # Decryption
    decryptor = cipher_class.new(key, cipher_class.MODE_CBC, iv)
    decrypted = unpad(
        decryptor.decrypt(ciphertext),
        cipher_class.block_size
    )

    return ciphertext, decrypted


# Test DES
des_ciphertext, des_plaintext = encrypt_decrypt(
    "DES", des_key, des_iv
)

# Test AES
aes_ciphertext, aes_plaintext = encrypt_decrypt(
    "AES", aes_key, aes_iv
)

print("Original message:", message.decode())

print("\n--- DES ---")
print("Encrypted (hex):", des_ciphertext.hex())
print("Decrypted:", des_plaintext.decode())

print("\n--- AES ---")
print("Encrypted (hex):", aes_ciphertext.hex())
print("Decrypted:", aes_plaintext.decode())


# Simple performance benchmark
data = b"A" * 1024
iterations = 10000

def benchmark(cipher_class, key, iv):
    start = perf_counter()

    for _ in range(iterations):
        cipher = cipher_class.new(
            key, cipher_class.MODE_CBC, iv
        )
        cipher.encrypt(pad(data, cipher_class.block_size))

    return (perf_counter() - start) / iterations * 1000


des_ms = benchmark(DES, des_key, des_iv)
aes_ms = benchmark(AES, aes_key, aes_iv)

print("\n--- Performance ---")
print(f"DES average: {des_ms:.6f} ms per operation")
print(f"AES average: {aes_ms:.6f} ms per operation")
print("Times depend on the computer and software implementation.")
