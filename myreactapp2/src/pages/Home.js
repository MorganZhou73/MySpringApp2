import React from 'react';
import ReactDOM from 'react-dom';
import Basic1 from './Basic1.js';
import {header1} from './Basic1.js';

export default function Home(){
  const test1 = () => {
    var name = "Mike";
    var element = <h3>Good morning, {name} ! </h3>

    if(document.getElementById("greeting").innerHTML.indexOf(name) >= 0) {
      element = React.createElement('h3', { className: 'greeting' }, 'Good morning, Joe !');
    }

    ReactDOM.render( element, document.getElementById('greeting'));
  }

  function test2() {

    var obj = new Basic1();
    const element2 = obj.BasicEle1();

    ReactDOM.render( element2 , document.getElementById('greeting'));

    // addEventListener for the element rendering in element2 immediatly, so sometime may get "Cannot read properties of null"
    obj.testHeader();
    
    obj.arrayDemo();

    obj.setMessage(header1.toString());    
  }

  return (
  <>
    <h3>This is *Home page* </h3>   
    <p><button onClick={test1}>test1 </button></p>
    <p><button onClick={test2}>test2 - basic1</button></p>
    <p id="greeting"></p>
  </>  
  )
};
