// demo Arrow functions
import React from 'react';
import ReactDOM from 'react-dom';

class Header {
    constructor(color) {
      this.color = color;
    }
    
    toString() {
        return "Header - " + this.color;
    }
    
    // In regular functions the this keyword represented the object that called the function, 
    // which could be the window, the document, a button or whatever.
    changeColor = function() {
      document.getElementById("demo").innerHTML += this.innerHTML ; 
    }
    
    // With arrow functions, the this keyword always represents the object that defined the arrow function.(Header object)
    changeColor2 = () => {
      document.getElementById("demo").innerHTML += this;
    }

    // clear = () =>{
    //     document.getElementById("demo").innerHTML = "" ;
    // }
  
    showMessage = () => {
      document.getElementById("demo").innerHTML = this.message ;
    }  
  }

class Basic1 {
    clear() {
      document.getElementById("demo").innerHTML = "" ;
    }

    BasicEle1() {
        return (
            <div>
            <p>The <strong>this</strong> keyword represents different objects depending on how the function was called.</p>
    
            <button id="btn1">btn1</button>
            <button id="btn2">btn2</button>
            <button id="btnShow">showMessage </button>
            <button id="btnClear" onClick={this.clear}>clear </button>
            <p id="demo"></p>
            </div>
        );        
    }
 
    setMessage(message){
        this.myheader.message = message;
    }

    testHeader() {
        this.myheader = new Header('Red');

        //The window object calls the function:
        //window.addEventListener("load1", this.myheader.changeColor);

        //A button object calls the function:
        document.getElementById("btn1").addEventListener("click", this.myheader.changeColor);

        //The Header object calls the function:
        //window.addEventListener("load2", this.myheader.changeColor2);

        //the Header object calls the function:
        document.getElementById("btn2").addEventListener("click", this.myheader.changeColor2);

        document.getElementById("btnShow").addEventListener("click", this.myheader.showMessage);

        // document.getElementById("btnClear").addEventListener("click", this.myheader.clear);
    }

// var foo = "Foo";  // globally scoped
// let bar = "Bar"; // not allowed to be globally scoped

// console.log(window.foo); // Foo
// console.log(window.bar); // undefined

 checkHoisting() {
	// console.log(foo); // ReferenceError
	let foo = "Foo";
	console.log(foo); // Foo

	// console.log(foo1); // undefined
	var foo1 = "Foo1";
	console.log(foo1); // Foo
}

// Destructuring arrays
calculate(a, b) {
    const add = a + b;
    const subtract = a - b;
    const multiply = a * b;
    const divide = a / b;
  
    return [add, subtract, multiply, divide];
  }
 
// Destructuring Objects  
  myVehicle({type, color, brand, model, registration: { state } }) {
    const message = 'My ' + type + ' is a ' + color + ' ' + brand + ' ' + model + '. '
        + "It is registered in " + state + '.';
  
    return message;
  }
  // output: My car is a red Ford Mustang. It is registered in Texas.


  // demo array and spread operator (...) 
 arrayDemo() {
    const myArray = ['0-apple', '1-banana', '2-orange', '3-date'];
    const myArray2 = ['4-pear', '5-graple'];
    const myArray3 = [...myArray, ...myArray2]; 
    const myList = myArray3.map((item) => <p>{item}</p>)

    // Destructing Arrays
    const [a0, a1, a2] =  myArray;
    
    // same as 
    // const a0 = myArray[0];
    // const a1 = myArray[1];
    // const a2 = myArray[2];

    const [a3, , a5] =  myArray;

    const [, ...rest] =  myArray;

    const vehicle1 = {
        brand: 'Ford',
        model: 'Mustang',
        type: 'car'
    }
 
    const vehicle2 = {
        year: 2021, 
        color: 'red',
        registration: {
            city: 'Houston',
            state: 'Texas',
            country: 'USA'
          }
      } 

    const vehicle3 = {...vehicle1, ...vehicle2};
   // same as 
    // const vehicle3 = {
    //     brand: 'Ford',
    //     model: 'Mustang',
    //     type: 'car',
    //     year: 2021, 
    //     color: 'red',
    //     registration: {
    //         city: 'Houston',
    //         state: 'Texas',
    //         country: 'USA'
    //       }
    //   } 
 
    const [add, subtract, multiply, divide] = this.calculate(3, 6);

    document.getElementById("demo").innerHTML += "a2: " + a2
        + " <br/> a3,,a5: " + a3 + ", ," + a5
        + " <br/> , ...rest: " + rest
        + " <br/> " + add + " , " + subtract + " , " + multiply + " , " + divide
        + " <br/> " + this.myVehicle(vehicle3);

   // ReactDOM.render( myList, document.getElementById('demo'));
}

}

export const header1 = new Header('Blue');

export default Basic1;