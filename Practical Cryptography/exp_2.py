
from collections import Counter
import matplotlib.pyplot as plt

ciphertext = input("Enter ciphertext: ").upper()

# Keep only alphabetic characters
letters = [ch for ch in ciphertext if ch.isalpha()]

# Calculate frequency
frequency = Counter(letters)
total = len(letters)

print("\nLetter Frequency Analysis")
for letter, count in sorted(frequency.items()):
    percentage = (count / total) * 100
    print(f"{letter}: {count} times ({percentage:.2f}%)")

# Plot the frequency distribution
if total > 0:
    alphabet = list("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    counts = [frequency.get(ch, 0) for ch in alphabet]

    plt.bar(alphabet, counts)
    plt.xlabel("Letters")
    plt.ylabel("Frequency")
    plt.title("Ciphertext Letter Frequency")
    plt.show()
else:
    print("No alphabetic characters found.")
