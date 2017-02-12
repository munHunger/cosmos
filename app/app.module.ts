import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';

import { Navbar } from './component/navbar.component';
import { VideoItemCard } from './component/video-item.component';


@NgModule({
  imports: [ BrowserModule ],
  declarations: [ AppComponent, Navbar, VideoItemCard ],
  bootstrap: [ AppComponent, Navbar, VideoItemCard ]
})
export class AppModule { }
