public class Hello {

  public static void main(String[] args) {
    System.out.println("=== Primitive vs Non-Primitive Deep Dive ===");

    // ---------------------------------------------------------------------------
    // Primitive overview
    // ---------------------------------------------------------------------------
    // Count: 8 primitives. They are value types (stored directly) and have default
    // values only when used as fields (not local variables):
    //   byte    (1 byte, signed)    : -128 .. 127
    //   short   (2 bytes, signed)   : -32,768 .. 32,767
    //   int     (4 bytes, signed)   : -2,147,483,648 .. 2,147,483,647
    //   long    (8 bytes, signed)   : ~-9.22e18 .. 9.22e18 (suffix L)
    //   float   (4 bytes, IEEE 754) : ~7 decimal digits, suffix f/F
    //   double  (8 bytes, IEEE 754) : ~15 decimal digits, default for decimals
    //   char    (2 bytes, unsigned) : UTF-16 code unit (0..65,535)
    //   boolean (size is JVM dependent) : true/false; not numeric
    // Wrapper classes (Byte, Short, Integer, Long, Float, Double, Character,
    // Boolean) are reference types that box/unbox these primitives.

    // ---------------------------------------------------------------------------
    // Integer family examples, literals, and overflow notes
    // ---------------------------------------------------------------------------
    byte byteMin = Byte.MIN_VALUE;      // -128
    byte byteMax = Byte.MAX_VALUE;      // 127
    short shortMin = Short.MIN_VALUE;   // -32768
    short shortMax = Short.MAX_VALUE;   // 32767
    int intMin = Integer.MIN_VALUE;     // -2147483648
    int intMax = Integer.MAX_VALUE;     // 2147483647
    long longMin = Long.MIN_VALUE;      // -9223372036854775808
    long longMax = Long.MAX_VALUE;      // 9223372036854775807

    // Literals: decimal, binary (0b), octal (0 prefix), hex (0x), underscores.
    int hex = 0xFF;          // 255
    int binary = 0b1010_0110; // 166 using underscores for readability
    int octal = 0777;        // 511
    long cardNumberChunk = 12_345_678_901L; // underscores allowed between digits

    // Overflow demo: adding 1 to Integer.MAX_VALUE wraps around (two's complement).
    int overflowed = intMax + 1; // wraps to -2147483648

    System.out.println("byte range    : " + byteMin + " .. " + byteMax);
    System.out.println("short range   : " + shortMin + " .. " + shortMax);
    System.out.println("int range     : " + intMin + " .. " + intMax);
    System.out.println("long range    : " + longMin + " .. " + longMax);
    System.out.println("hex 0xFF      : " + hex + ", binary 0b1010_0110 : " + binary + ", octal 0777 : " + octal);
    System.out.println("underscore long: " + cardNumberChunk);
    System.out.println("overflow example (intMax + 1): " + overflowed);

    // Narrowing requires casts; widening happens automatically.
    int widenedFromByte = byteMax;       // widening (safe)
    byte narrowedFromInt = (byte) intMax; // narrowing (data loss)
    System.out.println("widenedFromByte: " + widenedFromByte + ", narrowedFromInt: " + narrowedFromInt);

    // ---------------------------------------------------------------------------
    // Floating-point: precision, special values, and casting
    // ---------------------------------------------------------------------------
    float floatPi = 3.1415927f;           // ~7 digits of precision
    double doublePi = 3.141592653589793;  // ~15 digits of precision
    double big = 1e308;                   // near Double.MAX_VALUE
    double beyond = big * 10;             // Infinity
    double nan = 0.0 / 0.0;               // NaN (not a number)
    float preciseToFloat = (float) doublePi; // narrowing; loses precision

    System.out.println("float pi      : " + floatPi);
    System.out.println("double pi     : " + doublePi);
    System.out.println("double big    : " + big);
    System.out.println("double beyond : " + beyond);
    System.out.println("double nan    : " + nan);
    System.out.println("double->float : " + preciseToFloat);

    // Beware of floating comparison: use epsilon for doubles.
    double a = 0.1 * 3; // not exactly 0.3 in binary floating point
    double b = 0.3;
    boolean equalDirect = (a == b);
    boolean equalEpsilon = Math.abs(a - b) < 1e-9;
    System.out.println("0.1 * 3 == 0.3 ? " + equalDirect + " (direct) vs " + equalEpsilon + " (epsilon)");

    // ---------------------------------------------------------------------------
    // char: UTF-16 code unit; can be literal, decimal, hex, or Unicode escape
    // ---------------------------------------------------------------------------
    char letterA = 'A';           // direct literal
    char unicodeHeart = '\u2665'; // Unicode escape (heart symbol)
    char decimalCode = 65;        // decimal code point for 'A'
    char surrogateHigh = '\uD83D'; // part of surrogate pair (not a full emoji)

    System.out.println("char literal  : " + letterA);
    System.out.println("char unicode  : " + unicodeHeart);
    System.out.println("char decimal  : " + decimalCode);
    System.out.println("surrogate only: " + surrogateHigh + " (needs low surrogate for full codepoint)");

    // ---------------------------------------------------------------------------
    // boolean: logical true/false; cannot be treated as 0/1 in Java
    // ---------------------------------------------------------------------------
    boolean featureFlag = true;
    boolean isAdult = 20 >= 18;
    System.out.println("featureFlag: " + featureFlag + ", isAdult: " + isAdult);

    // ---------------------------------------------------------------------------
    // Wrapper classes: boxing, unboxing, null safety
    // ---------------------------------------------------------------------------
    Integer boxed = Integer.valueOf(42); // explicit boxing
    int unboxed = boxed;                 // unboxing
    Integer nullable = null;             // can be null (primitives cannot)
    System.out.println("boxed: " + boxed + ", unboxed: " + unboxed + ", nullable is null? " + (nullable == null));

    // ---------------------------------------------------------------------------
    // Non-primitive (reference) overview
    // ---------------------------------------------------------------------------
    // Stored as references on stack that point to heap objects. Default value for
    // fields is null. Categories: classes, arrays, interfaces, enums, records,
    // and also special references like String (immutable), var handles, etc.

    // Strings (immutable): any modification creates a new object.
    String course = "Java & Spring Boot";
    String updatedCourse = course.replace("Java", "Java SE"); // new instance
    System.out.println("course: " + course + " | updatedCourse: " + updatedCourse);

    // Arrays (fixed length, zero-based, length field available)
    int[] scores = {90, 85, 95};
    System.out.println("scores length: " + scores.length + ", first: " + scores[0]);

    // Multi-dimensional (arrays of arrays)
    int[][] matrix = {
        {1, 2, 3},
        {4, 5, 6}
    };
    System.out.println("matrix[1][2]: " + matrix[1][2]);

    // Records (Java 16+): concise data carriers (reference types)
    // Example usage in comments only; not defining here to keep the file simple.

    // Enums: type-safe constants with behavior.
    // Example inline definition:
    Day today = Day.WEDNESDAY;
    System.out.println("Enum example, today is: " + today + ", isWeekend? " + today.isWeekend());

    // Classes and objects: reference semantics, stored on heap, passed by value
    // (the reference is copied, not the object).
    Person p1 = new Person("Ada", 36);
    Person p2 = p1; // same reference
    p2.age = 37;    // mutates the same object
    System.out.println("Person p1 age after p2 change: " + p1.age);

    // Interfaces: define contracts; classes implement them.
    Greeter greeter = new ConsoleGreeter();
    greeter.greet("developers");

    // ---------------------------------------------------------------------------
    // Type promotion in expressions
    // ---------------------------------------------------------------------------
    byte x = 10;
    byte y = 20;
    // x + y is promoted to int; assignment to byte needs cast
    byte sum = (byte) (x + y);
    int promotedSum = x + y;
    long mixed = promotedSum + 5L; // int promoted to long
    double mixedFloat = mixed + 0.5; // long promoted to double
    System.out.println("byte sum with cast: " + sum + ", promotedSum(int): " + promotedSum + ", mixed(long): " + mixed + ", mixedFloat: " + mixedFloat);

    // ---------------------------------------------------------------------------
    // Casting pitfalls
    // ---------------------------------------------------------------------------
    int bigInt = 1_000_000;
    short narrowed = (short) bigInt; // data loss
    double precise = 12345.6789;
    int truncated = (int) precise;   // fractional part lost
    System.out.println("narrowed short (from 1_000_000): " + narrowed + ", truncated int (from 12345.6789): " + truncated);

    // ---------------------------------------------------------------------------
    // Autoboxing/unboxing pitfalls
    // ---------------------------------------------------------------------------
    Integer a1 = 128;
    Integer a2 = 128;
    Integer c1 = 100;
    Integer c2 = 100;
    System.out.println("128 objects with == ? " + (a1 == a2) + " (outside cache)");
    System.out.println("100 objects with == ? " + (c1 == c2) + " (inside Integer cache -128..127)");

    // ---------------------------------------------------------------------------
    // Null vs empty vs default values
    // ---------------------------------------------------------------------------
    String nullString = null;
    String emptyString = "";
    int[] emptyArray = new int[0];
    System.out.println("nullString is null? " + (nullString == null) + ", emptyString length: " + emptyString.length() + ", emptyArray length: " + emptyArray.length);

    // ---------------------------------------------------------------------------
    // Summary table (quick lookup) - kept as comments for reference
    // ---------------------------------------------------------------------------
    // Primitive quick facts:
    //   Kind      Size    Default(field)   Notes
    //   byte      1 B     0                signed, wraps on overflow
    //   short     2 B     0                signed
    //   int       4 B     0                most common integral type
    //   long      8 B     0L               use suffix L for literals
    //   float     4 B     0.0f             IEEE 754, suffix f/F
    //   double    8 B     0.0d             IEEE 754, default for decimals
    //   char      2 B     '\u0000'          unsigned UTF-16 code unit
    //   boolean   JVM dep false             true/false only
    // Reference quick facts:
    //   Default is null; store references; live on heap; arrays know length;
    //   Strings immutable; collections vary in mutability; records/enums are
    //   reference types with special syntax.

    // ---------------------------------------------------------------------------
    // Small demonstrations to reinforce concepts
    // ---------------------------------------------------------------------------
    demoArrayReferences();
    demoStringImmutability();
    demoFloatingPitfall();
  }

