import { Component, OnInit,  } from '@angular/core';
import { Movie } from '../movie/movie';
import { MovieService} from '../movie-service/movie.service';
import { ShortMovieViewComponent } from '../short-movie-view/short-movie-view.component';

@Component({
  selector: 'movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css'],
  providers: [MovieService]
})

export class MovieList implements OnInit {
  movielist: Movie[];

  constructor(private movieService: MovieService) {}

  ngOnInit(): void {
    this.getMovies();
  }

  getMovies():void  {
    this.movielist = this.movieService.getShortMovieInfoList();
  }
}
