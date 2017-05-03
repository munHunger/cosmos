import { Component } from '@angular/core';
import { ShortMovieViewComponent } from './short-movie-view/short-movie-view.component';
import { MovieList } from './movie-list/movie-list.component';
import { RootView } from './root-view/root.component';

@Component({
  selector: 'my-app',
  template: `
    <root-view>Loading the next nigga....</root-view>
  `,
})

export class AppComponent  { name = 'Angular'; }
