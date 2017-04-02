import { Component, OnInit,  } from '@angular/core';
import { Movie } from '../movie/movie';
import { MovieService} from '../movie-service/movie.service';
import { ShortMovieViewComponent } from '../short-movie-view/short-movie-view.component';

@Component({
  selector: 'movie-list',
  template: `
    <div id="movie-list">
      <short-movie-view *ngFor="let movie of movielist"
      [movie]="movie" class="arrange-horizontally">
      </short-movie-view>
    </div>
  `,
  styles: [`
   short-movie-view {
     display: inline-block;
    }
  `],
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
