import { Component } from '@angular/core';
import { MovieService } from '../../service/movies.service';
import { Movie } from '../../model/movie.model';

@Component({
  selector: 'movieList',
  templateUrl: './movieList.component.html',
  styleUrls: ['./movieList.component.css']
})
export class MovieListComponent {
  private imagePosters: string[] = ["","","","","","","","","","","","","","","",""];

  private movieList: Movie[];

  constructor(private movieService: MovieService)
  {
    movieService.getRecomendations().subscribe(res => this.movieList = res);
  }
}
