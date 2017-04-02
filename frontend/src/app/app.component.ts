import { Component } from '@angular/core';
import { ShortMovieViewComponent } from './short-movie-view/short-movie-view.component';
import { MovieList } from './movie-list/movie-list.component';

@Component({
  selector: 'my-app',
  template: `
  <p>
    <movie-list>Loading the next nigga....</movie-list>
  </p>
  `,
})

export class AppComponent  { name = 'Angular'; }
