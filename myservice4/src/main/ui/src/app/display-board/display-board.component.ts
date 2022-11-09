import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-display-board',
  templateUrl: './display-board.component.html',
  styleUrls: ['./display-board.component.css']
})

export class DisplayBoardComponent implements OnInit {

  constructor() { }

  @Input() userCount = 0;
  @Output() getUsersEvent = new EventEmitter();
  @Output() createUserEvent = new EventEmitter();
  @Output() removeUserEvent = new EventEmitter<string>();

  currentUserId = "";

  ngOnInit(): void {
  }

  onGetAllUsers() {
    this.getUsersEvent.emit('get all users');
  }

  onCreateUser(){
    this.createUserEvent.emit('create new user');
  }

  onRemoveClicked(userid: string) {
    this.removeUserEvent.emit(userid);

    this.currentUserId = "";
  }

}
