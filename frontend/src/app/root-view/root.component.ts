import {Component, OnInit} from '@angular/core';
import {MovieService} from '../movie-service/movie.service'
import {Movie} from "../movie/movie.component";
import {MovieObject} from "../movie-model/movie.model";

@Component({
    selector: 'root-view',
    templateUrl: './root.component.html',
    styleUrls: ['./root.component.css'],
    providers: [MovieService]
})

export class RootView implements OnInit {
  movies: MovieObject[];
  constructor(private movieService: MovieService) {}

  getMovies(): void {
    this.movieService.getMovies().then(movies => this.movies = movies);
  }

  ngOnInit(): void {
    this.getMovies();
  }
}
