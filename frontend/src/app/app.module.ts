import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';
import { ShortMovieViewComponent } from './short-movie-view/short-movie-view.component';
import { MovieList } from './movie-list/movie-list.component';
import { ServiceMenu } from './service-menu/service-menu.component';
import { RootView } from './root-view/root.component';

import {AlertModule} from 'ng2-bootstrap';

@NgModule({
  imports:      [ BrowserModule, AlertModule.forRoot()],
  declarations: [ AppComponent, MovieList, ShortMovieViewComponent, ServiceMenu, RootView],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
