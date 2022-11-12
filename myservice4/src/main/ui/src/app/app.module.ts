import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import {APP_BASE_HREF} from '@angular/common';

import { AppComponent } from './app.component';
import { UsersComponent } from './users/users.component';
import { HeaderComponent } from './header/header.component';
import { RemoveUserComponent } from './users/remove-user/remove-user.component';
import { CreateUserComponent } from './users/create-user/create-user.component';
import { PageNotFoundComponent } from './shared/components/page-not-found/page-not-found.component';

const routes: Routes = [
  {path: 'users', component: UsersComponent},
    {path: 'createuser', component: CreateUserComponent},
    {path: 'removeuser', component: RemoveUserComponent},
    {path: '', redirectTo: '/users', pathMatch: 'full'},
    {path: '**', component: PageNotFoundComponent}
];


@NgModule({
  declarations: [
    AppComponent,
    UsersComponent,
    HeaderComponent,
    RemoveUserComponent,
    CreateUserComponent,
    PageNotFoundComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [{provide: APP_BASE_HREF, useValue : '/' }],
  bootstrap: [AppComponent]
})
export class AppModule { }
