# The Syntax of NAPL

## Things to know
- NAPL does not use semi-colons to signify the end of a statement (like Python)
    - however, indentation does not define program structure (unlike Python)

## Comments:
- Java, C, C++ style for Comments

  ```
  // this is a end-line comment  
  /* this is a block comment */
  ```

## Variables:
- all variable's types are inferred by the compiler
    - variables declared with var are type-checked at compile time

  ```
  var myVariable = 10 // myVariable in inferred to have a type of Integer

  myVariable = true // error: myVariable is type-checked and cannot be a Boolean once it is an Integer
  ```
    - variables declared with const are type-checked at compile time and are not mutable

  ```
  const myConstantVar = 10 // myConstantVar is not mutable and set to an Integer

  myConstantVar = 5 // error: myConstantVar is not mutable

  myConstantVar = true // error: myConstantVar is not mutable and type-checked  
  ```
    - variables declared with dyn are not type-checked and their type can change during runtime

  ```
  dyn mySecondVar = 10 // mySecondVar is not type-checked at compile time

  mySecondVar = true // no error: mySecondVar is dynamic and can change types during runtime
  ```
    - variable declarations without assignments return null when used and are not type-checked until assigned (expect for dyn variables)

  ```
  var myVarVariable // will be type-checked when assigned a value; evaluates to null until then

  myVarVariable = 10 // now type-checked to be an Integer
  ```
    - cont variables must be assigned when declared and are evaluated at compile time (operations and math can still be preformed as long as they are also cont)

  ```
  cont x = 0

  cont y = 1

  cont z = + (x, y) // correct: still declared with cont functions and/or variables

  cont a = + (x, 3) // correct: numbers are considered cont

  var b = 3

  cont c = + (a, b) // error: b is not cont
  ```

## Functions

- functions are declared similarly to variables
    - functions are assigned with a "=" followed by a parameters list enclosed by "()" and delimited by ","
    - the function body is declared in "{}" after the "()"
    - functions are not required to return anything but always return null if no return is specified (subject to change)
    - functions that never return anything should be declared with var

  ```
  var myFunction = () {} // basic function structure; will return null when executed since there is not return statement
  ```
- functions declared with var can only contain return statements of one type and are type-checked

  ```
  var myFunction = (int x, int y) {return * (x, y)} // only returns type of int and is correctly declared with var
  ```
- functions declared with const can only use const variables and must return a const

  ```
  const myFunction = (const int x, const int y) {return * (x, y)} // correct: function is const and relies only on const variables

  const myFunction = (int x, int y) {return * {x, y}} // error: a const function can only use const parameters
  ```
- functions declared with dyn can return multiple types

  ```
  dyn myFunction = (dyn x, dyn y) {return * (x, y)} // correct: function is dyn and returns a dyn

  dyn myFunction = (int x, int y) {return * (x, y)} // iffy: var should be used since only one type is ever possibly returned although it is not illegal

  var myFunction = (dyn x, dyn y) {return * (x, y)} // error: the return type of myFunction is undefined since dyn variables are used
  ```
- functions that can either return or not return anything should be declared with dyn
   - if nothing is returned null is returned instead

  ```
  dyn myFunction = (dyn x) {
    if (< (x, 0)) { // if statements and comparisons explained later
      return x
    } else {
      print "Invalid X"
    }
  } // correct: the function can finish through returning or without returning so dyn should be used
  ```
- inside the parameters list "..." can be used to specify an unlimited number of additional arguments may be supplied

  ```
  var myFunction = (dyn x, dyn y) {return (int) * (x, y)} // ok: the return type is guaranteed to be castable to an int; may still result in an error if * (x, y) cannot be cast to an int

  var myFunction = (int a, int b, ...) { // example of using the unlimited arguments feature "..."
    var total = + (a, b)
    for (var i = 0; i < args.length(); i++) {
      total += args[i]
    }
    return total
  }
  ```
- functions can also be stored to variables

  ```
  var myVariable = 10

  var myFunction = (int a) {
    return * (a, 10)
  }

  myVariable = myFunction; // legal: they are both var and return the same type

  myFunction = 5 // also legal: again, the return type is still the same
  ```
