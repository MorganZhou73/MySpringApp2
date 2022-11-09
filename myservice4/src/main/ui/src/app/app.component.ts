import { Component, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { User } from './model/user';
import { UserService } from './services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnDestroy {

  constructor(private userServiceService: UserService) {}

  title = 'Unistar';

  users: User[] = [];
  userCount = 0;
  creatUserFlag = false;

  getAllUsers() {
    this.userServiceService.getUsers()
    .subscribe((response: any) => {
      if(!response){
        this.userCount = 0;
        return;
      }

      this.creatUserFlag = false;
	    this.userCount = response.length;
      this.users = response;
    });
  }

  showCreateUser(){
    this.creatUserFlag = true;
  }

  removeUser(userid: string){
    this.userServiceService.removeUser(userid)
    .subscribe((response: any) => {
      console.log('remove user::::', userid);
    });
  }

  ngOnDestroy() {

  }

  ngOnInit() {
	  this.getAllUsers();
  }

}
