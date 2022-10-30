// demo ES6 Classes


class Car {
  constructor(name) {
    this.brand = name;
  }

  Present = () => {
    return <h3>I am a {this.brand} </h3>;
  }

  Garage = () => {
    return (
      <>
        <h1>Who lives in my Garage?</h1>
        <this.Present />
      </>
    );
  }
}

class Model extends Car {
  constructor(name, mod) {
    super(name);
    this.model = mod;
  } 

  Present = () => {
    return (
      <>
        <super.Present />
        <h3>Its model is {this.model}.</h3>
      </>
    );
  }

}

export const mycar = new Model("Ford", "Mustang");

export default Model;