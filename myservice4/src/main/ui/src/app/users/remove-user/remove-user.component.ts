import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-display-board',
  templateUrl: './remove-user.component.html',
  styleUrls: ['./remove-user.component.css']
})

export class RemoveUserComponent implements OnInit {
  currentUserId = "";

  constructor(private userServiceService: UserService) { }

  ngOnInit(): void {
  }

  onRemoveClicked(userid: string) {
    this.userServiceService.removeUser(userid)
    .subscribe((response: any) => {
      console.log('remove user::::', userid);
    });

    this.currentUserId = "";
  }
  
}
