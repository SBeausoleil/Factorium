# Factorium
A simple generator and factory framework.

## Compatibility
Compatibility is guaranteed for Java 8 and up.

For Java 7: https://github.com/SBeausoleil/Factorium-J7

## How to use it
The core interfaces of Factorium are **Generator**, **Factory**, and **Modifier**.
Generators are the core of this framework, and in fact, factories are also just groups of generators with some sauce on top.
Modifiers serve to optionally modify a new object before it is returned.

### Generators

A generator's job is to create an object and apply modifiers to it. A new object with random values should be generated
at each new call to its methods. To simplify the application of modifiers, the **BaseGenerator** abstract class is provided.

>It is recommended to always extend from BaseGenerator or one of its subclasses when creating a generator as it guarantees 
>protection against possible (unlikely) future API changes.

>The FakerGenerator is a BaseGenerator with a static faker instance to simplify the creation of objects with random data. 
>All examples in this readme assume usage of the FakerGenerator.

Simply create a subclass of **BaseGenerator** (or FakerGenerator) and implement the `protected T make()` method. Like so:

```java

public class CityGenerator extends FakerGenerator<City> {
    @Override
    protected City make() {
        return new City(faker.name().lastName(), Math.abs(faker.random().nextLong()) + 1);
    }
}
```

And call it like:
```java
    City foo = new CityGenerator().generate();
```

**But what if I want to have specialized versions of my generator for different scenarios?** Simple, just create a new generator. 
I suggest keeping generators for a same class, in the same class with the outer class being the default generator.

```java
public class CityGenerator extends FakerGenerator<City> {
    /**
     * Suggested key for the default city generator.
     */
    public static String KEY = "default";

    @Override
    protected City make() {
        return new City(faker.name().lastName(), Math.abs(faker.random().nextLong()) + 1);
    }

    public static class CapitalGenerator extends FakerGenerator<City> {
        /**
         * Suggested key for the capital city generator.
         */
        public static String KEY = "capital";

        @Override
        protected City make() {
            return new City(faker.country().capital(), Math.abs(faker.random().nextLong()) + 1);
        }
    }
}
```

**Cool, but I want like, 5 cities. How do I do that?** Just give an `int` as your first argument to the generator you call.

```java
    List<City> cities = new CityGenerator().generate(5);
```

### Modifiers
Modifiers modify a new object before it is returned to the caller. There are two modifiers provided out of the box:
* FieldMod
* MethodMod

Note that you may add as many modifiers, one after the other, to a `generate()` call. If you generate multiple objects at the same time,
all the modifiers will be applied to all the objects.

```java
    City city = new CityGenerator().generate(new FieldMod("nCitizens", 5), new MethodMod("setName", "Toronto"));
```

>Note that modifiers are usually applied **after** the object has been created.

### Factories
Factories are groups of generators that produce the same kind of output. They may be used to decorate your generators.
For example, the **RecordingFactory** registers all the objects its generators create, which can be useful if you want to
test a DAO.

A Factory is a Generator. To accomplish this contract, it must have a default generator that will be used when no specific
generator key is given.

>The easiest way to create a Factory is by using the **BaseFactory** class and implementing it's `decorate(T, Modifier[])` method.
