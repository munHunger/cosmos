import { Component, Input } from '@angular/core';
import { Movie } from '../movie/movie';

@Component({
  selector: 'short-movie-view',
  templateUrl: './short-movie-view.component.html',
  styleUrls: ['./short-movie-view.component.css']
})

export class ShortMovieViewComponent {
  @Input() movie: Movie;
}
