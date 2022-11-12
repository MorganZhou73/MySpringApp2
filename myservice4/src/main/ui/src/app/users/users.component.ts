import { Component,  Input,  OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  users: User[] = [];

  constructor(private router: Router,
    private userServiceService: UserService) { 
    // const state = this.router.getCurrentNavigation()?.extras.state ?? {};
    // this.users = state['users'] ?? [];
  }
  
  ngOnInit(): void {
      this.getAllUsers();
  }

  getAllUsers() {
    this.userServiceService.getUsers()
    .subscribe((response: any) => {
      if(!response){
        return;
      }

      this.users = response;
    });
  }
}
