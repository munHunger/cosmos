import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { ShortMovieViewComponent } from './short-movie-view/short-movie-view.component';
import { MovieList } from './movie-list/movie-list.component';
import { ServiceMenu } from './service-menu/service-menu.component';
import { RootView } from './root-view/root.component';


@NgModule({
  declarations: [
    AppComponent, ShortMovieViewComponent, MovieList, ServiceMenu, RootView
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
