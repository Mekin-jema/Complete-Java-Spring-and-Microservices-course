# 001 — Casting, Conversion, and Promotions in Java

> Quick-reference for primitive/reference casting: widening, narrowing, boxing/unboxing, and numeric promotions with pitfalls and examples.

## Core Rules
- Widening (safe, implicit): `byte -> short -> int -> long -> float -> double`; `char -> int -> long -> float -> double`.
- Narrowing (unsafe, explicit cast): opposite direction; may overflow or truncate.
- `boolean` cannot be cast to/from numeric/char.
- In mixed numeric expressions, Java applies binary numeric promotion to at least `int` before evaluation.
- Casting changes the *view* of bits; converting via APIs may change the *value* (e.g., parsing Strings, `BigDecimal` precision).

## Widening Examples (implicit)
```java
int i = 42;           // int literal
long ln = i;          // int -> long
float f = ln;         // long -> float (may lose precision, but implicit)
double d = f;         // float -> double
char c = 'A';
int code = c;         // char -> int (65)
```

## Narrowing Examples (explicit)
```java
int big = 150;
byte small = (byte) big; // wraps to -106 (150 - 256)
long huge = 3_000_000_000L;
int clipped = (int) huge; // overflow, result: -1294967296

double precise = 12345.67;
int truncated = (int) precise; // 12345 (fraction lost)
char letter = (char) 65;       // 'A'
```

## Binary Numeric Promotion (in expressions)
- If any operand is `double` → all promoted to `double`.
- Else if `float` → to `float`.
- Else if `long` → to `long`.
- Else → to `int` (even for `byte`, `short`, `char`).

```java
byte b1 = 10, b2 = 20;
int sum = b1 + b2;      // result is int, not byte
byte sumByte = (byte) (b1 + b2); // need explicit cast
```

## char, byte, short specifics
- `char` is unsigned 16-bit; casting a negative `byte`/`short` to `char` reinterprets bits (may produce large positive value).
- Adding/subtracting chars promotes to `int`.

```java
char c1 = 'A';
char c2 = 3;
int r = c1 + c2;  // 68
char d = (char) r; // 'D'
```

## Floating-Point to Integer
- Cast truncates toward zero; no rounding.
- Beware of `NaN` and infinity: casting yields zero or the closest int range per JVM spec (`(int) Double.NaN == 0`).

## Integer to Floating-Point
- Large `long` values may lose precision in `float`/`double` beyond 2^24 (`float`) or 2^53 (`double`).

```java
long exact = 9_007_199_254_740_992L; // 2^53
double maybe = exact + 1; // still 9.007199254740992E15 (no +1 effect)
```

## Boxing and Unboxing
```java
Integer boxed = 42;      // boxing
int unboxed = boxed;     // unboxing
Long xl = 5L;            // boxing
// Be careful: unboxing null throws NullPointerException
Integer maybeNull = null;
// int x = maybeNull;   // NPE at runtime
```

## Casting vs Parsing
- Casting does *not* convert strings: `int x = (int) "123"; // compile error`
- Use parsing: `int x = Integer.parseInt("123");`

## String to Number Quick Map
- `Integer.parseInt`, `Long.parseLong`, `Double.parseDouble`, `Float.parseFloat`, `Short.parseShort`, `Byte.parseByte`, `Boolean.parseBoolean`.
- For safe parsing, catch `NumberFormatException` or use `Optional` wrappers.

## Numbers to String
- `String.valueOf(num)` or `Integer.toString(num)`; avoid string concatenation in tight loops (use `StringBuilder`).

## BigDecimal Precision Note
- When converting from `double`, prefer `BigDecimal.valueOf(double)` (not `new BigDecimal(double)`) to avoid binary floating-point artifacts.

```java
BigDecimal a = new BigDecimal(0.1);      // 0.10000000000000000555...
BigDecimal b = BigDecimal.valueOf(0.1);  // 0.1
```

## Overflow & Underflow Demos
```java
int max = Integer.MAX_VALUE;
int wrap = max + 1;   // -2147483648
byte b = 127;
b++;                  // wraps to -128
```

## Cast Precedence
- Cast has higher precedence than unary minus: `(byte) -1` vs `(byte) (-1)` are the same, but `-(byte)1` first casts, then negates.

```java
byte b = (byte) 130; // -126
byte c = (byte) -130; // 126 (cast after unary minus)
```

## Arrays and Casting
- Arrays are covariant for references: `String[]` is a `Object[]`, but inserting non-String causes ArrayStoreException.
- Primitive arrays are *not* covariant: cannot cast `int[]` to `long[]`.

```java
String[] names = new String[2];
Object[] objs = names;      // allowed (covariance)
objs[0] = 42;               // ArrayStoreException at runtime
```

## Generics and Casting
- Generics are erased; casts with raw types can hide runtime issues. Prefer parameterized types and `@SuppressWarnings("unchecked")` only when justified.

```java
List raw = new ArrayList();
raw.add(1);
List<String> strs = raw; // unchecked warning; runtime ClassCastException possible
```

## Pattern Matching & `instanceof` (Java 16+)
```java
Object obj = "hello";
if (obj instanceof String s) {
    System.out.println(s.toUpperCase());
}
```

## Common Pitfalls Checklist
- [ ] Forgot that `byte/short/char` arithmetic promotes to `int`.
- [ ] Unboxed a null wrapper → `NullPointerException`.
- [ ] Assumed casting changes value (it only reinterprets bits for primitives).
- [ ] Lost precision converting `long` to `float`/`double` or `double` to `int`.
- [ ] Used `new BigDecimal(double)` instead of `BigDecimal.valueOf`.
- [ ] Tried to cast `String` to number instead of parsing.
- [ ] Expected `boolean` to cast to/from numbers (it cannot).
- [ ] Covariant arrays causing `ArrayStoreException`.

## Mini Exercises
1) Show how `byte` overflow behaves when incrementing from 127 to 128.
2) Convert a `double price = 19.99` to `int` and discuss truncation; then round properly with `Math.round`.
3) Parse a hex string (`"0xFF"`) to an int; handle invalid input safely.
4) Demonstrate loss of precision when converting a large `long` to `double`.
5) Write a method that safely unboxes `Integer` to `int` with a default value when null.
6) Use `instanceof` pattern matching to handle `Number` subtypes differently (Integer vs Double).

## Quick Reference Table

| From | To | Implicit? | Notes |
| --- | --- | --- | --- |
| byte | short, int, long, float, double | Yes | Widening; float/double may round |
| short | int, long, float, double | Yes | |
| char | int, long, float, double | Yes | Unsigned → beware negative casts |
| int | long, float, double | Yes | float may lose precision |
| long | float, double | Yes | float loses precision beyond 2^24 |
| float | double | Yes | |
| double | float | No | Possible `Infinity` or `NaN` differences |
| double/float | long/int/short/byte/char | No | Truncates toward zero |
| boolean | anything | No | No casts allowed |

## Snippets for Reference
```java
// Safe unboxing with default
int safeUnbox(Integer val, int defaultValue) {
    return val != null ? val : defaultValue;
}

// Clamp after narrowing
int clampLongToInt(long value) {
    if (value > Integer.MAX_VALUE) return Integer.MAX_VALUE;
    if (value < Integer.MIN_VALUE) return Integer.MIN_VALUE;
    return (int) value;
}
```