- functions can also be stored to other functions with partial or all parameters specified

  ```
  var myFunction = (int x, int y) {return * (x, y)}

  var secondFunction = (int x) myFunction(x, 2)
  ```
    - can also be used with "..."

  ```
  var myFunction = (int x, int y, ...) {
    var total = + (a, b)
    for (var i = 0; i < args.length(); i++) {
      total += args[i]
    }
    return total
  }

  var secondFunction = (int x) myFunction(x, 2, 3) // legal
  ```
- functions can specify a default value if a parameter is not passed

  ```
  var myFunction = (int x; 5, int y; 2) {return * (x, y)}

  print myFunction() // outputs 10
  ```

## Starting point of the program:
- the program always starts with a function declared as main
- the parameters list must include "..." for arguments passed in from the command line
- return is not necessary but can be used for errors

  ```
  var main = (...) {
    return 0 // error code for successful completion
  }

  var main = (...) {

  } // perfectly fine because return is not required

  var main = (...) {
    return + (2, 3) // not correct use for main's return
  }
  ```

## Loops
- Same structure as Java, C, C++, and Javascript
  - For Loop

  ```
  for (initialization; condition; increment/decrement) {
    // code to loop through
  }
  ```
  - While Loop

  ```
  while (condition) {
    // code to loop through
  }
  ```
  - Do-While Loop

  ```
  do {
    // code to loop through
  } while (condition)
  ```

## Casting

- In NAPL, casting is a function unlike Java or C++
    - The typical structure is "castTo" followed by the type surrounded by "<>"
    - Can be used on both var and dyn variables and functions
    - Can be used to make a dyn variable a var variable

  ```
  var myVariable = 10

  var myOtherVariable = castTo<Double>(myVariable)

  dyn myDynVariable = 10

  var notADynVariable = castTo<Int>(myDynVariable) // legal: casting narrows a dyn to a var
  ```

## Objects

- NAPL is a prototype-based programming language similar to Javascript.
    - objects are created using functions as the base template which provides the constructor
    - the prototype of a object can be changed by modifying the _proto on the new object

  ```
  var PointObject = (int x, int y) { // PointObject works as a factory to create point objects and supplies the initial constructor
    this.x = x
    this.y = y
  }

  PointObject.printSomething = () {
    print "Something"
  }

  var myPoint = new PointObject (0, 0) // The prototype of myPoint is now PointObject

  PointObject.someFunction = () { // Adds the function someFunction to the PointObject
    print "Hi"
  }

  PointObject.someFunction // Not valid: PointObject is not actually an object and is more of a template

  myPoint.someFunction // Valid: The program will search for someFunction on the prototype tree and finds it on PointObject; will output "Hi"

  myPoint.someFunction = () {
    print "Bye"
  }

  myPoint.printSomething // Outputs "Something" by looking for the function on the prototype

  myPoint.someFunction // Prints "Bye" but does not alter PointObject

  var otherPoint = new PointObject (1,1)

  otherPoint.someFunction // Prints "Hi" from the prototype

  otherPoint._proto.someFunction = () { // Can be dangerous as all PointObjects are changed unless they override the prototype function
    print "Hello"
  }

  otherPoint.someFunction // Prints "Hello"

  myPoint.someFunction // Still prints "Bye" because myPoint has its own someFunction

  var lastPoint = new PointObject(2,2)

  lastPoint.someFunction // Prints "Hello" because lastPoint does not have it's own someFunction and therefore uses the one provided by PointObject
  ```

## Packages

- Packages in NAPL are created by exporting functions/objects using the export() function.
    - The export() function requires a String followed by any number of functions/objects.

  ```
  var someFunction = () {
    print "I am a function"
  }

  var anotherFunction = () {
    print "I'm another function"
  }

  export("myPackage", someFunction)
  ```
    - Other files my now access "someFunction" and "anotherFunction" by importing "myPackage"

  ```
  import myPackage
  myPackage.someFunction() // Prints "I am a function"
  ```