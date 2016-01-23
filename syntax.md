# The Syntax of NAPL

## Comments:
- Java, C, C++ style for Comments

```
// this is a end-line comment  
/* this is a block comment */
```

## Variable Declaration:
- variables declared with var are type-checked at compile time
- variables declared with const are type-checked at compile time and are not mutable
- variables declared with dyn are not type-checked and their type can change during runtime
- variable declarations without assignments return null when used and are not type-checked until assigned (expect for dyn variables)
- cont varaibles must be assigned when declared and are evaluated at compile time (operations and math can still be preformed as long as they are also cont)

```
var myVariable = 10 // myVariable in inferred to have a type of Integer

myVariable = true // error: myVariable is type-checked and cannot be a Boolean once it is an Integer

var myVarVariable // will be type-checked when assigned a value; evaluates to null until then

myVarVariable = 10 // now type-checked to be an Integer

dyn mySecondVar = 10 // mySecondVar is not type-checked at compile time

mySecondVar = true // no error: mySecondVar is dynamic and can change types during runtime

const myConstantVar = 10 // myConstantVar is not mutable and set to an Integer

myConstantVar = 5 // error: myConstantVar is not mutable

myConstantVar = true // error: myConstantVar is not mutable and type-checked  
```

## Function Declaration
- functions are declared similarly to variables
- functions declared with var can only contain return statements of one type and are type-checked
- functions declared with const can only use const variables and must return a const
- functions declared with dyn can return multiple types
- functions are assigned with a "=" followed by a parameters list enclosed by "()" and delimited by ","
- the function body is declared in "{}" after the "()"
- functions are not required to return anything but always return null if not return is specified (subject to change)
- functions that never return anything should be declared with var
- functions that can either return or not return anything should be declared with dyn

```
var myFunction = () {} // basic function structure; will return null when executed since there is not return statement

var myFunction = (int x, int y) {return * (x, y)} // only returns type of int and is correctly declared with var

dyn myFunction = (int x, int y) {return * (x, y)} // var should be used since only one type is ever possibly returned although it is not illegal

const myFunction = (const int x, const int y) {return * (x, y)} // correct: function is const and relies only on const variables

const myFunction = (int x, int y) {return * {x, y}} // error: a const function can only use const parameters

var myFunction = (dyn x, dyn y) {return * (x, y)} // error: the return type of myFunction is undefined since dyn variables are used

var myFunction = (dyn x, dyn y) {return (int) * (x, y)} // ok: the return type is guaranteed to be castable to an int; may still result in an error if * (x, y) cannot be cast to an int  
```
