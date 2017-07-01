import {Component, OnInit} from '@angular/core';
import {MovieService} from '../movie-service/movie.service'
import {Movie} from "../movie/movie.component";
import {MovieObject} from "../movie-model/movie.model";
import {Observable} from "rxjs/Observable";

@Component({
    selector: 'movie-view',
    templateUrl: './movie-view.component.html',
    styleUrls: ['./movie-view.component.css'],
    providers: [MovieService]
})

export class RootView implements OnInit {
  movies: MovieObject[];
  constructor(private movieService: MovieService) {}

  getMovies(): void {
    var o = this.movieService.getMovies()
    o.subscribe(movies => {
      this.movies = movies
    });
  }

  ngOnInit(): void {
    this.getMovies();
  }
}
