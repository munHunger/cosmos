import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';

import { Navbar } from './component/navbar.component';

@NgModule({
  imports: [ BrowserModule ],
  declarations: [ AppComponent, Navbar ],
  bootstrap: [ AppComponent, Navbar ]
})
export class AppModule { }
