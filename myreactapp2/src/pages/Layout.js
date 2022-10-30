import React from 'react';
import { Outlet, Link, NavLink} from "react-router-dom";
import styled from "styled-components";

// Method 1: basic router style
function Nav1() {
  return (
      <ul>
        <li>
          <Link to="/">Home</Link>
        </li>
        <li>
          <Link to="/about">About</Link>
        </li>
        <li>
          <Link to="/contact">Contact</Link>
        </li>
      </ul>
  )
};

const NavUnlisted = styled.ul`
  text-decoration: none;
`;

const linkStyle = {
  margin: "1rem",
  textDecoration: "none",
  color: 'blue'
};

// Method 2: using inline style attribute for inline Nav links
function Nav2() {
  return (
    <NavUnlisted>
          <Link to="/" style={linkStyle}>Home</Link>
          <Link to="/about" style={linkStyle}>About</Link>
          <Link to="/contact" style={linkStyle}>Contact</Link>
    </NavUnlisted>
  );
}

const StyledLink = styled(Link)`
  color: Blue;
  text-decoration: none;
  margin: 1rem;
  position: relative;
`;

// Method 3: using styled.componentName for inline Nav links
function Nav3() {
  return (
    <NavUnlisted>
      <StyledLink to="/">Home</StyledLink>
      <StyledLink to="/about">About</StyledLink>
      <StyledLink to="/contact">Contact</StyledLink>
    </NavUnlisted>
  );
}

const NavUnlisted2 = styled.ul`
  display: flex;

  a {
    text-decoration: none;
  }

  li {
    color: blue;
    margin: 0 0.8rem;
    font-size: 1.3rem;
    position: relative;
    list-style: none;
  }

  .active {
    li {
      background-color: orange;
      border-bottom: 2px solid black;
    }
  }
  .nonactive {
    li {
    }
  }
`;

// Method 4: using 'NavLinks' and isActive : not work for "/", so mapped to "/home"
function Nav4a() {
  return (
    <NavUnlisted2>
      <NavLink to="/home" className={({isActive}) => isActive ? "active" :"nonactive"} >
        <li>Home</li></NavLink>
      <NavLink to="/about" className={({isActive}) => isActive ? "active" :"nonactive"} >
        <li>About</li></NavLink>
      <NavLink to="/contact" className={({isActive}) => isActive ? "active" :"nonactive"} >
        <li>Contact</li></NavLink>
    </NavUnlisted2>
  );
}


const links = [
  {name: "Home", path:"/home"},
  {name: "About", path:"/about"},
  {name: "Contact", path:"/contact"},
];

function Nav() {
  return (
    <NavUnlisted2>
      {
      links.map((link, index) =>(
        <NavLink key={index} to={link.path} className={({isActive}) => isActive ? "active" :"nonactive"} >
          <li>{link.name}</li>
        </NavLink>
      ))}
    </NavUnlisted2>
  );
}

export default function Layout(){
  return (
    <>
      <Nav />
      <Outlet />
    </>
  );
};

