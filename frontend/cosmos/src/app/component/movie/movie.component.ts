import { Component, Input } from '@angular/core';
import { MovieService } from '../../service/movies.service';
import { Movie } from '../../model/movie.model';

@Component({
  selector: 'movie',
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.css']
})
export class MovieComponent {
    @Input()
    private movie: Movie;

    constructor(private movieService: MovieService)
    {
    }
}