  // Demonstrates that arrays are references; copying the reference shares data.
  private static void demoArrayReferences() {
    int[] original = {1, 2, 3};
    int[] alias = original; // both point to same array
    alias[0] = 99;          // mutates original through alias
    System.out.println("demoArrayReferences -> original[0]: " + original[0]);
  }

  // Shows that String modifications return new instances.
  private static void demoStringImmutability() {
    String base = "hello";
    String upper = base.toUpperCase();
    System.out.println("demoStringImmutability -> base: " + base + ", upper: " + upper);
  }

  // Demonstrates floating-point accumulation error.
  private static void demoFloatingPitfall() {
    double total = 0.0;
    for (int k = 0; k < 10; k++) {
      total += 0.1; // expected 1.0 exactly, but binary floating point accumulates error
    }
    System.out.println("demoFloatingPitfall -> total: " + total + " (expected 1.0)");
  }

  // Simple enum to demonstrate reference types with behavior.
  enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    boolean isWeekend() {
      return this == SATURDAY || this == SUNDAY;
    }
  }

  // Simple class to show reference semantics.
  static class Person {
    String name;
    int age;
    Person(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

  // Simple interface and implementation for demonstration.
  interface Greeter {
    void greet(String audience);
  }

  static class ConsoleGreeter implements Greeter {
    @Override
    public void greet(String audience) {
      System.out.println("Hello, " + audience + "!");
    }
  }
}
