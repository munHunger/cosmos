import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';
import { ShortMovieViewComponent } from './short-movie-view/short-movie-view.component';
import { MovieList } from './movie-list/movie-list.component';

@NgModule({
  imports:      [ BrowserModule ],
  declarations: [ AppComponent, MovieList, ShortMovieViewComponent],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
