/**
 * Created by falapen on 2017-06-25.
 */
import {Component, Input} from "@angular/core";
import {MovieObject} from "../movie-model/movie.model";

@Component({
  selector: 'movie',
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.css']
})

export class Movie {
  @Input() movieObject: MovieObject;
}
