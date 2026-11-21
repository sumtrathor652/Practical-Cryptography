
# 1. CAESAR CIPHER

def caesar_encrypt(text, shift):
    result = ""
    for ch in text:
        if ch.isalpha():
            base = ord('A') if ch.isupper() else ord('a')
            result += chr((ord(ch) - base + shift) % 26 + base)
        else:
            result += ch
    return result


def caesar_decrypt(text, shift):
    return caesar_encrypt(text, -shift)


# 2. VIGENERE CIPHER

def vigenere_cipher(text, key, decrypt=False):
    result = ""
    key = key.upper()
    j = 0

    for ch in text:
        if ch.isalpha():
            shift = ord(key[j % len(key)]) - ord('A')

            if decrypt:
                shift = -shift

            base = ord('A') if ch.isupper() else ord('a')
            result += chr((ord(ch) - base + shift) % 26 + base)
            j += 1
        else:
            result += ch

    return result


# 3. RAIL FENCE CIPHER

def rail_fence_encrypt(text, rails):
    if rails <= 1 or rails >= len(text):
        return text

    fence = [''] * rails
    row = 0
    direction = 1

    for ch in text:
        fence[row] += ch

        if row == 0:
            direction = 1
        elif row == rails - 1:
            direction = -1

        row += direction

    return ''.join(fence)


def rail_fence_decrypt(cipher, rails):
    if rails <= 1 or rails >= len(cipher):
        return cipher

    pattern = []
    row = 0
    direction = 1

    for _ in cipher:
        pattern.append(row)

        if row == 0:
            direction = 1
        elif row == rails - 1:
            direction = -1

        row += direction

    counts = [pattern.count(r) for r in range(rails)]
    fence = []
    pos = 0

    for count in counts:
        fence.append(list(cipher[pos:pos + count]))
        pos += count

    indices = [0] * rails
    result = ''

    for r in pattern:
        result += fence[r][indices[r]]
        indices[r] += 1

    return result


# DEMONSTRATION

message = "HELLOWORLD"

# Caesar
c = caesar_encrypt(message, 3)
print("Caesar Encrypted:", c)
print("Caesar Decrypted:", caesar_decrypt(c, 3))

# Vigenere
v = vigenere_cipher(message, "KEY")
print("Vigenere Encrypted:", v)
print("Vigenere Decrypted:", vigenere_cipher(v, "KEY", True))

# Rail Fence
r = rail_fence_encrypt(message, 3)
print("Rail Fence Encrypted:", r)
print("Rail Fence Decrypted:", rail_fence_decrypt(r, 3))
